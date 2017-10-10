package com.universal.dtos.onedrive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserValue {
	
		@JsonProperty("odata.type")
		private String odataType;
		@JsonProperty("odata.id")
		private String odataId;
		@JsonProperty("odata.editLink")
		private String odataEditLink;
		@JsonProperty("Id")
		private long Id;
		@JsonProperty("IsHiddenInUI")
		private boolean IsHiddenInUI;
		@JsonProperty("LoginName")
		private String LoginName;
		@JsonProperty("Title")
		private String Title;
		@JsonProperty("PrincipalType")
		private long PrincipalType;
		@JsonProperty("Email")
		private String Email;
		@JsonProperty("IsShareByEmailGuestUser")
		private boolean IsShareByEmailGuestUser;
		@JsonProperty("IsSiteAdmin")
		private boolean IsSiteAdmin;
		@JsonProperty("UserId")
		private com.universal.dtos.onedrive.UserId UserId;
		@JsonProperty("PrincipalId")
		private String PrincipalId;
		
		@JsonProperty("Description")
		private String Description; 
		
		@JsonProperty("Hidden")
		private String Hidden;
		
		@JsonProperty("Name")
		private String Name;
		
		@JsonProperty("Order")
		private long Order;

		@JsonProperty("RoleTypeKind")
		private long RoleTypeKind;

		@JsonIgnore
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();
		
		
		/**
		 * @return the description
		 */
		public String getDescription() {
			return Description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			Description = description;
		}
		
		
		/**
		 * @return the hidden
		 */
		public String getHidden() {
			return Hidden;
		}

		/**
		 * @param hidden the hidden to set
		 */
		public void setHidden(String hidden) {
			Hidden = hidden;
		}
		
		/**
		 * @return the name
		 */
		public String getName() {
			return Name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			Name = name;
		}
		
		/**
		 * @return the order
		 */
		public long getOrder() {
			return Order;
		}

		/**
		 * @param order the order to set
		 */
		public void setOrder(long order) {
			Order = order;
		}
		
		/**
		 * @return the roleTypeKind
		 */
		public long getRoleTypeKind() {
			return RoleTypeKind;
		}

		/**
		 * @param roleTypeKind the roleTypeKind to set
		 */
		public void setRoleTypeKind(long roleTypeKind) {
			RoleTypeKind = roleTypeKind;
		}
		
		
		
		/**
		 * 
		 * @return
		 * The odataType
		 */
		@JsonProperty("odata.type")
		public String getOdataType() {
			return odataType;
		}

		/**
		 * 
		 * @param odataType
		 * The odata.type
		 */
		@JsonProperty("odata.type")
		public void setOdataType(String odataType) {
			this.odataType = odataType;
		}

		/**
		 * 
		 * @return
		 * The odataId
		 */
		@JsonProperty("odata.id")
		public String getOdataId() {
			return odataId;
		}

		/**
		 * 
		 * @param odataId
		 * The odata.id
		 */
		@JsonProperty("odata.id")
		public void setOdataId(String odataId) {
			this.odataId = odataId;
		}

		/**
		 * 
		 * @return
		 * The odataEditLink
		 */
		@JsonProperty("odata.editLink")
		public String getOdataEditLink() {
			return odataEditLink;
		}

		/**
		 * 
		 * @param odataEditLink
		 * The odata.editLink
		 */
		@JsonProperty("odata.editLink")
		public void setOdataEditLink(String odataEditLink) {
			this.odataEditLink = odataEditLink;
		}

		/**
		 * 
		 * @return
		 * The Id
		 */
		@JsonProperty("Id")
		public long getId() {
			return Id;
		}

		/**
		 * 
		 * @param Id
		 * The Id
		 */
		@JsonProperty("Id")
		public void setId(long Id) {
			this.Id = Id;
		}

		/**
		 * 
		 * @return
		 * The IsHiddenInUI
		 */
		@JsonProperty("IsHiddenInUI")
		public boolean isIsHiddenInUI() {
			return IsHiddenInUI;
		}

		/**
		 * 
		 * @param IsHiddenInUI
		 * The IsHiddenInUI
		 */
		@JsonProperty("IsHiddenInUI")
		public void setIsHiddenInUI(boolean IsHiddenInUI) {
			this.IsHiddenInUI = IsHiddenInUI;
		}

		/**
		 * 
		 * @return
		 * The LoginName
		 */
		@JsonProperty("LoginName")
		public String getLoginName() {
			return LoginName;
		}

		/**
		 * 
		 * @param LoginName
		 * The LoginName
		 */
		@JsonProperty("LoginName")
		public void setLoginName(String LoginName) {
			this.LoginName = LoginName;
		}

		/**
		 * 
		 * @return
		 * The Title
		 */
		@JsonProperty("Title")
		public String getTitle() {
			return Title;
		}

		/**
		 * 
		 * @param Title
		 * The Title
		 */
		@JsonProperty("Title")
		public void setTitle(String Title) {
			this.Title = Title;
		}

		/**
		 * 
		 * @return
		 * The PrincipalType
		 */
		@JsonProperty("PrincipalType")
		public long getPrincipalType() {
			return PrincipalType;
		}

		/**
		 * 
		 * @param PrincipalType
		 * The PrincipalType
		 */
		@JsonProperty("PrincipalType")
		public void setPrincipalType(long PrincipalType) {
			this.PrincipalType = PrincipalType;
		}

		/**
		 * 
		 * @return
		 * The Email
		 */
		@JsonProperty("Email")
		public String getEmail() {
			return Email;
		}

		/**
		 * 
		 * @param Email
		 * The Email
		 */
		@JsonProperty("Email")
		public void setEmail(String Email) {
			this.Email = Email;
		}

		/**
		 * 
		 * @return
		 * The IsShareByEmailGuestUser
		 */
		@JsonProperty("IsShareByEmailGuestUser")
		public boolean isIsShareByEmailGuestUser() {
			return IsShareByEmailGuestUser;
		}

		/**
		 * 
		 * @param IsShareByEmailGuestUser
		 * The IsShareByEmailGuestUser
		 */
		@JsonProperty("IsShareByEmailGuestUser")
		public void setIsShareByEmailGuestUser(boolean IsShareByEmailGuestUser) {
			this.IsShareByEmailGuestUser = IsShareByEmailGuestUser;
		}

		/**
		 * 
		 * @return
		 * The IsSiteAdmin
		 */
		@JsonProperty("IsSiteAdmin")
		public boolean isIsSiteAdmin() {
			return IsSiteAdmin;
		}

		/**
		 * 
		 * @param IsSiteAdmin
		 * The IsSiteAdmin
		 */
		@JsonProperty("IsSiteAdmin")
		public void setIsSiteAdmin(boolean IsSiteAdmin) {
			this.IsSiteAdmin = IsSiteAdmin;
		}

		/**
		 * 
		 * @return
		 * The UserId
		 */
		@JsonProperty("UserId")
		public com.universal.dtos.onedrive.UserId getUserId() {
			return UserId;
		}

		/**
		 * 
		 * @param UserId
		 * The UserId
		 */
		@JsonProperty("UserId")
		public void setUserId(com.universal.dtos.onedrive.UserId UserId) {
			this.UserId = UserId;
		}
		
		
		/**
		 * @return the principalId
		 */
		public String getPrincipalId() {
			return PrincipalId;
		}

		/**
		 * @param principalId the principalId to set
		 */
		public void setPrincipalId(String principalId) {
			PrincipalId = principalId;
		}

		@JsonAnyGetter
		public Map<String, Object> getAdditionalProperties() {
			return this.additionalProperties;
		}

		@JsonAnySetter
		public void setAdditionalProperty(String name, Object value) {
			this.additionalProperties.put(name, value);
		}

	}
