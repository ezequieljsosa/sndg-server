package ar.com.bia.backend.dao.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ar.com.bia.entity.OrgOntIndexElement;

@Repository
public class OntologyDocIndexRepositoryImpl {
	@Autowired
	private MongoOperations mongoTemplate;

	
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	
	
	public List<OrgOntIndexElement> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, OrgOntIndexElement.class);
	}

	
	public List<OrgOntIndexElement> findAllNorRep(Criteria criteria) {
		if (criteria == null) {
			return Collections.emptyList();
		}
		
		Aggregation agg = newAggregation(
				match(criteria),
				group("term").first("name").as("name")
					.first("seq_collection_name").as("seq_collection_name")
					.first("keywords").as("keywords")
					.first("term").as("term")
					.first("ontology").as("ontology")
					.first("order").as("order"),
				sort(Direction.DESC,"order")
	 
			);
		
		return mongoTemplate.aggregate(agg, OrgOntIndexElement.class, OrgOntIndexElement.class).getMappedResults();
	}
	
}
