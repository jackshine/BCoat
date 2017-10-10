package com.elastica.beatle.tests.audit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.CommentPayload;

public class AuditTaggingTests extends AuditInitializeTests {

	protected Client restClient;
	
	ArrayList<JSONObject> tagsList=new ArrayList<JSONObject>();
	ArrayList<JSONObject> total=new ArrayList<JSONObject>();
	String serviceId="35";
	String serviceId2="15"; //box service id
	List<String> defaultTagsList=new ArrayList<String>();


	@BeforeClass(alwaysRun = true)
	public void init() throws Exception{
		restClient = new Client();
		defaultTagsList.add("Sanctioned");
		defaultTagsList.add("Unsanctioned");
		defaultTagsList.add("Block@FW");
		defaultTagsList.add("Ignore");
		
		
	}
	

	
	@DataProvider(name="tagAttributes")
	public Object[][] getTagAttributes() throws Exception
	{
		
		String[] colors={"0","1","2","3","4"};
		String[] patterns={"0","1","2","3"};
	
		Object[][] tagAttributes = new Object[20][3];
		int j=0;
		  for(String color:colors )
		{
	    	for(int i=0; i<patterns.length; i++){
	    		tagAttributes[j][0] = color;
				tagAttributes[j][1] = ""+i;
				tagAttributes[j][2] = "tag_"+color+i;
				j++;
	    	}
	    	
			
		}
		Reporter.log("tagAttributes:"+tagAttributes.length,true);
		
		
		for(int i=0; i<tagAttributes.length; i++) {
			System.out.println("color :" + tagAttributes[i][0]);
			System.out.println("pattern:" + tagAttributes[i][1]);
			System.out.println("tagname:" + tagAttributes[i][2]);
			
		}
		
		return tagAttributes;
	}
	


	@Test(priority=1,dataProvider="tagAttributes")	
	public void testAddNewTag(String color, String pattern, String tagname) throws Exception
	{
		JSONObject colorpattern = new JSONObject();
		colorpattern.put("color", color);
		colorpattern.put("pattern", pattern);
		
		JSONObject tagPayload = new JSONObject();
		tagPayload.put("tag_name", tagname);
		tagPayload.put("attributes", colorpattern);
		
		Reporter.log("tagPayload::"+tagPayload,true);
	
		
		HttpResponse  createTagResp=AuditFunctions.createTag(restClient, new StringEntity(tagPayload.toString()));
		Assert.assertEquals(createTagResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"No Content & Response code should be 204");
		JSONObject strTagResp = new JSONObject(ClientUtil.getResponseBody(createTagResp));
		Reporter.log("strTagResp...: "+strTagResp,true);
		
		//validate tag json
		Assert.assertNotNull(strTagResp.getString("id"), "tagObjectId is null");
		Assert.assertEquals(strTagResp.getString("tag_name"), tagname,"tagname eaual");
		Assert.assertNotNull(strTagResp.getString("tag_id"), "tagid is null");
		Assert.assertNotNull(strTagResp.get("created_by"), "created_by  is null");
		Assert.assertFalse(((String) strTagResp.get("created_by")).isEmpty(),"created_by by is empty");
		Assert.assertFalse(((String) strTagResp.get("created_on")).isEmpty(),"created_on by is empty");
		JSONObject tagAttributes= strTagResp.getJSONObject("attributes");
		Assert.assertEquals(tagAttributes.getString("color"), color,"tag color equal");
		Assert.assertEquals(tagAttributes.getString("pattern"), pattern,"tag pattern equal");
		tagsList.add(strTagResp);
		
		total.addAll(tagsList);
	
		Reporter.log("total...."+total,true);
	}
	
	
	
	@Test(priority=2)
	public void testEditTag() throws Exception
	{
	
		//create tag
		JSONObject getTagJsonObj=createTag();
		 String tagObjectId=getTagJsonObj.getString("id");
		
		//update tag
		//update tag request body preparation 
		String updatedtagname=getTagJsonObj.getString("tag_name")+"_updated";
		getTagJsonObj.remove("tag_name");
		getTagJsonObj.put("tag_name",updatedtagname);
		
		String updated_Color_id="2";
		String updated_pattern_id="3";
		
		
		JSONObject colorpattern = new JSONObject();
		colorpattern.put("color", updated_Color_id);
		colorpattern.put("pattern", updated_pattern_id);
		getTagJsonObj.remove("attributes");
		getTagJsonObj.put("attributes", colorpattern);
		
		
		HttpResponse modifiedresponse=AuditFunctions.editTag(restClient,new StringEntity(getTagJsonObj.toString()),tagObjectId);
		//Assert.assertEquals(modifiedresponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response Code should be 200");
		JSONObject modifiedObject = new JSONObject(ClientUtil.getResponseBody(modifiedresponse));
		
		Assert.assertEquals(modifiedObject.get("tag_name"), updatedtagname);
		Reporter.log("modifiedObject.::"+modifiedObject.get("modified_on"),true);
		Assert.assertNotNull(modifiedObject.get("modified_on"),"Modified timestamp null");
		Reporter.log("created timestmap..."+getTagJsonObj.get("modified_on"),true);
		Reporter.log("modified timestmap..."+modifiedObject.get("modified_on"),true);
		Assert.assertNotEquals(getTagJsonObj.get("modified_on"),modifiedObject.get("modified_on"),"Modified timestamp null");
		//Assert.assertNull(modifiedObject.get("modified_by"),suiteData.getUsername());
		
		JSONObject attributeJson = new JSONObject();
		attributeJson=modifiedObject.getJSONObject("attributes");
		Assert.assertEquals(attributeJson.getString("color"), updated_Color_id,"updated Color shoule be"); 
		Assert.assertEquals(attributeJson.getString("pattern"), updated_pattern_id,"updated Pattern shoule be");
		
		//delete the tag
		HttpResponse deleteTagResponse=AuditFunctions.deleteTag(restClient,tagObjectId);
		Assert.assertEquals(deleteTagResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT,"No Content & Response code should be 204");
		
	
	}
	
	
	
	@Test(priority=3)
	public void assignTagToService() throws Exception
	{
		//create a tag
		JSONObject colorpattern = new JSONObject();
		colorpattern.put("color", "1");
		colorpattern.put("pattern", "2");
		
		JSONObject tagPayload = new JSONObject();
		tagPayload.put("tag_name", "tag_dummy");
		tagPayload.put("attributes", colorpattern);
		
		Reporter.log("tagPayload::"+tagPayload,true);
	
		
		HttpResponse  createTagResp=AuditFunctions.createTag(restClient, new StringEntity(tagPayload.toString()));
		JSONObject strTagResp = new JSONObject(ClientUtil.getResponseBody(createTagResp));
		Reporter.log("strTagResp...: "+strTagResp,true);
		
		String tagObjectId=strTagResp.getString("id");
		String tagId=strTagResp.getString("tag_id");
	
		
		//assign tag to service
		 resetServiceTags(serviceId);
		JSONObject tagObjForService=new JSONObject();
		String[] servidId={serviceId};
		tagObjForService.put("service_ids", servidId);
		
		Integer[] add_tags={Integer.parseInt(tagId),Integer.parseInt(tagId)};
		tagObjForService.put("add_tags", add_tags);
		
		Integer[] remove_tags={};
		tagObjForService.put("remove_tags", remove_tags);
		
		Reporter.log("assigntagtoservice payload:.."+tagObjForService,true);
		
		HttpResponse addTagToService=AuditFunctions.changeTagToService(restClient,new StringEntity(tagObjForService.toString()));
		//JSONObject strTagAssignToservice = new JSONObject(ClientUtil.getResponseBody(addTagToService));
		//Reporter.log("strTagAssignToservice...: "+strTagAssignToservice,true);
		
		//verify tag assign to service or not
		//getservicetags
		
		JSONObject removeTagObj=new JSONObject();
		removeTagObj.put("service_ids", servidId);
		Integer[] add_tags1={};
		removeTagObj.put("add_tags", add_tags1);
		Integer[] remove_tags1={new Integer(tagId)};
		removeTagObj.put("remove_tags", remove_tags1);
		
		Reporter.log("assigntagtoservice payload:.."+removeTagObj,true);
		HttpResponse removeTagToService=AuditFunctions.changeTagToService(restClient,new StringEntity(removeTagObj.toString()));
		
		
		//delete tag
		HttpResponse deleteTagResponse=AuditFunctions.deleteTag(restClient,tagObjectId);
		Assert.assertEquals(deleteTagResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT,"No Content & Response code should be 204");
	
	}
	
	
	@DataProvider(name="defaultTags")
	public Object[][] getDefaultTags() throws Exception
	{
		
		Integer[] stDefaultTags={0,1,2};
		Object[][] defaultTags = new Object[stDefaultTags.length][2];
		int j=0;
		
		defaultTags[0][0] = new Integer[]{0};
		defaultTags[0][1] = new Integer[]{1,2};
		
		defaultTags[1][0] = new Integer[]{1};
		defaultTags[1][1] = new Integer[]{0,2};
		
		defaultTags[2][0] = new Integer[]{2};
		defaultTags[2][1] = new Integer[]{0,1};
		
		return defaultTags;
	}
	
	@Test(priority=4,dataProvider="defaultTags")	
	public void assingDefaultTagsToService(Integer[] addTags, Integer[] removeTags) throws Exception
	{
		 resetServiceTags(serviceId);
		 
		 //Assign default tags to service
		 Reporter.log("Assigning Default Tags to Service:",true);
		 JSONObject changeServiceTagPayload=new JSONObject();
		 changeServiceTagPayload.put("service_ids", new String[]{serviceId});
			changeServiceTagPayload.put("add_tags", addTags);
			changeServiceTagPayload.put("remove_tags", removeTags);
			
			Reporter.log("assingDefaultTagsToService payload:.."+changeServiceTagPayload,true);
			HttpResponse removeTagToService=AuditFunctions.changeTagToService(restClient,new StringEntity(changeServiceTagPayload.toString()));
		 
		
	}
	
	private void resetServiceTags(String serviceId) throws Exception
	{
        
		//find the list of tags assigned to the server
		HttpResponse serviceTagsListHttpResp=AuditFunctions.getServieTags(restClient);
		Assert.assertEquals(serviceTagsListHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response Code should be 200");
		JSONObject modifiedObject = new JSONObject(ClientUtil.getResponseBody(serviceTagsListHttpResp));
		
		JSONArray serviceTagsJsonArray=modifiedObject.getJSONArray("objects");
		JSONArray tagIdArray=null;
		for(int i=0; i<serviceTagsJsonArray.length(); i++)
		{
			JSONObject serviceTagObj=(JSONObject)serviceTagsJsonArray.get(i);
			if(serviceId.equals(serviceTagObj.getString("service_id")))
			{
				tagIdArray=serviceTagObj.getJSONArray("tag_ids");
				break;
			}
		}
		List<Integer> remove_tagsList=new ArrayList<Integer>();
		if(tagIdArray != null){
			Reporter.log("service tags array.."+tagIdArray.length(),true);
		if(tagIdArray.length()>1)
		{
			for(int i=0; i<tagIdArray.length(); i++)
			{
				remove_tagsList.add(new Integer((Integer)tagIdArray.get(i)));
			}
				
		}
		}
		remove_tagsList.add(0);
		remove_tagsList.add(1);
		remove_tagsList.add(2);
		
		//{service_ids: ["2242"], add_tags: [], remove_tags: [0, 1, 2, 8]}
		JSONObject removeTagObj=new JSONObject();
		removeTagObj.put("service_ids", new String[]{serviceId});
		Integer[] add_tags1={};
		removeTagObj.put("add_tags", add_tags1);
		Integer[] remove_tags = remove_tagsList.toArray(new Integer[remove_tagsList.size()]);
		removeTagObj.put("remove_tags", remove_tags);
		
		Reporter.log("assigntagtoservice payload:.."+removeTagObj,true);
		HttpResponse removeTagToService=AuditFunctions.changeTagToService(restClient,new StringEntity(removeTagObj.toString()));
		
		
	}
	
	@Test(priority=5,dataProvider="defaultTags")	
	public void testAssinDefaultTagsToMultipleServices(Integer[] addTags, Integer[] removeTags) throws Exception
		{
			 resetServiceTags(serviceId);
			 resetServiceTags(serviceId2);
			 
			 //Assign default tags to service
			 Reporter.log("Assigning Default Tags to Service:",true);
			 JSONObject changeServiceTagPayload=new JSONObject();
			 changeServiceTagPayload.put("service_ids", new String[]{serviceId,serviceId2});
				changeServiceTagPayload.put("add_tags", addTags);
				changeServiceTagPayload.put("remove_tags", removeTags);
				
				Reporter.log("assingDefaultTagsToService payload:.."+changeServiceTagPayload,true);
				HttpResponse removeTagToService=AuditFunctions.changeTagToService(restClient,new StringEntity(changeServiceTagPayload.toString()));
			 
			
		}
		
	
	@Test(priority=6)
	public void testDeleteAllTagsCreatedFromTestClass() throws Exception{
		HttpResponse getTagsList=AuditFunctions.listAllTags(restClient,null);
		Assert.assertEquals(getTagsList.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response Code should be 404");
       JSONObject tagsListObj = new JSONObject(ClientUtil.getResponseBody(getTagsList));
		
		JSONArray tagsListArray=tagsListObj.getJSONArray("objects");
		String tagObjectId=null;
		HttpResponse deleteTagResponse=null;
		for(int i=0; i<tagsListArray.length(); i++)
		{
			JSONObject tagObj=(JSONObject)tagsListArray.get(i);
			String tagName=tagObj.getString("tag_name");
			
			if( !(defaultTagsList.contains(tagName)))
			{
				//delete all tags except system tags
				tagObjectId=tagObj.getString("id");
				
				deleteTagResponse=AuditFunctions.deleteTag(restClient,tagObjectId);
				Assert.assertEquals(deleteTagResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT,"No Content & Response code should be 204");
				
				//try to get the deleted tag
				//deleteTagResponse=AuditFunctions.getTag(restClient,tagObjectId);
				//Assert.assertEquals(deleteTagResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND,"Response Code should be 404");
				
			}
			
		}
	
	}

	//comments  tests
	
	protected String getStringWithLengthAndFilledWithCharacter(int length, char charToFill) {
	    char[] array = new char[length];
	    int pos = 0;
	    while (pos < length) {
	        array[pos] = charToFill;
	        pos++;
	    }
	    return new String(array);
	}
	
	//comments  tests
	@Test
	public void testAddCommentsTotheService() throws Exception
	{
		HttpResponse commentsResp;
		JSONObject commentObject=null;
	  	CommentPayload cPayload=populateCommentPayload(serviceId,"comment1");
	    commentsResp=AuditFunctions.addCommentToService(restClient, new StringEntity(constructCommentPayload(cPayload).toString()));
	    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"Response code should be ");
	    JSONObject commentsRespObj = new JSONObject(ClientUtil.getResponseBody(commentsResp));
	    Reporter.log("commentsRespObj..."+commentsRespObj,true);
	    
	    commentsResp=AuditFunctions.getComment(restClient, getCommentRequestParams(cPayload));
	    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Get Comment Response code should be ");
	    String str=ClientUtil.getResponseBody(commentsResp);
	    Reporter.log("str..."+str,true);
	    commentObject = (JSONObject) new JSONObject(str).getJSONArray("objects").getJSONObject(0);
        validateCreatedComment(commentObject, cPayload);
	    	
	}
	
	@Test
	public void testAddCommentsToMaxLength() throws Exception
	{
		String comment=getStringWithLengthAndFilledWithCharacter(5,'T');
		Reporter.log("comment:"+comment,true);
		HttpResponse commentsResp;
		JSONObject commentObject=null;
		CommentPayload cPayload=populateCommentPayload(serviceId,comment);
		  commentsResp=AuditFunctions.addCommentToService(restClient, new StringEntity(constructCommentPayload(cPayload).toString()));
		    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"Response code should be ");
		    JSONObject commentsRespObj = new JSONObject(ClientUtil.getResponseBody(commentsResp));
		    Reporter.log("commentsRespObj..."+commentsRespObj,true);
		    
		    commentsResp=AuditFunctions.getComment(restClient, getCommentRequestParams(cPayload));
		    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Get Comment Response code should be ");
		    String str=ClientUtil.getResponseBody(commentsResp);
		    Reporter.log("str..."+str,true);
		    commentObject = (JSONObject) new JSONObject(str).getJSONArray("objects").getJSONObject(0);
	        validateCreatedComment(commentObject, cPayload);
		
		
		
	}
	@Test
	public void testEditCommentsTotheService() throws Exception
	{
		HttpResponse commentsResp;
		JSONObject commentObject=null;
		String commentstr="testcomment";
	  	
		//Create comment
		CommentPayload cPayload=populateCommentPayload(serviceId,commentstr);
	  	commentsResp=AuditFunctions.addCommentToService(restClient, new StringEntity(constructCommentPayload(cPayload).toString()));
	    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"Response code should be ");
	    JSONObject commentsRespObj = new JSONObject(ClientUtil.getResponseBody(commentsResp));
	    
	  	commentsResp=AuditFunctions.getComment(restClient, getCommentRequestParams(cPayload));
		Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Get Comment Response code should be ");
		String addedCommentResponse=ClientUtil.getResponseBody(commentsResp);
		
		commentObject = (JSONObject) new JSONObject(addedCommentResponse).getJSONArray("objects").getJSONObject(0);
		Reporter.log("Added Comment response..."+commentObject,true);
		
		//Edit the comment
		commentObject.put("ifModified", "true");
		commentObject.put("isEditable", "true");
		commentObject.put("modified_on",commentObject.get("created_on") );
		commentObject.put("on_id", serviceId);
		commentObject.put("modified_by", suiteData.getUsername());
		commentObject.put("comment", commentObject.get("comment")+"_updated");
		Reporter.log("edited comment request payload:.."+commentObject,true);
		
		commentsResp=AuditFunctions.addCommentToService(restClient, new StringEntity(commentObject.toString()));
		Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"Response code should be ");
		CommentPayload cPayload1=populateCommentPayload(serviceId,commentstr+"_updated");
		commentsResp=AuditFunctions.getComment(restClient, getCommentRequestParams(cPayload1));
		Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Get Comment Response code should be ");
		String editedCommentResponse=ClientUtil.getResponseBody(commentsResp);
		Reporter.log("editedCommentResponse.complete.."+editedCommentResponse,true);
		commentObject = (JSONObject) new JSONObject(editedCommentResponse).getJSONArray("objects").getJSONObject(0);
		Reporter.log("edited Comment response..."+commentObject,true);
		
		//validate edited comment
		SoftAssert softAssert=new SoftAssert();
	    softAssert.assertNotNull(commentObject.get("id"), "commentId is null");
	    softAssert.assertNotNull(commentObject.get("created_by"), "created_by is null");
	    softAssert.assertEquals(commentObject.get("created_by"), suiteData.getUsername(), "created_by should be");
	    softAssert.assertNotNull(commentObject.get("modified_by"), "modified_by is null");
	    softAssert.assertEquals(commentObject.get("modified_by"), suiteData.getUsername(), "modified_by should be");
	    softAssert.assertNotNull(commentObject.get("modified_on"), "modified_on is null");
	    softAssert.assertNotNull(commentObject.get("on_id"), "modified_on is null");
	    softAssert.assertEquals(commentObject.get("on_id"), cPayload1.getCommmentFieldId(), "on_id should be");
		softAssert.assertNotNull(commentObject.get("app"), "app is null");
	    softAssert.assertEquals(commentObject.get("app"), cPayload1.getCommentApp(), "app should be");
	    softAssert.assertNotNull(commentObject.get("component"), "component is null");
	    softAssert.assertEquals(commentObject.get("component"), cPayload1.getCommentComponent(), "app should be");
	    softAssert.assertNotNull(commentObject.get("created_on"), "created_on is null");
	    softAssert.assertNotNull(commentObject.get("resource_uri"), "resource_uri is null");
	    softAssert.assertNotNull(commentObject.get("comment"), "comment is null");
	    softAssert.assertEquals(commentObject.get("comment"), cPayload1.getCommentStr(), "comment should be");	
	    
	    softAssert.assertAll();
	    
		
	}
	private List<NameValuePair> getCommentRequestParams(CommentPayload cPayload)
	{
	    List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		queryParam.add(new BasicNameValuePair("app", cPayload.getCommentApp()));
		queryParam.add(new BasicNameValuePair("component", cPayload.getCommentComponent()));
		queryParam.add(new BasicNameValuePair("fieldId", cPayload.getCommmentFieldId()));
		queryParam.add(new BasicNameValuePair("limit", "0"));
		return queryParam;
	}
	//@Test
	public void testDeleteCommentsTotheService() throws Exception
	{
		//create comment
		HttpResponse commentsResp;
		JSONObject commentObject=null;
		
		CommentPayload cPayload=populateCommentPayload(serviceId,"comment1");
	    commentsResp=AuditFunctions.addCommentToService(restClient, new StringEntity(constructCommentPayload(cPayload).toString()));
	    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"Create Comment Response code should be ");
	    JSONObject commentsRespObj = new JSONObject(ClientUtil.getResponseBody(commentsResp));
	    validateCreatedComment(commentsRespObj, cPayload);
		
		//get comment 
		commentsResp=AuditFunctions.getComment(restClient, getCommentRequestParams(cPayload));
	    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Get Comment Response code should be ");
	     
	    commentObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(commentsResp)).getJSONArray("comments").getJSONObject(0);

	    
	    //edit comment
	   String commentId=commentObject.getString("id");
	    commentObject.put("comment", commentObject.getString("comment")+"-"+"updated");
	   /* commentObject.put("on_id", serviceId);
	    commentObject.put("modified_by", suiteData.getUsername());
	    commentObject.put("modified_on", commentObject.getString("created_on"));
	    commentObject.put("isEditable", "true");
	    commentObject.put("ifModified", "true");
	    JSONObject json = new JSONObject();
		json.put("comment", commentObject);
		Reporter.log("updated Comment.."+json,true);*/
	    Reporter.log("updated Comment.."+commentObject,true);
		    
		  
	    commentsResp=AuditFunctions.editOrDeleteCommentToService(restClient, new StringEntity(commentObject.toString()),commentId);
	    Assert.assertEquals(commentsResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Edit Comment Response code should be ");
	    JSONObject editCommentResp = new JSONObject(ClientUtil.getResponseBody(commentsResp));
	    Reporter.log("editCommentResp.."+editCommentResp,true);
	    
	    
	    
	    //delete comment
		
		
	}
	
	private CommentPayload populateCommentPayload(String serviceId,String commentMsg)
	{
		CommentPayload cPayload=new CommentPayload();
		cPayload.setCommentStr(commentMsg);
		cPayload.setCommentApp("Audit");
		cPayload.setCommentComponent("Service");
		cPayload.setCommmentFieldId(serviceId);
		return cPayload;
	}
	private void validateCreatedComment(JSONObject commentsRespObj,CommentPayload cPayload ) throws Exception
	{
		SoftAssert softAssert=new SoftAssert();
	    softAssert.assertNotNull(commentsRespObj.get("id"), "commentId is null");
	    softAssert.assertNotNull(commentsRespObj.get("created_by"), "created_by is null");
	    softAssert.assertEquals(commentsRespObj.get("created_by"), suiteData.getUsername(), "created_by should be");
	    softAssert.assertNotNull(commentsRespObj.get("app"), "app is null");
	    softAssert.assertEquals(commentsRespObj.get("app"), cPayload.getCommentApp(), "app should be");
	    softAssert.assertNotNull(commentsRespObj.get("component"), "component is null");
	    softAssert.assertEquals(commentsRespObj.get("component"), cPayload.getCommentComponent(), "app should be");
	    softAssert.assertNotNull(commentsRespObj.get("created_on"), "created_on is null");
	    softAssert.assertNotNull(commentsRespObj.get("resource_uri"), "resource_uri is null");
	    softAssert.assertNotNull(commentsRespObj.get("on_id"), "on_id is null");
	    softAssert.assertEquals(commentsRespObj.get("on_id"), cPayload.getCommmentFieldId(), "on_id should be");
	    softAssert.assertNotNull(commentsRespObj.get("comment"), "comment is null");
	    softAssert.assertEquals(commentsRespObj.get("comment"), cPayload.getCommentStr(), "comment should be");	
	    
	    softAssert.assertAll();
		
	}
	
	public JSONObject constructCommentPayload(CommentPayload cPayload) throws Exception
	{
		JSONObject commentsPayload = new JSONObject();
	    commentsPayload.put("app", cPayload.getCommentApp());
	    commentsPayload.put("component", cPayload.getCommentComponent());
	    commentsPayload.put("fieldId", new Integer(cPayload.getCommmentFieldId()));
	    commentsPayload.put("comment", cPayload.getCommentStr());
	    Reporter.log("comments payload.."+commentsPayload,true);
	    return commentsPayload;
		
	}

	private JSONObject createTag() throws Exception
	{
		//create a tag
				JSONObject colorpattern = new JSONObject();
				colorpattern.put("color", "1");
				colorpattern.put("pattern", "1");
				
				JSONObject tagPayload = new JSONObject();
				tagPayload.put("tag_name", "tag_dummy_forEdit");
				tagPayload.put("attributes", colorpattern);
				
				Reporter.log("tagPayload::"+tagPayload,true);
			
				
				HttpResponse  createTagResp=AuditFunctions.createTag(restClient, new StringEntity(tagPayload.toString()));
				Assert.assertEquals(createTagResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"Response Code should be 200");
				JSONObject strTagResp = new JSONObject(ClientUtil.getResponseBody(createTagResp));
				Assert.assertNotNull(strTagResp,"Create Tag response null");
				Reporter.log("strTagResp...: "+strTagResp,true);
				
				return strTagResp;
				
	}

}
