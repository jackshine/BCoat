package com.elastica.beatle.es;

import org.codehaus.jackson.map.ObjectMapper;

public class Activity {
	private ActivitySource _source;
	private String _score;
	private String _index;
	private String _type;
	
	public ActivitySource get_source() {
		return _source;
	}

	public void set_source(ActivitySource _source) {
		this._source = _source;
	}

	public String get_score() {
		return _score;
	}

	public void set_score(String _score) {
		this._score = _score;
	}

	public String get_index() {
		return _index;
	}

	public void set_index(String _index) {
		this._index = _index;
	}

	public String get_type() {
		return _type;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

	public void createActivity() {
		ObjectMapper mapper = new ObjectMapper();
	}
	
	@Override
	public String toString() {
		return "Activity: " + _score + "," + _type + _source;
	}
}
