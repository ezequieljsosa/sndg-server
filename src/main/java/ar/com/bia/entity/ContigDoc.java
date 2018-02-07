package ar.com.bia.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contig_collection")
public class ContigDoc extends Sequence {
		
	private String type;
	private String status;
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
	
	
	
}
