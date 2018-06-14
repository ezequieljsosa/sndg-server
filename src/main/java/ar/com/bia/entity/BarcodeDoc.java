package ar.com.bia.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "barcodes")
public class BarcodeDoc {

	private String processid;
	private Map<String,List<Map<String,String>>> sequences;
	private Map<String,Map<String,Map<String,String>>> taxonomy;
	
	private Map<String,String> specimen_desc;
	private Map<String,String> collection_event;
	private Map<String,String> specimen_identifiers;
	private Map<String,List<Map<String,Object>>> specimen_imagery;
	
	
	
	public Map<String, List<Map<String, Object>>> getSpecimen_imagery() {
		return specimen_imagery;
	}
	public void setSpecimen_imagery(Map<String, List<Map<String, Object>>> specimen_imagery) {
		this.specimen_imagery = specimen_imagery;
	}
	public String getProcessid() {
		return processid;
	}
	public void setProcessid(String processid) {
		this.processid = processid;
	}
	public Map<String, List<Map<String, String>>> getSequences() {
		return sequences;
	}
	public void setSequences(Map<String, List<Map<String, String>>> sequences) {
		this.sequences = sequences;
	}
	public Map<String, Map<String, Map<String, String>>> getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(Map<String, Map<String, Map<String, String>>> taxonomy) {
		this.taxonomy = taxonomy;
	}
	public Map<String, String> getSpecimen_desc() {
		return specimen_desc;
	}
	public void setSpecimen_desc(Map<String, String> specimen_desc) {
		this.specimen_desc = specimen_desc;
	}
	public Map<String, String> getCollection_event() {
		return collection_event;
	}
	public void setCollection_event(Map<String, String> collection_event) {
		this.collection_event = collection_event;
	}
	public Map<String, String> getSpecimen_identifiers() {
		return specimen_identifiers;
	}
	public void setSpecimen_identifiers(Map<String, String> specimen_identifiers) {
		this.specimen_identifiers = specimen_identifiers;
	}
	
	
	
}
