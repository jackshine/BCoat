package com.elastica.beatle.audit;

import java.util.ArrayList;
import java.util.List;

import com.elastica.beatle.detect.dto.Objects;

public class AuditTIAAttributesDTO
{
    private List<Objects> objects;

	/**
	 * @return the objects
	 */
	public List<Objects> getObjects() {
		
		if(objects==null){
			List<Objects> objects  = new ArrayList<Objects>();
			return objects;
		}
		return objects;
	}

	/**
	 * @param objects the objects to set
	 */
	public void setObjects(List<Objects> objects) {
		this.objects = objects;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DetectAttributeDto [objects=" + objects + "]";
	}

   
}