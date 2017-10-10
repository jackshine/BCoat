package com.elastica.beatle.detect.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DetectSequenceDto {
	
	    String id;
	    int limit;
	    public int getLimit() {
			return limit;
		}
		public void setLimit(int limit) {
			this.limit = limit;
		}
		public int getOffset() {
			return offset;
		}
		public void setOffset(int offset) {
			this.offset = offset;
		}
		int offset ;
	    String requestType;
	    
	    public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getRequestType() {
			return requestType;
		}
		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}
		public Source getSource() {
			return source;
		}
		@Override
		public String toString() {
			return "DetectSequenceDto [id=" + id + ", requestType=" + requestType + ", source=" + source + "]";
		}
		public void setSource(Source source) {
			this.source = source;
		}
		Source source;
	    
	}
				
				