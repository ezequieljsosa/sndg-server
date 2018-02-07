package ar.com.bia.dto;

import java.util.HashMap;
import java.util.Map;

public class SessionResurce {

	private String name;
	private Map<String, String> values;

	public SessionResurce() {
		super();
		this.values = new HashMap<String, String>();
	}

	public SessionResurce(String resourceName) {
		this();
		this.setName(resourceName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String get(String name) {
		return this.values.get(name);
	}

	public void put(String name, String id) {
		this.values.put(name, id);
	}
	
	public void remove(String name ) {
		this.values.remove(name);
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	

}
