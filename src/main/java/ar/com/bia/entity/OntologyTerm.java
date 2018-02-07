package ar.com.bia.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ontologies")
public class OntologyTerm {
	
	private String ontology;
	private String term;
	
	private String description;
	private String link;
	private String name;
	private List<String> children;
	private String database;
	private int order;
	
	
	
	public OntologyTerm(String ontology, String term, String name, String database, int order) {
		super();
		this.ontology = ontology;
		this.term = term;
		this.name = name;
		this.database = database;
		this.order = order;
	}
	public OntologyTerm() {
		super();
		this.description = "";
		this.children = new ArrayList<String>();
	}
	public String getOntology() {
		return ontology;
	}
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getChildren() {
		return children;
	}
	public void setChildren(List<String> children) {
		this.children = children;
	}
	
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "OntologyTerm [ontology=" + ontology + ", term=" + term + ", name=" + name + ", database=" + database
				+ "]";
	}
	
	
	
	
}
