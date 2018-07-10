package ar.com.bia.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "var_col_ont_idx")
public class VarOrgOntIndexElement {

	private String seq_collection_name;
	private String term;
	private String name;
	private List<String> keywords;
	private Integer count;
	private Integer order;
	private String ontology;
	private String database;

	public VarOrgOntIndexElement() {
			super();
		}

	public String getSeq_collection_name() {
		return seq_collection_name;
	}

	public void setSeq_collection_name(String seq_collection_name) {
		this.seq_collection_name = seq_collection_name;
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

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getOntology() {
		return this.ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

}
