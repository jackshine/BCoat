package com.elastica.beatle.es;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class ESQueryBuilder {

	public String getSearchQueryForDisplayLogs(String tsFrom, String tsTo, String query, String facility, int count, String apiServer, String csrfToken, String sessionId, String user) throws Exception {
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
			                    field("from", tsFrom).
			                    field("to", tsTo).
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
			                  field("facility", facility).
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
			    field("size", count).
			    startObject("sort").
			      startObject("created_timestamp").
			        field("order", "desc").
			        field("ignore_unmapped", "true").
			      endObject().
			    endObject().
			  endObject().
			  field("sourceName", "investigate").
			  field("apiServerUrl", apiServer).
			  field("csrftoken", csrfToken).
			  field("sessionid", sessionId).
			  field("userid", user).
			endObject();
		
		return builder.string();
	}
	
	public String getSearchQueryForDetect(String tsFrom, String tsTo) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
			startObject().
			  startObject("source").
			    startObject("query").
			          startObject("bool").
			            startArray("must").
			              startObject().
			                startObject("range").
			                  startObject("updated_timestamp").
			                    field("from", tsFrom).
			                    field("to", tsTo).
			                  endObject().
			                endObject().
			              endObject().
			              startObject().
			                startObject("term").
			                  field("event_type", "AnomalyReport").
			                endObject().
			              endObject().
			            endArray().
			            startArray("should").
			              startObject().
			                startObject("term").
			                  field("severity", "high").
			                endObject().
			              endObject().
			              startObject().
			                startObject("term").
			                  field("severity", "medium").
			                endObject().
			              endObject().
			              startObject().
			                startObject("term").
			                  field("severity", "low").
			                endObject().
			              endObject().
			            endArray().
			            field("minimum_should_match", 1).
			            startObject("must_not").
			            startObject("term").
		                  field("__source", "audit").
		                endObject().
		                endObject().
		                endObject().
		                endObject().
		                startObject("sort").
		                startObject("updated_timestamp").
		                field("order", "desc").
		                field("ignore_unmapped", "true").
		                endObject().
		                endObject().
		                field("size", 1000).
		                endObject().
		                field("sourceName", "DETECT").
			endObject();
		
		return builder.string();
	}
	

	public String getSearchQueryForGWDisplayLogs(String tsFrom, String tsTo, String query, String facility, int count, String apiServer, String csrfToken, String sessionId, String user) throws Exception {
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
			                    field("from", tsFrom).
			                    field("to", tsTo).
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
			                  field("facility", facility).
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
			    field("size", count).
			    startObject("sort").
			      startObject("created_timestamp").
			        field("order", "desc").
			        field("ignore_unmapped", "true").
			      endObject().
			    endObject().
			  endObject().
			  field("sourceName", "investigate").
			  field("apiServerUrl", apiServer).
			  field("csrftoken", csrfToken).
			  field("sessionid", sessionId).
			  field("userid", user).
			endObject();
		
		return builder.string();
	}
	
	public String getESQueryForInvestigate(String tsfrom, String facility, HashMap<String, String> terms, 
			String user, String apiServerUrl, String csrfToken, String sessionId,  int size, String userid) throws Exception {
		String toTime=new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).plusDays(1).toString(); 
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
							      		         field("to", toTime).
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
				field("userid", userid);
				System.out.println("Payload: "+builder.string());
				return builder.string();
		//return qstring;
	}
	
	
	public String getESQuery(String tsfrom, String facility, HashMap<String, String> terms, 
			String user, String apiServerUrl, String csrfToken, String sessionId, int size, String userid) throws Exception {
		
		String toTime=new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).plusDays(1).toString(); 
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
							      		         field("to", toTime).
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
				field("app", facility).
				field("sourceName", "investigate").
				field("apiServerUrl", apiServerUrl).
				field("csrftoken", csrfToken).
				field("sessionid", sessionId).
				field("userid", userid);
										
				System.out.println(builder.string());
				return builder.string();
		//return qstring;
	}

public String getSearchQueryForTIALogs(String tsFrom, String tsTo) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				
				startObject().
				  startObject("source").
				     startObject("query").
				       startObject("bool").
					            startArray("must").
						               startObject().
						                 startObject("range").
						                  startObject("created_timestamp").
						                    field("from", tsFrom).
						                    field("to", tsTo).
						                  endObject().
						                endObject().
						              endObject().
						              startObject().
						                 startObject("term").
						                    field("event_type", "AnomalyReport").
						                 endObject().
						              endObject().
						              startObject().
						                 startObject("term").
						                 field("__source", "audit").
						                 endObject().
						              endObject().
					            endArray(). 
			          	        startArray("should").
						           startObject().
				                     startObject("term").
				                       field("severity", "high").
				                     endObject().
				                   endObject().
				                   
				                  startObject().
				                     startObject("term").
				                       field("severity", "medium").
				                     endObject().
				                  endObject().
				                  startObject().
				                     startObject("term").
				                       field("severity", "low").
				                     endObject().
				                  endObject().
				                endArray(). 
	                         field("minimum_should_match", "1").
	                     endObject().
				     endObject().
		
					startObject("filter").
			          startObject("missing").
			            field("field", "__internal").
			          endObject().
			        endObject().
			        
			        startObject("sort").
	                  startObject("updated_timestamp").
	                    field("order", "desc").
	                    field("ignore_unmapped", "true").
	                  endObject().
	                endObject().
	                field("size", 100).
	       endObject(). 
//	       field("condense", "false").
	       field("sourceName", "AUDIT").
	       endObject();
			         
		return builder.string();
	}
	
	
}
