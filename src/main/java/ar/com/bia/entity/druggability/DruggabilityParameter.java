package ar.com.bia.entity.druggability;

import java.util.List;

public class DruggabilityParameter {

	private String name;
	private String description;
	private List<String> options;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	
	
	
	
	
}
