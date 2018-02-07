package ar.com.bia.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import ar.com.bia.entity.aln.SimpleAligment;

/**
 * @author eze
 * 
 */
public class SeqFeatureEmbedDoc implements SeqFeature {

	@Id
	private String id;
	private String type;
	private String description;
	
	private Location location;
	private String locus_tag;
	private Size size;
		
	private String identifier;
	private String parent;
	

	
	private Map<String,String> qualifiers;
	private List<String> structures;
	private String unit;
	private SimpleAligment aln;
	private String source;
	

	
	
	public SeqFeatureEmbedDoc() {
		super();
		this.structures = new ArrayList<String>();
		this.unit = "aa";
		this.aln = new SimpleAligment();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty
	public String strLocus() {
		if (this.getSize() == null)
			this.initializeSize();
		if (this.location == null)
			return "?";
		return this.location.toString();
	}

	public void initializeSize() {
		if (this.getLocation() != null) {
			this.setSize(new Size(this.getLocation().getEnd()
					- this.getLocation().getStart() , this.getUnit()));
		}
	}

	
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@JsonProperty
	public String strSize() {
		if (this.getSize() == null)
			this.initializeSize();

		if (this.getSize() != null) {
			return this.getSize().toString();
		}
		return "?";
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	
	@Override
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Map<String, String> getQualifiers() {
		return qualifiers;
	}

	public void setQualifiers(Map<String, String> qualifiers) {
		this.qualifiers = qualifiers;
	}

	public List<String> getStructures() {
		return structures;
	}

	public void setStructures(List<String> structures) {
		this.structures = structures;
	}

	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getLocusTag() {
		return locus_tag;
	}

	public void setLocusTag(String locus_tag) {
		this.locus_tag = locus_tag;
	}

	public SimpleAligment getAln() {
		return aln;
	}

	public void setAln(SimpleAligment aln) {
		this.aln = aln;
	}

	

	
}
