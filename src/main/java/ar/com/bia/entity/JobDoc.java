package ar.com.bia.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection="jobs")
public class JobDoc {
	@Id
	private String id;
	private String status;
	private String type;
	private String user;
	private Date date;
	
	private List<Map<String,String>> result;

	
	
	
	public JobDoc() {
		super();
		this.setResult(new ArrayList<Map<String, String>>());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Map<String, String>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, String>> result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@JsonProperty
	public String dateTime(){
		if (this.date == null) return "";
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(this.date);
	}

	
	
	
	
	
}
