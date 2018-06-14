package ar.com.bia.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection ="projects" )
public class ProjectDoc {

	@Field("_id")
	private ObjectId id;
	
	@Field("user_id")
	private ObjectId userId;
	private String name;
	private String description;
	private List<LinkDoc> links;
	
	
	public ObjectId getUserId() {
		return userId;
	}
	public void setUserId(ObjectId userId) {
		this.userId = userId;
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
	
	
	public List<LinkDoc> getLinks() {
		return links;
	}
	public void setLinks(List<LinkDoc> links) {
		this.links = links;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getCompositeId() {
		return id.toString();
	}
	
	
}
