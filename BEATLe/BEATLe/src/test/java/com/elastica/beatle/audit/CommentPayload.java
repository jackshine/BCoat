package com.elastica.beatle.audit;

public class CommentPayload {
	
	String commentStr;
	 String commmentFieldId;
	 String commentCreatedBy;
	 String commentApp;
	 String commentComponent;
	 
	public String getCommentStr() {
		return commentStr;
	}
	public void setCommentStr(String commentStr) {
		this.commentStr = commentStr;
	}
	public String getCommmentFieldId() {
		return commmentFieldId;
	}
	public void setCommmentFieldId(String commmentFieldId) {
		this.commmentFieldId = commmentFieldId;
	}
	public String getCommentCreatedBy() {
		return commentCreatedBy;
	}
	public void setCommentCreatedBy(String commentCreatedBy) {
		this.commentCreatedBy = commentCreatedBy;
	}
	public String getCommentApp() {
		return commentApp;
	}
	public void setCommentApp(String commentApp) {
		this.commentApp = commentApp;
	}
	public String getCommentComponent() {
		return commentComponent;
	}
	public void setCommentComponent(String commentComponent) {
		this.commentComponent = commentComponent;
	}
	 

}
