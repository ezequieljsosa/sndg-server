package ar.com.bia.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author leo
 * 
 */
@Document(collection = "comparative_analysis")
public class ComparativeAnalysisDoc {

	@Id
	private ObjectId id;
	private List<String> organisms;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public List<String> getOrganisms() {
		return this.organisms;
	}

	public void setOrganisms(List<String> organisms) {
		this.organisms = organisms;
	}


}
