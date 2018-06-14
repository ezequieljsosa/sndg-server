package ar.com.bia.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Document(collection = "users")
public class UserDoc implements Principal {

	public static final String publicUserName = "demo";

	public static ObjectId publicUserId = new ObjectId("563b9440b1b50423d1fd1fee");
	
	@Id
	private String id;
	
	private ObjectId authId;
	
	private String username;
	private String password;
	
	private String email;
	private String institutions;
	private String name;
	
	private Map<String,Object> pre_loaded;
	
	@Field(value="seq_collections")
	private List<ObjectId> seqCollections;
	
	@Field(value="menues")
	private List<MenuDoc> menues;
	
	@Field(value="links")
	private List<LinkDoc> links;
	
	private int projectCount;
	
	//TODO las queries deberian ser a nivel proyecto
	@Field("auth_queries")
	private List<Map<String,String>> queries;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompositeId() {		
		return getId().toString();
	}
	public List<ObjectId> getSeqCollections() {
		return seqCollections;
	}
	public void setSeqCollections(List<ObjectId> seqCollections) {
		this.seqCollections = seqCollections;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInstitutions() {
		return institutions;
	}
	public void setInstitutions(String institutions) {
		this.institutions = institutions;
	}
	public List<MenuDoc> getMenues() {
		return menues;
	}
	public void setMenues(List<MenuDoc> menues) {
		this.menues = menues;
	}
	public List<LinkDoc> getLinks() {
		return links;
	}
	public void setLinks(List<LinkDoc> links) {
		this.links = links;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProjectCount() {
		return projectCount;
	}
	public void setProjectCount(int projectCount) {
		this.projectCount = projectCount;
	}
	public List<Map<String, String>> getQueries() {
		return queries;
	}
	public void setQueries(List<Map<String, String>> queries) {
		this.queries = queries;
	}
	public String getAuthId() {
		return (authId != null) ? authId.toString() : id;
	}
	public void setAuthId(ObjectId authId) {
		this.authId = authId;
	}
	public Map<String, Object> getPre_loaded() {
		return pre_loaded;
	}
	public void setPre_loaded(Map<String, Object> pre_loaded) {
		this.pre_loaded = pre_loaded;
	}
	
	
	
	
	
	
}
