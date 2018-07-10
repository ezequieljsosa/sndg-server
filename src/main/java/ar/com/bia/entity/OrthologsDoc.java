package ar.com.bia.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "orthologs")
public class OrthologsDoc {

	@Id
	private ObjectId id;
	private String locus_tag;
	private String gene_name;
	private ObjectId comparative_analysis_id;
	private List<GeneOrthologDoc> genes;
	
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getLocusTag() {
		return locus_tag;
	}
	
	public void setLocusTag(String locus_tag) {
		this.locus_tag = locus_tag;
	}
	
	public String getGeneName() {
		return gene_name;
	}
	
	public void setGeneName(String gene_name) {
		this.gene_name = gene_name;
	}
	
	public ObjectId getCompartiveAnalisisId() {
		return comparative_analysis_id;
	}
	
	public void setCompartiveAnalisisId(ObjectId comparative_analysis_id) {
		this.comparative_analysis_id = comparative_analysis_id;
	}
	
	
	public List<GeneOrthologDoc> getGenes() {
		return this.genes;
	}

	public void setGenes(List<GeneOrthologDoc> genes) {
		this.genes = genes;
	}
	
}
