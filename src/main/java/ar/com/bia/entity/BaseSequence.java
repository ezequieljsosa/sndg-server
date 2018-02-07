package ar.com.bia.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseSequence<F extends SeqFeature> {

	/**
	 * Atributo "legible" que identifica univocamente a la secuencia
	 */
	public static final String NAME = "gene";

	@Id
	private String id;

	private String name;
	private String description;
	@Field("seq")
	private String sequence;

	private List<String> ontologies;

	private List<F> features;

	private List<VariantEmbedDoc> variants;

	@Field("seq_collection_id")
	private ObjectId seqCollectionId;

	private Size size;

	

	public BaseSequence() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompositeId() {
		return id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getOntologies() {
		return ontologies;
	}

	public void setOntologies(List<String> ontologies) {
		this.ontologies = ontologies;
	}

	@JsonProperty()
	public String strCollectionId() {

		return this.seqCollectionId.toStringMongod();

	}

	public ObjectId getSeqCollectionId() {
		return seqCollectionId;
	}

	public void setSeqCollectionId(ObjectId seqCollectionId) {
		this.seqCollectionId = seqCollectionId;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public List<F> getFeatures() {
		return features;
	}

	public void setFeatures(List<F> features) {
		this.features = features;
	}

	
	
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	

	public List<VariantEmbedDoc> getVariants() {
		return variants;
	}

	public void setVariants(List<VariantEmbedDoc> variants) {
		this.variants = variants;
	}

	public void addVariant(VariantEmbedDoc variant) {
		if (this.getVariants() == null) {
			this.setVariants(new ArrayList<VariantEmbedDoc>());
		}
		this.getVariants().add(variant);
	}

	public void addFeature(F gene) {
		if (this.getFeatures() == null) {
			this.setFeatures(new ArrayList<F>());
		}
		this.getFeatures().add(gene);
	}
	
}
