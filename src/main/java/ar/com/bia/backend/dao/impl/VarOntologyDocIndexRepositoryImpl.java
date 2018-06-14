package ar.com.bia.backend.dao.impl;

import ar.com.bia.entity.VarOrgOntIndexElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class VarOntologyDocIndexRepositoryImpl {
	@Autowired
	private MongoOperations mongoTemplate;

	
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	
	
	public List<VarOrgOntIndexElement> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, VarOrgOntIndexElement.class);
	}

	
	public List<VarOrgOntIndexElement> findAllNorRep(Criteria criteria) {
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
		
		return mongoTemplate.aggregate(agg, VarOrgOntIndexElement.class, VarOrgOntIndexElement.class).getMappedResults();
	}
	
}
