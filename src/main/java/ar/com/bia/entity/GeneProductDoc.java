package ar.com.bia.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.dto.druggability.Scoreable;

@Document(collection = "proteins")
public class GeneProductDoc extends Sequence implements SeqFeature, Scoreable {

	private Location location;
	private String gene;
	private String status;
	private String identifier;
	private String type;
	private String organism;
	private List<String> dbxrefs;
	private Map<String, ExpressionData> expression;
	private List<String> structures;

	private List<String> keywords;
	private Map<String,Object> search;
	private List<ReactionDoc> reactions;
	
	

	private List<Map<String, String>> properties;
	
	private Double score = 0.0;
	private List<String> alias;
	
	
	public GeneProductDoc() {
		super();
		this.search = new HashMap<String,Object>();
	}

	
	
	public Map<String, Object> getSearch() {
		return search;
	}



	public void setSearch(Map<String, Object> search) {
		this.search = search;
	}



	@JsonProperty
	public String strLocus() {
		if (this.location == null)
			return "?";
		return this.location.toString();
	}

	public Map<String, ExpressionData> getExpression() {
		return expression;
	}

	public void setExpression(Map<String, ExpressionData> expression) {
		this.expression = expression;
	}

	public String getType() {
		return type;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
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

	public void initializeSize() {
		if (this.getLocus() != null) {
			this.setSize(new Size(this.getLocus().getEnd() - this.getLocus().getStart() + 1, "aa"));
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getStructures() {
		return structures;
	}

	public void setStructures(List<String> structures) {
		this.structures = structures;
	}

	public List<String> getDbxrefs() {
		return dbxrefs;
	}

	public void setDbxrefs(List<String> dbxrefs) {
		this.dbxrefs = dbxrefs;
	}

	

	

	public Location getLocus() {
		return this.location;
	}

	public Location getLocation() {
		return this.location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public void addKeyWord(String keyword) {
		this.initializeKeywordsIfNull();
		this.keywords.add(keyword);
	}

	public void initializeKeywordsIfNull() {
		if (null == this.keywords) {
			this.keywords = new ArrayList<String>();
		}
	}

	public ChangeEmbedDoc toVariant(String strain) {
		ChangeEmbedDoc variant = new ChangeEmbedDoc();

		variant.setChange("");
		variant.setPosition("");
		variant.setRef(strain);
		variant.setType("Reference");

		return variant;
	}

	
	
	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
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



	public List<ReactionDoc> getReactions() {
		return reactions;
	}

	public void setReactions(List<ReactionDoc> reactions) {
		this.reactions = reactions;
	}

	public List<Map<String, String>> getProperties() {
		return properties;
	}

	public void setProperties(List<Map<String, String>> properties) {
		this.properties = properties;
	}

	public class ExpressionData {
		private Map<String, Integer> fpkms;
		private Float fpkm;
		private Boolean significant;
		private Float fold_change;

		public Map<String, Integer> getFpkms() {
			return fpkms;
		}

		public void setFpkms(Map<String, Integer> fpkms) {
			this.fpkms = fpkms;
		}

		public Boolean getSignificant() {
			return significant;
		}

		public void setSignificant(Boolean significant) {
			this.significant = significant;
		}

		public Float getFold_change() {
			return fold_change;
		}

		public void setFold_change(Float fold_change) {
			this.fold_change = fold_change;
		}

		public Float getFpkm() {
			return fpkm;
		}

		public void setFpkm(Float fpkm) {
			this.fpkm = fpkm;
		}

	}

	public List<Object> getGeneList() {
		String[] genes = this.getGene().split(",");
		for (int i = 0; i < genes.length; i++) {
			genes[i] = genes[i].replace("\"","").replace("\'","").replace("[","").replace("]","").trim();			
		}
		
		return Arrays.asList(genes).stream().collect(Collectors.toList());
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}



	public List<String> getAlias() {
		return alias;
	}



	public void setAlias(List<String> alias) {
		this.alias = alias;
	}



	@Override
	public void updateScore(List<DruggabilityParam> scores) {
		SearchProtDoc spd = new SearchProtDoc();
		spd.setKeywords(this.keywords);
		spd.setGene(this.gene);
		spd.setSearch(this.search);
		spd.updateScore(scores);
		this.setScore(spd.getScore());
		
	}



	@Override
	public String gene() {		
		return this.getGene();
	}



	




}