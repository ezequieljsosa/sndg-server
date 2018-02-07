package ar.com.bia.entity.druggability;

import java.util.List;

public class SeqColDruggabilityParam {

	private String name;	
	private String description;
	private String type;	
	private String target;
	private String uploader;
	private List<String> options;
	private String org_name;
	
	private String defaultGroupOperation;
	private String defaultOperation;
	private String defaultValue;
	
	
	
	public SeqColDruggabilityParam() {
		super();
	
	}
	
	
	
	public SeqColDruggabilityParam(String name, String description, String type, String target, List<String> options) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.target = target;
		this.options = options;
	}



	public String getOrg_name() {
		return org_name;
	}



	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}



	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
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
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}



	public String getDefaultGroupOperation() {
		return defaultGroupOperation;
	}



	public void setDefaultGroupOperation(String defaultGroupOperation) {
		this.defaultGroupOperation = defaultGroupOperation;
	}



	public String getDefaultOperation() {
		return defaultOperation;
	}



	public void setDefaultOperation(String defaultOperation) {
		this.defaultOperation = defaultOperation;
	}



	public String getDefaultValue() {
		return defaultValue;
	}



	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
	
	
	
}
