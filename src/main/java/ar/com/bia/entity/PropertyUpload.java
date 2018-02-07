package ar.com.bia.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;



public class PropertyUpload {

	private String uploader;
	private Date timestamp;
	private List<String> errors;
	private List<String> properties;
	
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public List<String> getProperties() {
		return properties;
	}
	public void setProperties(List<String> properties) {
		this.properties = properties;
	}
	
	@JsonProperty
	public String formatedTimestamp() {
	    return new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(this.getTimestamp());
	    
	}
	
	
}
