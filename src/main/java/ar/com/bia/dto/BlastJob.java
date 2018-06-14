package ar.com.bia.dto;

import ar.com.bia.entity.JobDoc;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jobs")
public class BlastJob extends JobDoc {
	
	private BlastParameters patameters;
	
	
	
	
	public BlastJob() {
		super();
		this.setType("blast");
	}
	public BlastParameters getPatameters() {
		return patameters;
	}
	public void setPatameters(BlastParameters patameters) {
		this.patameters = patameters;
	}
	
	
}




