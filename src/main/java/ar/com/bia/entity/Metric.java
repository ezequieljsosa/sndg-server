package ar.com.bia.entity;

import java.util.List;

public class Metric {

	private String name;
	private String description;
	private float value;
	private List<Float> values;
	private List<String> labels;
	
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
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public List<Float> getValues() {
		return values;
	}
	public void setValues(List<Float> values) {
		this.values = values;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	
	
}
