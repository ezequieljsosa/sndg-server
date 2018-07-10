package ar.com.bia.dto;

import ar.com.bia.entity.JobDoc;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jobs")
public class MSAJob extends JobDoc {

	public MSAJob() {
		super();
		this.setType("msa");		
	}

}
