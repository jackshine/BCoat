package com.elastica.beatle.securlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import com.elastica.beatle.logger.Logger;



public class ESQueryBuilder {
//  For nested searches, we need to refactor the code	
//	XContentBuilder xb =  XContentFactory.jsonBuilder().startObject();
//
//	xb.startArray("eventnested");
//	for(int j=0;j<2;j++) {
//	    xb.startObject();
//	    xb.field("event_type", eventType);
//	    xb.field("event_attribute_instance", eventInstance);
//	    xb.startArray("attributes");
//	    for(int i=0;i<2;i++) {
//	        xb.startObject();
//	        xb.field("event_attribute_name", attrName);
//	        xb.field("event_attribute_value", attrValue);
//	        xb.endObject();
//	    }
//	    xb.endArray();
//	    xb.endObject();
//	}
//	xb.endArray();
//	
	
	public String getSearchQuery(String tsfrom, String tsto, String facility, String useremail, String objectType, String location) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				  startObject().
				    startObject("source").
				      startObject("query").
				      	startObject("filtered").
				      	  startObject("query").
				      		startObject("bool").
				      		   startArray("must").
				      		     startObject().
					      		     startObject("range").
					      		       startObject("created_timestamp").
					      		         field("from", tsfrom).
					      		         field("to", tsto).
					      		       endObject().
					      		     endObject().
					      		 endObject().
				      		     startObject().
					      		     startObject("term"). 
					      		        field("facility", facility).
					      		     endObject().
				      		     endObject().
				      		     startObject().
					      		     startObject("term"). 
				      		            field("user", useremail).
				      		         endObject().
				      		     endObject().
				      		     startObject().
				      		         startObject("term"). 
			      		                field("Object_type", objectType).
			      		             endObject().
			      		         endObject().
			      		         startObject().
				      		         startObject("term"). 
			      		                field("location", location).
			      		             endObject().
		      		             endObject().
		      		            endArray().
		      		         endObject().
		      		       endObject().
		      		       startObject("filter").
		      		       endObject().
		      		     endObject().
		      		   endObject().
		      		   startObject("sort").
		      		     startObject("created_timestamp").
		      		      field("order", "desc").
		      		      field("ignore_unmapped", "true").
		      		     endObject().
		      		   endObject().
		      		  endObject();
		//System.out.println(builder.string());
		return builder.string();
	}
	
	public String getSearchQuery(String tsfrom, String tsto, String facility, String query, String user) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				  startObject().
				    startObject("source").
				      startObject("query").
				      	startObject("filtered").
				      	  startObject("query").
				      		startObject("bool").
				      		   startArray("must").
				      		     startObject().
					      		     startObject("range").
					      		       startObject("created_timestamp").
					      		         field("from", tsfrom).
					      		         field("to", tsto).
					      		       endObject().
					      		     endObject().
					      		 endObject().
				      		     startObject().
					      		     startObject("term"). 
					      		        field("facility", facility).
					      		     endObject().
				      		     endObject().
				      		     startObject().
				      		         startObject("term"). 
				      		            field("user", user).
				      		         endObject().
			      		         endObject().
				      		     startObject().
					      		     startObject("query_string"). 
				      		            field("query", query).
				      		            field("default_operator", "AND").
				      		            field("analyzer", "custom_search_analyzer").
				      		            field("allow_leading_wildcard", "false").
				      		         endObject().
				      		     endObject().
		      		            endArray().
		      		         endObject().
		      		       endObject().
		      		       startObject("filter").
		      		       endObject().
		      		     endObject().
		      		   endObject().
		      		   startObject("sort").
		      		     startObject("created_timestamp").
		      		      field("order", "desc").
		      		      field("ignore_unmapped", "true").
		      		     endObject().
		      		   endObject().
		      		  endObject();
		//System.out.println(builder.string());
		return builder.string();
	}
	
	
	public String getTimeBoundSearchQuery(String tsfrom, String tsto, String facility, String user, String objectType) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				  startObject().
				    startObject("source").
				      startObject("query").
				      	startObject("filtered").
				      	  startObject("query").
				      		startObject("bool").
				      		   startArray("must").
				      		     startObject().
					      		     startObject("range").
					      		       startObject("created_timestamp").
					      		         field("from", tsfrom).
					      		         field("to", tsto).
					      		       endObject().
					      		     endObject().
					      		 endObject().
				      		     startObject().
					      		     startObject("term"). 
					      		        field("facility", facility).
					      		     endObject().
				      		     endObject().
				      		     startObject().
				      		         startObject("term"). 
				      		            field("user", user).
				      		         endObject().
			      		         endObject().
			      		         startObject().
				      		         startObject("term"). 
				      		            field("Object_type", objectType).
				      		         endObject().
			      		         endObject().
		      		            endArray().
		      		         endObject().
		      		       endObject().
		      		       startObject("filter").
		      		       endObject().
		      		     endObject().
		      		   endObject().
		      		   startObject("sort").
		      		     startObject("created_timestamp").
		      		      field("order", "desc").
		      		      field("ignore_unmapped", "true").
		      		     endObject().
		      		   endObject().
		      		  endObject();
		//System.out.println(builder.string());
		return builder.string();
	}
	
	public String getSearchQueryForDisplayLogsOffice365(String tsfrom, String tsto, String facility,String termType, String termValue, String query, 
			String user, String apiServerUrl, String csrfToken, String sessionId, String searchForUser) throws Exception {
		
		if(searchForUser.equals("self")){
			searchForUser = user;
		}
		
		XContentBuilder builder = null;
		if (termType.equals("all_messages"))
		{
			builder = XContentFactory.jsonBuilder().
					startObject().
					startObject("source").
					startObject("query").
					startObject("filtered").
					startObject("query").
						startObject("bool").
						   startArray("must").
							   startObject().
				      		     startObject("range").
				      		       startObject("created_timestamp").
				      		         field("from", tsfrom).
				      		         field("to", tsto).
				      		       endObject().
				      		     endObject().
				      		 endObject().
						     startObject().
							     startObject("term"). 
							        field("facility", facility).
							     endObject().
						     endObject().				      		     
							 startObject().
							     startObject("term"). 
							        field("__source", "API").
							     endObject().
						     endObject().
//							 startObject().
//							     startObject("term"). 
//							        field("Object_type", termValue). //"Email_Message").
//							     endObject().
//						     endObject().
						     startObject().
							     startObject("query_string"). 
						            field("query", query).
						            field("default_operator", "AND").
						            field("analyzer", "custom_search_analyzer").
						            field("allow_leading_wildcard", "false").
						         endObject().
						     endObject().
						   
					      endArray().
					   endObject().
					 endObject().
					 startObject("filter").
					 endObject().
					endObject().
					endObject().
					startObject("sort").
					startObject("created_timestamp").
					field("order", "desc").
					field("ignore_unmapped", "true").
					endObject().
					endObject().
					endObject().
					field("sourceName", facility).
					field("apiServerUrl", apiServerUrl).
					field("csrftoken", csrfToken).
					field("sessionid", sessionId).
					field("userid", user);
		}
		else if (termType.equals("Object_type"))
		{
			builder = XContentFactory.jsonBuilder().
			startObject().
			startObject("source").
			startObject("query").
			startObject("filtered").
			startObject("query").
				startObject("bool").
				   startArray("must").
					   startObject().
		      		     startObject("range").
		      		       startObject("created_timestamp").
		      		         field("from", tsfrom).
		      		         field("to", tsto).
		      		       endObject().
		      		     endObject().
		      		 endObject();
				if (searchForUser!=""){
				   	builder.startObject().
					     startObject("term"). 
					        field("user", searchForUser).
					     endObject().
				     endObject();
				}
				     builder.startObject().
					     startObject("term"). 
					        field("facility", facility).
					     endObject().
				     endObject().				      		     
					 startObject().
					     startObject("term"). 
					        field("__source", "API").
					     endObject().
				     endObject().
					 startObject().
					     startObject("term"). 
					        field("Object_type", termValue). //"Email_Message").
					     endObject().
				     endObject().
				     startObject().
					     startObject("query_string"). 
				            field("query", query).
				            field("default_operator", "AND").
				            field("analyzer", "custom_search_analyzer").
				            field("allow_leading_wildcard", "false").
				         endObject().
				     endObject().
				   
			      endArray().
			   endObject().
			 endObject().
			 startObject("filter").
			 endObject().
			endObject().
			endObject().
			startObject("sort").
			startObject("created_timestamp").
			field("order", "desc").
			field("ignore_unmapped", "true").
			endObject().
			endObject().
			endObject().
			field("sourceName", facility).
			field("apiServerUrl", apiServerUrl).
			field("csrftoken", csrfToken).
			field("sessionid", sessionId).
			field("userid", user);
		}
		else if (termType.equals("Activity_type"))
		{
			builder = XContentFactory.jsonBuilder().
			startObject().
			startObject("source").
			startObject("query").
			startObject("filtered").
			startObject("query").
				startObject("bool").
				   startArray("must").

				   startObject().
	      		     startObject("range").
	      		       startObject("created_timestamp").
	      		         field("from", tsfrom).
	      		         field("to", tsto).
	      		       endObject().
	      		     endObject().
	      		 endObject();
	      		if (searchForUser!=""){
				   	builder.startObject().
					     startObject("term"). 
					        field("user", searchForUser).
					     endObject().
				     endObject();
				}
				     builder.
				     startObject().
					     startObject("term"). 
					        field("facility", facility).
					     endObject().
				     endObject().				      		     
					 startObject().
					     startObject("term"). 
					        field("__source", "API").
					     endObject().
				     endObject().					
				     startObject().
					     startObject("term"). 
					        field("Activity_type",termValue ).// "Email_Sent").
					     endObject().
				     endObject().
				     startObject().
					     startObject("query_string"). 
				            field("query", query).
				            field("default_operator", "AND").
				            field("analyzer", "custom_search_analyzer").
				            field("allow_leading_wildcard", "false").
				         endObject().
				     endObject().
				   
			      endArray().
			   endObject().
			 endObject().
			 startObject("filter").
			 endObject().
			endObject().
			endObject().
			startObject("sort").
			startObject("created_timestamp").
			field("order", "desc").
			field("ignore_unmapped", "true").
			endObject().
			endObject().
			endObject().
			field("sourceName", facility).
			field("apiServerUrl", apiServerUrl).
			field("csrftoken", csrfToken).
			field("sessionid", sessionId).
			field("userid", user);
		}
		else if (termType.equals("All"))
		{
			builder = XContentFactory.jsonBuilder().
			startObject().
			startObject("source").
			startObject("query").
			startObject("filtered").
			startObject("query").
				startObject("bool").
				   startArray("must").
				     
				   startObject().
	      		     startObject("range").
	      		       startObject("created_timestamp").
	      		         field("from", tsfrom).
	      		         field("to", tsto).
	      		       endObject().
	      		     endObject().
	      		 endObject();
	      		if (searchForUser!=""){
				   	builder.startObject().
					     startObject("term"). 
					        field("user", searchForUser).
					     endObject().
				     endObject();
				}
				     builder.
			     	startObject().
					     startObject("term"). 
					        field("facility", facility).
					     endObject().
				     endObject().				      		     
					 startObject().
					     startObject("term"). 
					        field("__source", "API").
					     endObject().
				     endObject().
				     startObject().
					     startObject("query_string"). 
				            field("query", query).
				            field("default_operator", "AND").
				            field("analyzer", "custom_search_analyzer").
				            field("allow_leading_wildcard", "false").
				         endObject().
				     endObject().
				   
			      endArray().
			   endObject().
			 endObject().
			 startObject("filter").
			 endObject().
			endObject().
			endObject().
			startObject("sort").
			startObject("created_timestamp").
			field("order", "desc").
			field("ignore_unmapped", "true").
			endObject().
			endObject().
			endObject().
			field("sourceName", facility).
			field("apiServerUrl", apiServerUrl).
			field("csrftoken", csrfToken).
			field("sessionid", sessionId).
			field("userid", user);
			
		}
		

		System.out.println(builder.string());
		return builder.string();
		//return qstring;
}
	
	
	
	
	
	public String getInvestigateQueryWithFacets(String tsfrom, String tsto, String facility, HashMap<String, String> terms, ArrayList<String> missing,
			HashMap<String, String> facets, String histoInterval, String timezone, HashMap<String, String> filters, String user, String apiServerUrl, String csrfToken, String sessionId, long from, long size, String source) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				startObject("query").
					startObject("filtered").
				  	  startObject("query").
				      		startObject("bool").
					      		   startArray("must").
								      	startObject().
							      		     startObject("range").
							      		       startObject("created_timestamp").
							      		         field("from", tsfrom).
							      		         field("to", tsto).
							      		       endObject().
							      		     endObject().
							      		 endObject();
										for (Map.Entry<String, String> entry : terms.entrySet()) {
											  builder.startObject().
								      		     startObject("term"). 
								      		        field(entry.getKey(), entry.getValue()).
								      		     endObject().
							      		     endObject();
										}
										
										builder.   
				  		            endArray().
				  		          startArray("must_not").
				  		           startObject().
				  		          	startObject("term").
				  		          		field("facility", facility).
				  		          	endObject().
				  		          endObject().
				  		          endArray().
						         endObject().
					       endObject().
					       startObject("filter");
								if(missing.size() > 0) {
									builder.startArray("and");	
										for (String field : missing) {
											builder.startObject().
												startObject("missing"). 
							      		        	field("field", field).
							      		        endObject().
							      		    endObject();
										}		
									builder.endArray();
								}
	  		          	   builder.endObject().
				   endObject().
				endObject().
				field("from", from).
				field("size", size).
				startObject("sort").
				  startObject("created_timestamp").
					  field("order", "desc").
					  field("ignore_unmapped", "true").
				  endObject().
				endObject();
										
										
												
				builder.startObject("facets");
					for (Map.Entry<String, String> entry : facets.entrySet()) {	
						builder.startObject(entry.getKey()).
						      startObject("date_histogram").
							    field("field",    "created_timestamp").
							    field("interval", histoInterval).
							    field("time_zone", timezone).
							    field("post_zone", timezone).
						      endObject().
							  startObject("facet_filter").
						        startObject("term").
						           field("severity",    entry.getValue()). 
						        endObject().
					          endObject().
						endObject();
					}
					
					for (Map.Entry<String, String> entry : filters.entrySet()) {	
						builder.startObject(entry.getKey()).
						      startObject("terms").
							    field("field",    entry.getKey()).
							    field("size", entry.getValue()).
						      endObject().
						endObject();
					}
					
				builder.endObject().
				
				
				endObject().
				field("sourceName", source).
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", user);
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}
	
	
	
	public String getInvestigateQueryWithFacets(String tsfrom, String tsto, String facility, HashMap<String, String> terms, ArrayList<String> missing,
			HashMap<String, String> facets, String histoInterval, String timezone, HashMap<String, String> filters, String user, String apiServerUrl, String csrfToken, String sessionId, 
			long from, long size, String source, String startDate, String endDate, String format) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				startObject("query").
					startObject("filtered").
				  	  startObject("query").
				      		startObject("bool").
					      		   startArray("must").
								      	startObject().
							      		     startObject("range").
							      		       startObject("created_timestamp").
							      		         field("from", tsfrom).
							      		         field("to", tsto).
							      		       endObject().
							      		     endObject().
							      		 endObject();
										for (Map.Entry<String, String> entry : terms.entrySet()) {
											  builder.startObject().
								      		     startObject("term"). 
								      		        field(entry.getKey(), entry.getValue()).
								      		     endObject().
							      		     endObject();
										}
										
										builder.   
				  		            endArray().
				  		          startArray("must_not").
				  		           startObject().
				  		          	startObject("term").
				  		          		field("facility", facility).
				  		          	endObject().
				  		          endObject().
				  		          endArray().
						         endObject().
					       endObject().
					       startObject("filter");
								if(missing.size() > 0) {
									builder.startArray("and");	
										for (String field : missing) {
											builder.startObject().
												startObject("missing"). 
							      		        	field("field", field).
							      		        endObject().
							      		    endObject();
										}		
									builder.endArray();
								}
	  		          	   builder.endObject().
				   endObject().
				endObject().
				field("from", from).
				field("size", size).
				startObject("sort").
				  startObject("created_timestamp").
					  field("order", "desc").
					  field("ignore_unmapped", "true").
				  endObject().
				endObject();
										
										
												
				builder.startObject("facets");
					for (Map.Entry<String, String> entry : facets.entrySet()) {	
						builder.startObject(entry.getKey()).
						      startObject("date_histogram").
							    field("field",    "created_timestamp").
							    field("interval", histoInterval).
							    field("time_zone", timezone).
							    field("post_zone", timezone).
						      endObject().
							  startObject("facet_filter").
						        startObject("term").
						           field("severity",    entry.getValue()). 
						        endObject().
					          endObject().
						endObject();
					}
					
					for (Map.Entry<String, String> entry : filters.entrySet()) {	
						builder.startObject(entry.getKey()).
						      startObject("terms").
							    field("field",    entry.getKey()).
							    field("size", entry.getValue()).
						      endObject().
						endObject();
					}
					
				builder.endObject().
				
				
				endObject().
				field("startDate", startDate).
				field("endDate", endDate).
				field("format", format).
				field("sourceName", source).
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", user);
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}
	
	
	
	public String getAuditQuery(HashMap<String, String> reportconfigParams, ArrayList<Reportfilter> andfilters, HashMap<String, String> cloudSocParams) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		if(reportconfigParams.size() > 0) {
			builder.startObject("reportconfig");
			builder.startObject("filters");
			if (andfilters.size() > 0) {
				builder.startArray("AND");

				for (Reportfilter filter : andfilters) {
					builder.startObject();

					builder.field("field", filter.getField())
					.field("type", filter.getType())
					.field("op", filter.getOp())
					.field("value", filter.getValue());

					builder.endObject();

				}
				builder.endArray();

			}

			for (Map.Entry<String, String> entry : reportconfigParams.entrySet()) {
				builder.field(entry.getKey(), entry.getValue());     
			}

			builder.endObject();
			builder.endObject();
		}
		for (Map.Entry<String, String> entry : cloudSocParams.entrySet()) {
			builder.field(entry.getKey(), entry.getValue());     
		}

		builder.endObject();

		System.out.println(builder.string());
		return builder.string();
		//return qstring;
	}
	
	
	
	public String getESQuery(String tsfrom, String tsto, String facility, String objectType, 
			String user, String apiServerUrl, String csrfToken, String sessionId, int from, int size) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				startObject("query").
					startObject("filtered").
				  	  startObject("query").
				      		startObject("bool").
					      		   startArray("must").
								      	startObject().
							      		     startObject("range").
							      		       startObject("created_timestamp").
							      		         field("from", tsfrom).
							      		         field("to", tsto).
							      		       endObject().
							      		     endObject().
							      		 endObject().
						      		     startObject().
							      		     startObject("term"). 
							      		        field("facility", facility).
							      		     endObject().
						      		     endObject().
						      		     startObject().
						      		         startObject("term"). 
						      		            field("__source", "API").
						      		         endObject().
					      		         endObject().
					      		         startObject().
					      		         	startObject("term"). 
					      		         		field("Object_type", objectType).
					      		         	endObject().
					      		         endObject().
					      		       
							      		 /*startObject().
							      		     startObject("query_string"). 
						      		            field("query", query).
						      		            field("default_operator", "AND").
						      		            field("analyzer", "custom_search_analyzer").
						      		            field("allow_leading_wildcard", "false").
						      		         endObject().
						      		     endObject().*/
				  		            endArray().
						         endObject().
					       endObject().
				   endObject().
				endObject().
				field("from", from).
				field("size", size).
				endObject().
				field("sourceName", facility).
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", user);
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}
	
	
	public String getESQuery(String tsfrom, String tsto, String facility, HashMap<String, String> terms, 
			String user, String apiServerUrl, String csrfToken, String sessionId, int from, int size, String sourceName) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				startObject("query").
					startObject("filtered").
				  	  startObject("query").
				      		startObject("bool").
					      		   startArray("must").
								      	startObject().
							      		     startObject("range").
							      		       startObject("created_timestamp").
							      		         field("from", tsfrom).
							      		         field("to", tsto).
							      		       endObject().
							      		     endObject().
							      		 endObject();
										if (terms.containsKey("query")){
												builder.startObject().
											     startObject("query_string"). 
										            field("query", terms.get("query")).
										            field("default_operator", "AND").
										            field("analyzer", "custom_search_analyzer").
										            field("allow_leading_wildcard", "false").
										         endObject().
										     endObject();
											//after adding query section, remove it so that it wont be added to terms section
											terms.remove("query");
										}
										for (Map.Entry<String, String> entry : terms.entrySet()) {
											  builder.startObject().
								      		     startObject("term"). 
								      		        field(entry.getKey(), entry.getValue()).
								      		     endObject().
							      		     endObject();
										}
										
										builder.   
				  		            endArray().
						         endObject().
					       endObject().
				   endObject().
				endObject().
				field("from", from).
				field("size", size).
				endObject().
				field("sourceName", sourceName).
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", user);
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}
	
	
	public String getESQuery(String tsfrom, String tsto, String facility, HashMap<String, String> terms, 
			String user, String apiServerUrl, String csrfToken, String sessionId, int from, int size, String sourceName, String format, String startDate, String endDate) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				startObject("query").
					startObject("filtered").
				  	  startObject("query").
				      		startObject("bool").
					      		   startArray("must").
								      	startObject().
							      		     startObject("range").
							      		       startObject("created_timestamp").
							      		         field("from", tsfrom).
							      		         field("to", tsto).
							      		       endObject().
							      		     endObject().
							      		 endObject();
										for (Map.Entry<String, String> entry : terms.entrySet()) {
											  builder.startObject().
								      		     startObject("term"). 
								      		        field(entry.getKey(), entry.getValue()).
								      		     endObject().
							      		     endObject();
										}
										builder.   
				  		           endArray().
				  		           startArray("must_not").
							      	 startObject().
						      		     startObject("term").
						      		         field("facility", "Elastica").
						      		     endObject().
						      		 endObject().
								   endArray().
						         endObject().
					       endObject().
				   endObject().
				endObject().
				field("from", from).
				field("size", size).
				endObject().
				field("startDate", startDate).
				field("endDate", endDate).
				field("format", format).
				field("app", facility).
				field("sourceName", sourceName).
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", user);
										
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}
	
	
	public String getESQueryForInvestigate(String tsfrom, String tsto, String facility, HashMap<String, String> terms, 
			String user, String apiServerUrl, String csrfToken, String sessionId, int from, int size) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				startObject("query").
					startObject("filtered").
				  	  startObject("query").
				      		startObject("bool").
					      		   startArray("must").
								      	startObject().
							      		     startObject("range").
							      		       startObject("created_timestamp").
							      		         field("from", tsfrom).
							      		         field("to", tsto).
							      		       endObject().
							      		     endObject().
							      		 endObject();
										for (Map.Entry<String, String> entry : terms.entrySet()) {
											  builder.startObject().
								      		     startObject("term"). 
								      		        field(entry.getKey(), entry.getValue()).
								      		     endObject().
							      		     endObject();
										}
										builder.   
				  		            endArray().
						         endObject().
					       endObject().
				   endObject().
				endObject().
				field("from", from).
				field("size", size).
				endObject().
				field("sourceName", "investigate").
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", user);
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}
	
	
	public String getESQueryForActivityType(String tsfrom, String tsto, String facility, String activityType, 
			String user, String apiServerUrl, String csrfToken, String sessionId, int from, int size) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				startObject("query").
					startObject("filtered").
				  	  startObject("query").
				      		startObject("bool").
					      		   startArray("must").
								      	startObject().
							      		     startObject("range").
							      		       startObject("created_timestamp").
							      		         field("from", tsfrom).
							      		         field("to", tsto).
							      		       endObject().
							      		     endObject().
							      		 endObject().
						      		     startObject().
							      		     startObject("term"). 
							      		        field("facility", facility).
							      		     endObject().
						      		     endObject().
						      		     startObject().
						      		         startObject("term"). 
						      		            field("__source", "API").
						      		         endObject().
					      		         endObject().
					      		         startObject().
					      		         	startObject("term"). 
					      		         		field("Activity_type", activityType).
					      		         	endObject().
					      		         endObject().
					      		       
							      		 /*startObject().
							      		     startObject("query_string"). 
						      		            field("query", query).
						      		            field("default_operator", "AND").
						      		            field("analyzer", "custom_search_analyzer").
						      		            field("allow_leading_wildcard", "false").
						      		         endObject().
						      		     endObject().*/
				  		            endArray().
						         endObject().
					       endObject().
				   endObject().
				endObject().
				field("from", from).
				field("size", size).
				endObject().
				field("sourceName", facility).
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", user);
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}
	
	public String getESQueryForPolicyAlert(String tsfrom, String tsto, List<String> query) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
						startObject("query").
							startObject("bool").
						      	startArray("must").
						      		startObject().
						      		   	startObject("range").
						      		   		startObject("created_timestamp").
						      		   			field("from", tsfrom).
						      		   			field("to", tsto).
						      		   		endObject().
								      	endObject().
								    endObject().
							      	startObject().
							      		startObject("term"). 
								      		field("Activity_type", "Policy Violation").
								      		endObject().
							      	endObject().
					      		    /*startObject().
					      		    	startObject("term"). 
					      		            field("severity", "critical").
					      		        endObject().
				      		        endObject().*/
				      		        startObject().
				      		         	startObject("term"). 
				      		         		field("policy_action", "ALERT").
				      		         	endObject().
						      		endObject().
									startObject().
										startObject("filtered").
											startObject("filter").
												startObject("not").
													startArray("and").
														startObject().
								      		         		startObject("term"). 
								      		         			field("severity", "informational").
								      		         		endObject().
								      		         	endObject().
								      		         	startObject().
								      		         		startObject("term"). 
								      		         			field("severity_old", "critical").
								      		         		endObject().
								      		         	endObject().
													endArray().
												endObject().
											endObject().
										endObject().
									endObject();
									for (int i=0;i<query.size();i++) {
										  builder.startObject().
							      		     startObject("query_string"). 
							      		        field("query", query.get(i).toLowerCase()).
							      		     endObject().
						      		     endObject();
									}
						      	builder.endArray().
						      	startObject("must_not").
		      		         		startObject("term"). 
		      		         			field("alert_cleared", "true").
		      		         		endObject().
		      		         	endObject().
						    endObject().
						endObject().
						startObject("filter"). 
		         		endObject().
		         		/*startObject("facets").
			         		startObject("policy_type").
				         		startObject("terms").
					         		field("field", "policy_type").
					         		field("size", 1000).
				         		endObject().
			         		endObject().
			         		startObject("user").
				         		startObject("terms").
					         		field("field", "user").
					         		field("size", 1000).
				         		endObject().
			         		endObject().
			         		startObject("_PolicyViolated").
				         		startObject("terms").
					         		field("field", "_PolicyViolated").
					         		field("size", 1000).
				         		endObject().
			         		endObject().
			         		startObject("facility").
				         		startObject("terms").
					         		field("field", "facility").
					         		field("size", 1000).
				         		endObject().
			         		endObject().
			         		startObject("severity").
				         		startObject("terms").
					         		field("field", "severity").
					         		field("size", 1000).
				         		endObject().
			         		endObject().
		         		endObject().*/
					field("from", 0).
					field("size", 160).
					startObject("sort").
      		     		startObject("created_timestamp").
      		     			field("order", "desc").
      		     		endObject().
      		     	endObject().
				endObject();
				Logger.info("Query String for Policy Alerts"+builder.toString());
				return builder.string();
	}
	
	public String getSearchQueryForDisplayLogs(String tsfrom, String tsto, String facility, String query, 
										String user, String apiServerUrl, String csrfToken, String sessionId) throws Exception {
		
		//String qstring = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+tsfrom+"\",\"to\":\""+tsto+"\"}}},{\"query_string\":{\"query\":\""+query+"\",\"default_operator\":\"AND\",\"analyzer\":\"custom_search_analyzer\",\"allow_leading_wildcard\":\"false\"}},{\"term\":{\"facility\":\""+facility+"\"}}]}}}},\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"from\":0,\"size\":30,\"facets\":{\"histoGreen\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"informational\"}}},\"histoOrange\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"warning\"}}},\"histoRed\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"critical\"}}},\"histoYellow\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"error\"}}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}},\"location\":{\"terms\":{\"field\":\"location\",\"size\":1000}},\"Object_type\":{\"terms\":{\"field\":\"Object_type\",\"size\":1000}},\"Activity_type\":{\"terms\":{\"field\":\"Activity_type\",\"size\":1000}},\"__source\":{\"terms\":{\"field\":\"__source\",\"size\":1000}},\"Resource_Id\":{\"terms\":{\"field\":\"Resource_Id\",\"size\":1000}}}},\"sourceName\":\""+facility+"\",\"apiServerUrl\":\""+apiServerUrl+"\",\"csrftoken\":\""+csrfToken +"\",\"sessionid\":\""+ sessionId +"\",\"userid\":\""+user+"\"}";
				
				
				
		
		XContentBuilder builder = XContentFactory.jsonBuilder().
				  startObject().
				    startObject("source").
				      startObject("query").
					      	startObject("filtered").
						      	  startObject("query").
							      		startObject("bool").
								      		   startArray("must").
									      		     startObject().
										      		     startObject("term"). 
										      		        field("facility", facility).
										      		     endObject().
									      		     endObject().
										      		 startObject().
										      		     startObject("query_string"). 
									      		            field("query", query).
									      		            field("default_operator", "AND").
									      		            field("analyzer", "custom_search_analyzer").
									      		            field("allow_leading_wildcard", "false").
									      		         endObject().
									      		     endObject().
						      		            endArray().
					      		         endObject().
				      		       endObject().
			      		     endObject().
		      		   endObject().
		      		   field("from", 0).
	      		       field("size", 500).
		      		  endObject().
		      		field("sourceName", facility).
	      		    field("apiServerUrl", apiServerUrl).
	      		    field("csrftoken", csrfToken).
	      		    field("sessionid", sessionId).
	      		    field("userid", user);
		System.out.println(builder.string());
		return builder.string();
	//return qstring;
	}
	
	public String getSearchQueryForCIQ(String tsfrom, String tsto, String facility, String query) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				  startObject().
				    startObject("source").
				      startObject("query").
				      	startObject("filtered").
				      	  startObject("query").
				      		startObject("bool").
				      		   startArray("must").
				      		     startObject().
					      		     startObject("range").
					      		       startObject("created_timestamp").
					      		         field("from", tsfrom).
					      		         field("to", tsto).
					      		       endObject().
					      		     endObject().
					      		 endObject().
				      		     startObject().
					      		     startObject("term"). 
					      		        field("facility", facility).
					      		     endObject().
				      		     endObject().
				      		     startObject().
				      		         startObject("term"). 
				      		            field("__source", "API").
				      		         endObject().
			      		         endObject().
			      		         startObject().
				      		         startObject("term"). 
				      		         	field("Activity_type", "Content Inspection").
				      		         endObject().
		      		             endObject().
				      		     startObject().
					      		     startObject("query_string"). 
				      		            field("query", query).
				      		            field("default_operator", "AND").
				      		            field("analyzer", "custom_search_analyzer").
				      		            field("allow_leading_wildcard", "false").
				      		         endObject().
				      		     endObject().
		      		            endArray().
		      		         endObject().
		      		       endObject().
		      		       startObject("filter").
		      		       endObject().
		      		     endObject().
		      		   endObject().
		      		   startObject("sort").
		      		     startObject("created_timestamp").
		      		      field("order", "desc").
		      		      field("ignore_unmapped", "true").
		      		     endObject().
		      		   endObject().
		      		  endObject();
		//System.out.println(builder.string());
		return builder.string();
	}
	
	public String getSearchQueryForInvestigateDisplayLogs(String tsfrom,
			String tsto, String query, String user, String apiServerUrl,
			String csrfToken, String sessionId) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().
				startObject("source").
					startObject("query").
						startObject("filtered").
							startObject("query").
								startObject("bool").
									startArray("must").
										startObject().
											startObject("range").
												startObject("created_timestamp").
													field("from", tsfrom).
													field("to", tsto).
//													field("from", "2015-08-23T07:00:00.000Z").
//													field("to", "2015-09-24T06:59:59.999Z").
												endObject().
											endObject().
										endObject().
										startObject().
											startObject("query_string").
												field("query", query).
												field("default_operator", "AND").
												field("analyzer", "custom_search_analyzer").
												field("allow_leading_wildcard", "false").
											endObject().
										endObject().
									endArray().
										startArray("must_not").
											startObject().
												startObject("term").
													field("facility", "Elastica").
												endObject().
											endObject().
										endArray().
									endObject().				
								endObject().
								startObject("filter").
								endObject().
							endObject().
						endObject().
						field("from", 0).
						field("size", 50).
						startObject("sort")
							.startObject("created_timestamp").
								field("order", "desc").
								field("ignore_unmapped", "true").
							endObject().
						endObject().
						startObject("facets").
						endObject().
					endObject().
					field("sourceName", "investigate").
					field("apiServerUrl", apiServerUrl).
					field("csrftoken", csrfToken).
					field("sessionid", sessionId).
					field("userid", user).
				endObject();										
		return builder.string();
	}
	
	public String getSearchQueryForInvestigateDisplayLogs(String tsfrom,
			String tsto, String query_String, String facility, String Object_type, String Activity_type, String user, String apiServerUrl,
			String csrfToken, String sessionId) throws Exception{
		
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().
				startObject("source").
					startObject("query").
						startObject("filtered").
							startObject("query").
								startObject("bool").
									startArray("must").
										startObject().
											startObject("range").
												startObject("created_timestamp").
													field("from", tsfrom).
													field("to", tsto).
												endObject().
											endObject().
										endObject().
										startObject().
											startObject("query_string").
												field("query", query_String).
												field("default_operator", "AND").
												field("analyzer", "custom_search_analyzer").
												field("allow_leading_wildcard", "false").
											endObject().
										endObject().
										startObject().
											startObject("term"). 
			      		         				field("facility", facility).
			      		         			endObject().
			      		         		endObject().
										startObject().
											startObject("term"). 
				      		         			field("Object_type", Object_type).
				      		         		endObject().
				      		         	endObject().
										startObject().
											startObject("term"). 
					      		         		field("Activity_type", Activity_type).
					      		         	endObject().
					      		         endObject().
									endArray().
										startArray("must_not").
											startObject().
												startObject("term").
													field("facility", "Elastica").
												endObject().
											endObject().
										endArray().
									endObject().				
								endObject().
								startObject("filter").
								endObject().
							endObject().
						endObject().
						field("from", 0).
						field("size", 50).
						startObject("sort")
							.startObject("created_timestamp").
								field("order", "desc").
								field("ignore_unmapped", "true").
							endObject().
						endObject().
						startObject("facets").
						endObject().
					endObject().
					field("sourceName", "investigate").
					field("apiServerUrl", apiServerUrl).
					field("csrftoken", csrfToken).
					field("sessionid", sessionId).
					field("userid", user).
				endObject();										
		return builder.string();
	}

	/**
	 * @param previousHoursDateFromCurrentTime
	 * @param currentTime
	 * @param groupName
	 * @param string
	 * @param string2
	 * @param username
	 * @param string3
	 * @param csfrToken
	 * @param sessionID
	 * @return
	 * @throws IOException 
	 */
	public String getSearchQueryForInvestigateDisplayLogs(String tsfrom,
			String tsto, String query_String, String facility, String Object_type, String user, String apiServerUrl,
			String csrfToken, String sessionId) throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().
				startObject("source").
					startObject("query").
						startObject("filtered").
							startObject("query").
								startObject("bool").
									startArray("must").
										startObject().
											startObject("range").
												startObject("created_timestamp").
													field("from", tsfrom).
													field("to", tsto).
												endObject().
											endObject().
										endObject().
										startObject().
											startObject("query_string").
												field("query", query_String).
												field("default_operator", "AND").
												field("analyzer", "custom_search_analyzer").
												field("allow_leading_wildcard", "false").
											endObject().
										endObject().
										startObject().
											startObject("term"). 
			      		         				field("facility", facility).
			      		         			endObject().
			      		         		endObject().
										startObject().
											startObject("term"). 
				      		         			field("Object_type", Object_type).
				      		         		endObject().
				      		         	endObject().										
									endArray().
										startArray("must_not").
											startObject().
												startObject("term").
													field("facility", "Elastica").
												endObject().
											endObject().
										endArray().
									endObject().				
								endObject().
								startObject("filter").
								endObject().
							endObject().
						endObject().
						field("from", 0).
						field("size", 50).
						startObject("sort")
							.startObject("created_timestamp").
								field("order", "desc").
								field("ignore_unmapped", "true").
							endObject().
						endObject().
						startObject("facets").
						endObject().
					endObject().
					field("sourceName", "investigate").
					field("apiServerUrl", apiServerUrl).
					field("csrftoken", csrfToken).
					field("sessionid", sessionId).
					field("userid", user).
				endObject();										
		return builder.string();
	}
       
        public String getGoogleAppPayloadForDisplayLog(String to_time, String from_time,String csrfToken, String sessionId, String user, String appServerUrl){
            String payload="{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""
                    + "FROM"
                    + "\",\"to\":\""
                    + "TO"
                    + "\"}}},{\"terms\":{\"facility\":[\"Google Drive\",\"Gmail\"]}}]}}}},\"from\":0,\"size\":700,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"facets\":{\"histoGreen\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":\"+05:30\",\"post_zone\":\"+05:30\"},\"facet_filter\":{\"term\":{\"severity\":\"informational\"}}},\"histoOrange\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":\"+05:30\",\"post_zone\":\"+05:30\"},\"facet_filter\":{\"term\":{\"severity\":\"warning\"}}},\"histoRed\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":\"+05:30\",\"post_zone\":\"+05:30\"},\"facet_filter\":{\"term\":{\"severity\":\"critical\"}}},\"histoYellow\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":\"+05:30\",\"post_zone\":\"+05:30\"},\"facet_filter\":{\"term\":{\"severity\":\"error\"}}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}},\"location\":{\"terms\":{\"field\":\"location\",\"size\":1000}},\"Object_type\":{\"terms\":{\"field\":\"Object_type\",\"size\":1000}},\"Activity_type\":{\"terms\":{\"field\":\"Activity_type\",\"size\":1000}},\"__source\":{\"terms\":{\"field\":\"__source\",\"size\":1000}},\"Resource_Id\":{\"terms\":{\"field\":\"Resource_Id\",\"size\":1000}}}},\"sourceName\":\"Google Apps\",\"apiServerUrl\":\""
                    + "APISERVERURL"
                    + "\",\"csrftoken\":\""
                    + "CSRF"
                    + "\",\"sessionid\":\""
                    + "SESSIONID"
                    + "\",\"userid\":\""
                    + "USERID"
                    + "\"}"; 
            String replaceAll = payload.replaceAll("TO", to_time)
                    .replaceAll("FROM", from_time)
                    .replaceAll("CSRF", csrfToken)
                    .replaceAll("SESSIONID", sessionId)
                    .replaceAll("USERID", user)
                    .replaceAll("APISERVERURL", appServerUrl);
             
         return replaceAll;   
        }
        
      public String generatePayLoadForDisplayLog(String to_time, String from_time, String SaaSApp, String csrfToken, String sessionId, String user, String appServerUrl) {
        return "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\"FROM\",\"to\":\"TO\"}}},{\"term\":{\"facility\":\"SAASAPP\"}}]}}}},\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"from\":0,\"size\":1000,\"facets\":{\"histoGreen\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"informational\"}}},\"histoOrange\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"warning\"}}},\"histoRed\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"critical\"}}},\"histoYellow\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"error\"}}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}},\"location\":{\"terms\":{\"field\":\"location\",\"size\":1000}},\"Object_type\":{\"terms\":{\"field\":\"Object_type\",\"size\":1000}},\"Activity_type\":{\"terms\":{\"field\":\"Activity_type\",\"size\":1000}},\"__source\":{\"terms\":{\"field\":\"__source\",\"size\":1000}},\"Resource_Id\":{\"terms\":{\"field\":\"Resource_Id\",\"size\":1000}}}},\"sourceName\":\"SAASAPP\",\"apiServerUrl\":\"APISERVERURL\",\"csrftoken\":\"CSRF\",\"sessionid\":\"SESSIONID\",\"userid\":\"USERID\"}"
                .replaceAll("TO", to_time)
                .replaceAll("FROM", from_time)
                .replaceAll("CSRF", csrfToken)
                .replaceAll("SESSIONID", sessionId)
                .replaceAll("USERID", user)
                .replaceAll("SAASAPP", SaaSApp)
                .replaceAll("APISERVERURL", appServerUrl);
    }
}
