package com.elastica.beatle.securlets;

import java.util.ArrayList;
import java.util.List;

class Reportfilter {
	
	private String field;
	private String type;
	private String op;
	private List<String> value = new ArrayList<String>();
	
	
	public Reportfilter(String field, String type, String op, ArrayList<String> value) {
		this.field = field;
		this.type  = type;
		this.op = op;
		this.value = value;
	}

	
	public String getField() {
	return field;
	}

	
	public void setField(String field) {
	this.field = field;
	}

	
	public String getType() {
	return type;
	}

	
	public void setType(String type) {
	this.type = type;
	}

	
	public String getOp() {
	return op;
	}

	
	public void setOp(String op) {
	this.op = op;
	}

	
	public List<String> getValue() {
	return value;
	}

	
	public void setValue(List<String> value) {
	this.value = value;
	}
	
	 
}
