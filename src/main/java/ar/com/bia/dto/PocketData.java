package ar.com.bia.dto;

import java.util.List;
import java.util.Map;

public class PocketData {

	private int number;
	private List<String> as_lines;
	private List<Integer> atoms;
	private Map<String,Double> properties;
	
	
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public List<String> getAs_lines() {
		return as_lines;
	}
	public void setAs_lines(List<String> as_lines) {
		this.as_lines = as_lines;
	}
	public List<Integer> getAtoms() {
		return atoms;
	}
	public void setAtoms(List<Integer> atoms) {
		this.atoms = atoms;
	}
	public Map<String, Double> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, Double> properties) {
		this.properties = properties;
	}

	
	
	
}
