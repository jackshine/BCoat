package com.elastica.beatle.audit;

import java.util.Date;

import org.elasticsearch.common.joda.time.DateTime;


public class TestDsDelete implements Comparable<TestDsDelete> {
	
	String dataSourceId;
	private DateTime  dsCreationDateTime;
	String dsname;
	
	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public DateTime getDateTime() {
	    return dsCreationDateTime;
	  }

	  public void setDateTime(DateTime datetime) {
	    this.dsCreationDateTime = datetime;
	  }
	
	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	@Override
	public int compareTo(TestDsDelete o) {
		if (getDateTime() == null || o.getDateTime() == null)
		      return 0;
		  return getDateTime().toDate().compareTo(o.getDateTime().toDate());
	}

	@Override
	public String toString() {
		return "TestDsDelete [dataSourceId=" + dataSourceId + ", dsCreationDateTime=" + dsCreationDateTime + ", dsname="
				+ dsname + "]";
	}



}
