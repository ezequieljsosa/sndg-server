package ar.com.bia.entity.var;

import ar.com.bia.entity.SeqFeatureEmbedDoc;
import org.bson.types.ObjectId;

import java.util.List;

public class Allele {

	private List<SampleAllele> samples;
	private String alt;
	private List<String> variant_type;
	private String aa_ref;
	private String aa_alt;
	private int aa_pos;
	private ObjectId prot_ref;
	private SeqFeatureEmbedDoc feature; 
	
	
	public List<SampleAllele> getSamples() {
		return samples;
	}
	public void setSamples(List<SampleAllele> samples) {
		this.samples = samples;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public List<String> getVariant_type() {
		return variant_type;
	}
	public void setVariant_type(List<String> variant_type) {
		this.variant_type = variant_type;
	}
	public String getAa_ref() {
		return aa_ref;
	}
	public void setAa_ref(String aa_ref) {
		this.aa_ref = aa_ref;
	}
	public String getAa_alt() {
		return aa_alt;
	}
	public void setAa_alt(String aa_alt) {
		this.aa_alt = aa_alt;
	}
	public int getAa_pos() {
		return aa_pos;
	}
	public void setAa_pos(int aa_pos) {
		this.aa_pos = aa_pos;
	}
	public ObjectId getProt_ref() {
		return prot_ref;
	}
	public void setProt_ref(ObjectId prot_ref) {
		this.prot_ref = prot_ref;
	}
	public SeqFeatureEmbedDoc getFeature() {
		return feature;
	}
	public void setFeature(SeqFeatureEmbedDoc feature) {
		this.feature = feature;
	}
	
	
	
	
}
