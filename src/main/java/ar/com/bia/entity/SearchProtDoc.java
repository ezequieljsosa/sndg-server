package ar.com.bia.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.dto.druggability.Scoreable;

@Document(collection = "proteins")
public class SearchProtDoc implements Scoreable {

	private String id;
	private String gene;
	private String name;
	private Map<String, Object> search;
	private Collection<String> keywords;
	private double score = 0.0;

	private Size size;
	private List<ReactionDoc> reactions;
	private String description;
	private String organism;

	public SearchProtDoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public SearchProtDoc(DBObject dbObject, MongoOperations mongoTemplate) {
		this.id = ((ObjectId) dbObject.get("_id")).toString();
		this.gene = dbObject.get("gene").toString();
		this.organism = dbObject.get("organism").toString();
		this.name = dbObject.get("name").toString();

		
		this.size = mongoTemplate.getConverter().read(Size.class,(DBObject) dbObject.get("size"))  ;
		this.size.setUnit("aa");
		this.description = dbObject.get("description").toString();
		BasicDBList basicDBList = (BasicDBList) dbObject.get("reactions");
		this.reactions = new ArrayList<>();
		basicDBList.stream().forEach(x -> {
			this.reactions.add(mongoTemplate.getConverter().read(ReactionDoc.class,(DBObject) x));
		});

		if (dbObject.containsField("search")) {
			this.search = ((DBObject) dbObject.get("search")).toMap();
		} else {
			this.search = new HashMap<String, Object>();
		}

		this.keywords = ((BasicDBList) dbObject.get("keywords")).stream().map(p -> p.toString())
				.collect(Collectors.toList());
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public Map<String, Object> getSearch() {
		return search;
	}

	public void setSearch(Map<String, Object> search) {
		this.search = search;
	}

	public Collection<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Collection<String> keywords) {
		this.keywords = keywords;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public List<ReactionDoc> getReactions() {
		return reactions;
	}

	public void setReactions(List<ReactionDoc> reactions) {
		this.reactions = reactions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void updateScore(List<DruggabilityParam> scores) {
		double calculatedScore = scores.stream().mapToDouble(p -> {
			return p.score(this.getKeywords(), this.getSearch());
		}).sum();

		this.setScore(calculatedScore);
	}

	@Override
	public String gene() {
		return this.getGene();
	}

}
