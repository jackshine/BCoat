package com.elastica.beatle.tests.gmail;

public class GmailActivityLog {
		//Email logs
		private String sendPlainMessageLog			= "User sent an email to EMAIL with subject \"{subject}\"";
		private String sendMimeMessageLog			= "User downloaded {filename}";
		private String trashMessageLog				= "User trashed email with subject \"{subject}\"";
		private String receivePlainMessageLog			= "User received an email from EMAIL with subject \"{subject}\"";
		private String sendAttachmentMessageLog			= "User sent an email to EMAIL with subject \"{subject}\"";
		private String sendAttachmentMessageLogDVC			= "User sent an email to EMAIL with subject \"{subject}\"";
		private String deletePlainMessageLog			= "User deleted with subject \"{subject}\"";
		private String draftMessageLog					= "User created email draft with subject \"{subject}\"";
		private String receiveMimeMessageLog			= "User downloaded {filename}";

		public String getSendAttachmentMessageLogDVC() {
			return sendAttachmentMessageLogDVC;
		}
		public void setSendAttachmentMessageLogDVC(String sendAttachmentMessageLogDVC) {
			this.sendAttachmentMessageLogDVC = sendAttachmentMessageLogDVC;
		}
				public String getDraftMessageLog() {
			return draftMessageLog;
		}
		public void setDraftMessageLog(String draftMessageLog) {
			this.draftMessageLog = draftMessageLog;
		}
		public String getDeletePlainMessageLog() {
			return deletePlainMessageLog;
		}
		public void setDeletePlainMessageLog(String deletePlainMessageLog) {
			this.deletePlainMessageLog = deletePlainMessageLog;
		}
		public String getSendAttachmentMessageLog() {
			return sendAttachmentMessageLog;
		}
		public void setSendAttachmentMessageLog(String sendAttachmentMessageLog) {
			this.sendAttachmentMessageLog = sendAttachmentMessageLog;
		}
		public String getReceivePlainMessageLog() {
			return receivePlainMessageLog;
		}
		public void setReceivePlainMessageLog(String receivePlainMessageLog) {
			this.receivePlainMessageLog = receivePlainMessageLog;
		}
		
		public String getTrashMessageLog() {
			return trashMessageLog;
		}
		public void setTrashMessageLog(String trashMessageLog) {
			this.trashMessageLog = trashMessageLog;
		}
		/**
		 * @return the sendPlainMessageLog
		 */
		public String getSendPlainMessageLog() {
			return sendPlainMessageLog;
		}
		/**
		 * @param sendPlainMessageLog the sendPlainMessageLog to set
		 */
		public void setSendPlainMessageLog(String sendPlainMessageLog) {
			this.sendPlainMessageLog = sendPlainMessageLog;
		}
		/**
		 * @return the sendMimeMessageLog
		 */
		public String getSendMimeMessageLog() {
			return sendMimeMessageLog;
		}
		/**
		 * @param sendMimeMessageLog the sendMimeMessageLog to set
		 */
		public void setSendMimeMessageLog(String sendMimeMessageLog) {
			this.sendMimeMessageLog = sendMimeMessageLog;
		}
		
		
		
}

