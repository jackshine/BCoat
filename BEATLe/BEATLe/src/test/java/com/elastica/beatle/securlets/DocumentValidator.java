package com.elastica.beatle.securlets;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

import com.elastica.beatle.securlets.dto.BoxDocument;
import com.elastica.beatle.securlets.dto.Exposures;
import com.elastica.beatle.securlets.dto.O365Document;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.ShareInfo;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import com.universal.dtos.UserAccount;

public class DocumentValidator {
	BoxDocument documents;

	public DocumentValidator() {

	}

	public DocumentValidator(BoxDocument docs) {
		this.documents = docs;
	}

	public void validateExposedDocuments(BoxDocument docs, boolean isInternal) {
		
		for (com.elastica.beatle.securlets.dto.Object boxDocObject : docs.getObjects()) {
			Reporter.log("Verifying the document with identification :"+ boxDocObject.getIdentification(), true);
			Reporter.log("*******************************************************************************", true);
			//Evaluated expression, expected message, actual message
			CustomAssertion.assertTrue((boxDocObject.getActivityCount() >=0), "Document activity count is >= 0", 
																			  "Document Activity count is not >=0");
			
			CustomAssertion.assertTrue((boxDocObject.getContentChecks() != null), 
												"Document content checks not null",	"Document content checks is null");
			
			CustomAssertion.assertTrue((boxDocObject.getCreatedBy() != null), "Document created by not null", "Document created by is null");
			
			CustomAssertion.assertTrue((boxDocObject.getDocType()!= null), "Document type not null", "Document type is null");
			
			CustomAssertion.assertTrue(boxDocObject.getExposed(), "Document exposed value not false", "Document exposed value is false");
			
			CustomAssertion.assertTrue(boxDocObject.getExposures() != null, "Document exposures value not null", "Document exposures value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getExposures().getAllInternal() != null, "Document exposures all internal value not null", 
																							"Document exposures all internal value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getExposures().getInternal() != null, "Document exposures internal value not null",
																							"Document exposures internal value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getExposures().getIntCount() >= 0, 
										"Document exposures internal count greater than zero", "Document exposures internal count is < 0");
			
			CustomAssertion.assertTrue(boxDocObject.getExposures().getExtCount() >= 0, 
										"Document exposures external count greater than zero", "Document exposures external count < zero");
			
			CustomAssertion.assertTrue(boxDocObject.getExposures().getExternal() != null, 
										"Document exposures external value not null", "Document exposures external value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getExposures().getPublic() != null, 
										"Document exposures public value not null", "Document exposures public value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getFormat()!= null, "Document format value not null", "Document format value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getIdentification() != null, 
										"Document identification value not null", "Document identification value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getIsInternal() == isInternal, 
										"Document is_internally owned value not false", "Document is_internally owned value is false");
			
			CustomAssertion.assertTrue(boxDocObject.getName() != null, 
										"Document name value not null", "Document name value is null");
			
			CustomAssertion.assertTrue((boxDocObject.getObjectType() != null || boxDocObject.getObjectType() == null), 
										"Document object type is present", "Document object type is not present");
			
			CustomAssertion.assertTrue(boxDocObject.getOwnedBy() != null, 
										"Document owned by value not null", "Document owned by value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getOwnerId() != null, 
										"Document owner id value not null", "Document owner id value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getParentId() != null, 
										"Document parent id value not null", "Document parent id value is null");
			
			CustomAssertion.assertTrue(boxDocObject.getParentName() != null, 
										"Document parent name value not null", "Document parent name is null");
			
			CustomAssertion.assertTrue(boxDocObject.getPath() != null, 
										"Document path value not null", "Document path is null");
			
			CustomAssertion.assertTrue((boxDocObject.getScanState() != null || boxDocObject.getScanState() == null), 
										"Document scan state is present", "Document scan state is not present");
			
			CustomAssertion.assertTrue((boxDocObject.getScanTs() != null || boxDocObject.getScanTs() == null),
										"Document scan ts is present", "Document scan ts is not present");
			
			CustomAssertion.assertTrue((boxDocObject.getShareInfo() != null), 
										"Document share info not null", "Document share info is null");
			
			CustomAssertion.assertTrue((boxDocObject.getSize() >= 0), 
										"Document size is >= 0", "Document size is < 0");
			
			CustomAssertion.assertTrue((boxDocObject.getUrl() != null), "Document url not null", "Document url is null");
		}
	}

	public void verifyDocument(BoxDocument docs, String identification, String exposureType, String access, HashMap<String, String> hmap) {
		
/*		Class<?> c = com.elastica.beatle.securlets.dto.Object.class;
		Method  method = c.getDeclaredMethod ("method name", parameterTypes)
		method.invoke (objectToInvokeOn, params)
*/		
		for(com.elastica.beatle.securlets.dto.Object docObject : docs.getObjects()) {
			if(docObject.getIdentification().equals(identification)) {
				Exposures exposures = docObject.getExposures();
				ShareInfo shareInfo = docObject.getShareInfo();
				
				if(exposureType.equals("ALL_INTERNAL")) {
					CustomAssertion.assertTrue(exposures.getAllInternal(), "All internal expsoed documents value mismatch");
				}
				CustomAssertion.assertEquals(shareInfo.getAccess(), access, 		 			"Access value mismatch");
				CustomAssertion.assertEquals(shareInfo.getEffectiveAccess(), access, 			"Effective access value mismatch");
				CustomAssertion.assertEquals(docObject.getCreatedBy(), hmap.get("createdBy"), 	"Created by value mismatch");
				CustomAssertion.assertEquals(docObject.getOwnedBy(), hmap.get("ownedBy"), 		"Owned by value mismatch");
				CustomAssertion.assertEquals(docObject.getFormat(), hmap.get("format"), 		"Format value mismatch");
				CustomAssertion.assertEquals(docObject.getName(), hmap.get("name"), 			"name value mismatch");
				CustomAssertion.assertEquals(docObject.getOwnerId(), hmap.get("ownerId"), 		"Owner Id value mismatch");
				CustomAssertion.assertEquals(String.valueOf(docObject.getSize()), hmap.get("size"), 		    "Size value mismatch");
				CustomAssertion.assertEquals(docObject.getParentName(), hmap.get("parentName"), "Parent name value mismatch");
				CustomAssertion.assertEquals(docObject.getDocType(), hmap.get("docType"), 		"Doc type value mismatch");
			}
		}
	}
	
	public void verifyPubliclyExposedDocument(SecurletDocument docs,  UserAccount saasAppUserAccount, String filename, String docType) {
		
		CustomAssertion.assertEquals(docs.getMeta().getTotalCount(), 1, "Publicly exposed document not returned");
		
		CustomAssertion.assertEquals(docs.getObjects().get(0).getDocType(), docType, "Document type mismatch");
		
		if(docs.getObjects().get(0).getCreatedBy()!=null) {
			CustomAssertion.assertEquals(docs.getObjects().get(0).getCreatedBy(), 
					saasAppUserAccount.getUsername().toLowerCase(), "Created by mismatch");
		}
		
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposed().booleanValue(), "Exposed is matching", "Exposed is not matching");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIdentification() !=null, "Identification is not null", "Identification is null");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposures().getPublic().booleanValue(), "Public exposure specified", "Public exposure not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIsInternal().booleanValue(),"is internal specified", "is internal not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getSize() > 0, "Size is greater than zero", "Size is not greater than zero");
		CustomAssertion.assertEquals(docs.getObjects().get(0).getName(), filename, "Document name mismatch");
		
		String saasAppUser = saasAppUserAccount.getUsername().toLowerCase();
		if(saasAppUser.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUser.toLowerCase()).replace(".sandbox", "");
		} 
		CustomAssertion.assertEquals(docs.getObjects().get(0).getOwnedBy(), saasAppUser, "Owner name mismatch");
		
	}
	
	
	public void verifyInternallyExposedDocument(SecurletDocument docs,  UserAccount saasAppUserAccount, String filename, String docType) {
		
		CustomAssertion.assertEquals(docs.getMeta().getTotalCount(), 1, "Internally exposed document not returned");
		
		CustomAssertion.assertEquals(docs.getObjects().get(0).getDocType(), docType, "Document type mismatch");
		
		if(docs.getObjects().get(0).getCreatedBy()!=null) {
			CustomAssertion.assertEquals(docs.getObjects().get(0).getCreatedBy(), 
					saasAppUserAccount.getUsername().toLowerCase(), "Created by mismatch");
		}
		
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposed().booleanValue(), "Exposed is matching", "Exposed is not matching");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIdentification() !=null, "Identification is not null", "Identification is null");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposures().getAllInternal().booleanValue(), "Internal exposure specified", "Internal exposure not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIsInternal().booleanValue(),"is internal specified", "is internal not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getSize() > 0, "Size is greater than zero", "Size is not greater than zero");
		CustomAssertion.assertEquals(docs.getObjects().get(0).getName(), filename, "Document name mismatch");
		
		String saasAppUser = saasAppUserAccount.getUsername().toLowerCase();
		
		if(saasAppUser.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUser.toLowerCase()).replace(".sandbox", "");
		} 
		CustomAssertion.assertEquals(docs.getObjects().get(0).getOwnedBy(), saasAppUser, "Owner name mismatch");
		
	}
	
	public void verifyExternallyExposedDocument(SecurletDocument docs,  UserAccount saasAppUserAccount, String filename, String docType) {
		
		CustomAssertion.assertEquals(docs.getMeta().getTotalCount(), 1, "Externally exposed document not returned");
		
		CustomAssertion.assertEquals(docs.getObjects().get(0).getDocType(), docType, "Document type mismatch");
		
		if(docs.getObjects().get(0).getCreatedBy()!=null) {
			CustomAssertion.assertEquals(docs.getObjects().get(0).getCreatedBy(), 
					saasAppUserAccount.getUsername().toLowerCase(), "Created by mismatch");
		}
		
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposed().booleanValue(), "Exposed is matching", "Exposed is not matching");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIdentification() !=null, "Identification is not null", "Identification is null");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposures().getExternal().size() > 0, "External exposure specified", "Internal exposure not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIsInternal().booleanValue(),"is internal specified", "is internal not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getSize() > 0, "Size is greater than zero", "Size is not greater than zero");
		CustomAssertion.assertEquals(docs.getObjects().get(0).getName(), filename, "Document name mismatch");
		
		String saasAppUser = saasAppUserAccount.getUsername().toLowerCase();
		if(saasAppUser.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUser.toLowerCase()).replace(".sandbox", "");
		} 
		CustomAssertion.assertEquals(docs.getObjects().get(0).getOwnedBy(), saasAppUser, "Owner name mismatch");
		
	}
	
	
	public void verifyExternallyExposedDocumentWithExternalSwitch(SecurletDocument docs,  UserAccount saasAppUserAccount, String filename, String docType, String internalUser) {
		
		CustomAssertion.assertEquals(docs.getMeta().getTotalCount(), 1, "Externally exposed document not returned");
		
		CustomAssertion.assertEquals(docs.getObjects().get(0).getDocType(), docType, "Document type mismatch");
		
		if(docs.getObjects().get(0).getCreatedBy()!=null) {
			CustomAssertion.assertEquals(docs.getObjects().get(0).getCreatedBy(), 
					saasAppUserAccount.getUsername().toLowerCase(), "Created by mismatch");
		}
		
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposed().booleanValue(), "Exposed is matching", "Exposed is not matching");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIdentification() !=null, "Identification is not null", "Identification is null");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposures().getInternal().size() > 0, "Internal exposure specified", "Internal exposure not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getExposures().getInternal().contains(internalUser), "Internally exposed user is present", "Internally exposed user is not present");
		CustomAssertion.assertTrue(!docs.getObjects().get(0).getIsInternal().booleanValue(),"is internal specified", "is internal not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getSize() >= 0, "Size is zero", "Size is null");
		CustomAssertion.assertEquals(docs.getObjects().get(0).getName(), filename, "Document name mismatch");
		
		String saasAppUser = saasAppUserAccount.getUsername().toLowerCase();
		if(saasAppUser.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUser.toLowerCase()).replace(".sandbox", "");
		} 
		CustomAssertion.assertEquals(docs.getObjects().get(0).getOwnedBy(), saasAppUser, "Owner name mismatch");
		
	}
	
	public void verifyUnexposedDocument(SecurletDocument docs,  UserAccount saasAppUserAccount, String filename, String docType) {
		
		CustomAssertion.assertEquals(docs.getMeta().getTotalCount(), 1, "Unexposed document not returned");
		
		CustomAssertion.assertEquals(docs.getObjects().get(0).getDocType(), docType, "Document type mismatch");
		CustomAssertion.assertEquals(docs.getObjects().get(0).getCreatedBy(), 
													saasAppUserAccount.getUsername().toLowerCase(), "Created by mismatch");
		
		CustomAssertion.assertFalse(docs.getObjects().get(0).getExposed().booleanValue(), "Exposed is not matching");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIdentification() !=null, "Identification is null");
		CustomAssertion.assertFalse(docs.getObjects().get(0).getExposures().getPublic().booleanValue(), "Public exposure not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getIsInternal().booleanValue(), "is internal not specified");
		CustomAssertion.assertTrue(docs.getObjects().get(0).getSize() > 0, "Size should be greater than zero");
		CustomAssertion.assertEquals(docs.getObjects().get(0).getName(), filename, "Document name mismatch");
		CustomAssertion.assertEquals(docs.getObjects().get(0).getOwnedBy(), saasAppUserAccount.getUsername().toLowerCase(), "Owner name mismatch");
		
	}
	
	public void validateExposedDocuments(SecurletDocument docs, boolean isInternal) {
		
		for (com.elastica.beatle.securlets.dto.Object o365Document : docs.getObjects()) {
			Reporter.log("Verifying the document with identification :"+ o365Document.getIdentification(), true);
			Reporter.log("*******************************************************************************", true);
			//Evaluated expression, expected message, actual message
			CustomAssertion.assertTrue((o365Document.getActivityCount() >=0), "Document activity count is >= 0", 
					"Document Activity count is not >=0");
			
			CustomAssertion.assertTrue((o365Document.getContentChecks() != null), 
					"Document content checks not null",	"Document content checks is null");
			
			CustomAssertion.assertTrue((o365Document.getCreatedBy() != null), "Document created by not null", "Document created by is null");
			
			CustomAssertion.assertTrue((o365Document.getDocType()!= null), "Document type not null", "Document type is null");
			
			CustomAssertion.assertTrue(o365Document.getExposed()==true || o365Document.getExposed() ==false, "Document exposed field value present", "Document exposed field value is not present");
			
			CustomAssertion.assertTrue(o365Document.getExposures() != null, "Document exposures value not null", "Document exposures value is null");
			
			CustomAssertion.assertTrue(o365Document.getExposures().getAllInternal() != null, "Document exposures all internal value not null", 
					"Document exposures all internal value is null");
			
			CustomAssertion.assertTrue(o365Document.getExposures().getInternal() != null, "Document exposures internal value not null",
					"Document exposures internal value is null");
			
			CustomAssertion.assertTrue(o365Document.getExposures().getIntCount() >= 0, 
					"Document exposures internal count greater than zero", "Document exposures internal count is < 0");
			
			CustomAssertion.assertTrue(o365Document.getExposures().getExtCount() >= 0, 
					"Document exposures external count greater than zero", "Document exposures external count < zero");
			
			CustomAssertion.assertTrue(o365Document.getExposures().getExternal() != null, 
					"Document exposures external value not null", "Document exposures external value is null");
			
			CustomAssertion.assertTrue(o365Document.getExposures().getPublic() != null, 
					"Document exposures public value not null", "Document exposures public value is null");
			
			CustomAssertion.assertTrue(o365Document.getFormat()!= null, "Document format value not null", "Document format value is null");
			
			CustomAssertion.assertTrue(o365Document.getIdentification() != null, 
					"Document identification value not null", "Document identification value is null");
			
			CustomAssertion.assertTrue(o365Document.getIsInternal() == isInternal || o365Document.getIsInternal() != isInternal ,
					"Document is_internally owned value not false", "Document is_internally owned value is false");
			
			CustomAssertion.assertTrue(o365Document.getName() != null, 
					"Document name value not null", "Document name value is null");
			
			CustomAssertion.assertTrue((o365Document.getObjectType() != null || o365Document.getObjectType() == null), 
					"Document object type is present", "Document object type is not present");
			
			CustomAssertion.assertTrue(o365Document.getOwnedBy() != null, 
					"Document owned by value not null", "Document owned by value is null");
			
			CustomAssertion.assertTrue(o365Document.getOwnerId() != null || o365Document.getOwnerId() == null,
					"Document owner id value is present", "Document owner id value is not present");
			
			CustomAssertion.assertTrue(o365Document.getParentId() != null || o365Document.getParentId() == null,
					"Document parent id value  present", "Document parent id value is not present");
			
			CustomAssertion.assertTrue(o365Document.getParentName() != null || o365Document.getParentName() == null ,
					"Document parent name value not null", "Document parent name is null");
			
			CustomAssertion.assertTrue(o365Document.getPath() != null || o365Document.getPath() == null, 
					"Document path value is present", "Document path is not present");
//			
			CustomAssertion.assertTrue((o365Document.getScanState() != null || o365Document.getScanState() == null), 
					"Document scan state is present", "Document scan state is not present");
			
			CustomAssertion.assertTrue((o365Document.getScanTs() != null || o365Document.getScanTs() == null),
					"Document scan ts is present", "Document scan ts is not present");
			
			CustomAssertion.assertTrue((o365Document.getShareInfo() != null ) || (o365Document.getShareInfo() == null ), 
					"Document share info is present", "Document share info is not present");
			
			CustomAssertion.assertTrue((o365Document.getSize() >= -1), 
					"Document size is >= -1.", "Document size is < -1.");
			
			CustomAssertion.assertTrue((o365Document.getUrl() == null) || (o365Document.getUrl() != null), "Document url is present", "Document url is not present");
		}
	}
	public void validateUsers(SecurletDocument docs, boolean isInternal) {
		
		for (com.elastica.beatle.securlets.dto.Object o365Document : docs.getObjects()) {
			Reporter.log("Verifying the user with email:"+ o365Document.getEmail(), true);
			Reporter.log("*******************************************************************************", true);
			//Evaluated expression, expected message, actual message
			CustomAssertion.assertTrue((o365Document.getDocsExposed() >=0), "User Exposed document count is >= 0", "User Exposed document count is not >=0");
			CustomAssertion.assertTrue((o365Document.getEmail() != null), "User email id not null",	"User email id is null");
			CustomAssertion.assertTrue((o365Document.getId()!= null), "Id field not null", "Id is null");
			CustomAssertion.assertTrue(o365Document.getIsInternal() == isInternal || o365Document.getIsInternal() != isInternal, "User is_internal field is:"+isInternal, "User is_internally field missing");
			CustomAssertion.assertTrue(o365Document.getUserId() != null, "UserId field not null", "UserId field is null");
			CustomAssertion.assertTrue(o365Document.getUsername() != null, "UserName field not null", "UserName field is null");
			
		}
	}

	
	
	
	
	
}
