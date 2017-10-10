package com.elastica.beatle.detect.dto;

import java.util.HashSet;
import java.util.Set;

public class InputBean {
	
		private String tmplFileName,fileName, userName, user, testId;
		private long count;
		private Set<String> saasApps;

		public Set<String> getSaasApps() {
			return saasApps;
		}

		public void setSaasApps(Set<String> saasApps) {
			this.saasApps = saasApps;
		}

		/**
		 * @return the count
		 */
		public long getCount() {
			return count;
		}

		/**
		 * @param count the count to set
		 */
		public void setCount(long count) {
			this.count = count;
		}

		/**
		 * @return the tmplFileName
		 */
		public String getTmplFileName() {
			return tmplFileName;
		}

		public InputBean(String tmplFileName, String fileName, String userName, String user, String testId) {
			super();
			this.tmplFileName = tmplFileName;
			this.fileName = fileName;
			this.userName = userName;
			this.user = user;
			this.testId = testId;
		}

		/**
		 * @param tmplFileName the tmplFileName to set
		 */
		public void setTmplFileName(String tmplFileName) {
			this.tmplFileName = tmplFileName;
		}

		/**
		 * @return the fileName
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * @param fileName the fileName to set
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}

		/**
		 * @return the user
		 */
		public String getUser() {
			return user;
		}

		/**
		 * @param user the user to set
		 */
		public void setUser(String user) {
			this.user = user;
		}

		/**
		 * @return the testId
		 */
		public String getTestId() {
			return testId;
		}

		/**
		 * @param testId the testId to set
		 */
		public void setTestId(String testId) {
			this.testId = testId;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "InputBean [tmplFileName=" + tmplFileName + ", fileName=" + fileName + ", userName=" + userName
					+ ", user=" + user + ", testId=" + testId + "]";
		}
		

}
