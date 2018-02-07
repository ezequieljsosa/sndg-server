package ar.com.bia.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import ar.com.bia.entity.JobDoc;

@Document(collection = "jobs")
public class MSAJob extends JobDoc {

	public MSAJob() {
		super();
		this.setType("msa");		
	}

}
