package ar.com.bia.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

//TODO revisar que vuelva a depender de abstractk keyed
@Document(collection = "ontologies")
public class OntologyDoc {

	@Id
	private String compositeId;
	
	public String getCompositeId() {
		return compositeId;
	}

	public void setCompositeId(String compositeId) {
		this.compositeId = compositeId;
	}

	
	
	private String ontology;
	private String term;
	private String name;
	private List<String> children;
	private List<String> successors;
	
	
	public OntologyDoc() {
		super();
		this.children = new ArrayList<String>();
		this.successors = new ArrayList<String>();
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
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

	public void setChildren(List<String> childTerms) {
		this.children = childTerms;
	}

	
	public String toString() {
		return  ontology + " Doc [" + term + ":  " + name + "]";
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	public List<String> getSuccessors() {
		return successors;
	}

	public void setSuccessors(List<String> successors) {
		this.successors = successors;
	}

	
	
	
	
	
	
	
}
