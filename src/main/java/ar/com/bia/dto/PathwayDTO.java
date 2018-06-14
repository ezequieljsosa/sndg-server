package ar.com.bia.dto;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.nio.channels.IllegalSelectorException;
import java.util.List;
import java.util.Map;

/**
 * @author eze
 *
 */
public class PathwayDTO implements Cloneable {

	private String term;
	private String name;
	private int count;
	@Field("properties")
	private Map<String,Object> properties2 ;
	@Transient
	private List<Map<String,Object>> properties ;
	@Transient
	private double score;
	
	
	public PathwayDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	

	public List<Map<String, Object>> getProperties() {
		return properties;
	}

	public void setProperties(List<Map<String, Object>> properties) {
		this.properties = properties;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	
	public Map<String, Object> getProperties2() {
		return properties2;
	}

	public void setProperties2(Map<String, Object> properties2) {
		this.properties2 = properties2;
	}

	public PathwayDTO clone(){
		try {
			return (PathwayDTO) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalSelectorException();
		}
	}
	
	
}
