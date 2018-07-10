package ar.com.bia.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


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
