package com.elastica.beatle.detect.dto;

import java.util.List;

public class Source
{
    private boolean enabled;

    private int importance;

    private String description;

    private String name;

    private List<Groups> groups;

    public int getImportance() {
		return importance;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

	private String ts_label;

  

    public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

   

    public String getTs_label ()
    {
        return ts_label;
    }

    public void setTs_label (String ts_label)
    {
        this.ts_label = ts_label;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [enabled = "+enabled+", importance = "+importance+", description = "+description+", name = "+name+", groups = "+groups+", ts_label = "+ts_label+"]";
    }
}
			
			