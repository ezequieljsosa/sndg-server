package ar.com.bia.backend.dao.impl;

import ar.com.bia.backend.dao.SeqCollectionRepository;
import ar.com.bia.entity.SeqCollectionDoc;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Repository
public class SeqCollectionRepositoryImpl implements SeqCollectionRepository {

	@Autowired
	private MongoOperations mongoTemplate;

	
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	public SeqCollectionDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, SeqCollectionDoc.class);
	}
	
	public List<SeqCollectionDoc> seqCollectionsFromOrganism(String organism) {
		Assert.hasText(organism, "The given organism must not be null or empty!");
		return mongoTemplate.find(new Query(Criteria.where("organism").is(organism)),SeqCollectionDoc.class);
	}
	
	public SeqCollectionDoc findByName(String name) {
		Assert.hasText(name, "The given organism must not be null or empty!");
		return mongoTemplate.findOne(new Query(Criteria.where("name").is(name)),SeqCollectionDoc.class);
	}
	
	public SeqCollectionDoc findByProject(String pid) {
		Assert.hasText(pid, "The given pid must not be null or empty!");
		try{
			return mongoTemplate.findOne(new Query(Criteria.where("strainProjects.id").is(new ObjectId(pid))),SeqCollectionDoc.class);
		} catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}


	}
	
	

	@Override
	public void drop() {
		this.mongoTemplate.dropCollection(SeqCollectionDoc.class);
		
	}

	@Override
	public void save(SeqCollectionDoc seqCollection) {
		this.mongoTemplate.save(seqCollection);
		
	}

	@Override
	public Map<String, String> idStrainMap(List<String> correctedStrains) {
		throw new RuntimeException("Not implemented");
//		Map<String, String> map = new HashMap<String,String>();
		//TODO Not implemented
//		List<SeqCollectionDoc> collections = this.mongoTemplate.find(new Query(Criteria.where(SeqCollectionDoc.STRAIN).in(correctedStrains)),
//				SeqCollectionDoc.class);
//		for (SeqCollectionDoc seqCollectionDoc : collections) {
//			map.put(seqCollectionDoc.getId(), seqCollectionDoc.getStrain());
//		}
//		return map;
	}

	@Override
	public void addDrugParam(String collectionName,String description,  String propName, String propType, String[] options) {
		
		BasicDBObject value = new BasicDBObject("name",propName).append("description",description).append("options", options);
		BasicDBObject set = new BasicDBObject("$push",new BasicDBObject("druggabilityParams.1." +    propType , value ));		
		BasicDBObject select = new BasicDBObject("name",collectionName);
		this.getMongoTemplate().getCollection("sequence_collection").update(select , set );
		
	}

	@Override
	public void addDrugParam(String collectionName,String description, String propName, String propType) {
		BasicDBObject value = new BasicDBObject("name",propName).append("description",description);
		BasicDBObject set = new BasicDBObject("$push",new BasicDBObject("druggabilityParams.1." +    propType , value ));		
		BasicDBObject select = new BasicDBObject("name",collectionName);
		this.getMongoTemplate().getCollection("sequence_collection").update(select , set );
		
	}

	@Override
	public void removeDrugParam(String collectionName, String propName, String propType) {
		BasicDBObject value = new BasicDBObject("name",propName);
		BasicDBObject pull = new BasicDBObject("$pull",new BasicDBObject("druggabilityParams.1." +    propType , value ));		
		BasicDBObject select = new BasicDBObject("name",collectionName).append("druggabilityParams.name", "protein");
		this.getMongoTemplate().getCollection("sequence_collection").update(select , pull );
		
	}
	
}
