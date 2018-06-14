package ar.com.bia.backend.dao.impl;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.Sequence;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.*;

@Repository
public class GeneProductDocumentRepositoryImpl implements GeneProductDocumentRepository {
	
	@Autowired
	private MongoOperations mongoTemplate;
	
	/**
	 * Parametro para realizar pruebas, solo se utiliza en los tests. En produccion deberia ser null.
	 */
	private String collectionName;
	
	public GeneProductDocumentRepositoryImpl(String collectionName, MongoTemplate mongoTemplate) {
		super();
		this.collectionName = collectionName;
		this.mongoTemplate = mongoTemplate;
	}

	public GeneProductDocumentRepositoryImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterable<GeneProductDoc> findAll(Sort sort) {
		return this.findAll(new Query().with( sort));
	}

	@Override
	public Page<GeneProductDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<GeneProductDoc> list = 
				this.findAll(new Query().with( pageable));

		return new PageImpl<GeneProductDoc>(list, pageable, count);
	}

	

	@Override
	public GeneProductDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, GeneProductDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), GeneProductDoc.class) != null;
	}

	@Override
	public Iterable<GeneProductDoc> findAll() {
		
		return mongoTemplate.findAll(GeneProductDoc.class);
	}

	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, GeneProductDoc.class);
	}
	
	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(GeneProductDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), GeneProductDoc.class);
	}



	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(GeneProductDoc.class);
		mongoTemplate.remove(query, collectionName);		
	}

	@Override
	public void delete(GeneProductDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getCompositeId());
	}

	@Override
	public void delete(Iterable<? extends GeneProductDoc> entities) {
		for (GeneProductDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(GeneProductDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}

	@Override
	public GeneProductDoc findTerm(String goTerm) {		
		List<GeneProductDoc> findAll = this.findAll(new Query(Criteria.where("term").is(goTerm)));
		if(findAll.size() == 1)
			return findAll.get(0);
		if(findAll.size() > 1 ) throw new IllegalStateException("Hay mas de un termino con el nombre:" + goTerm);
		return null;
		
	}
	
	
	
	public List<String> succesors(String child) {
		
		
		DBCursor find = this.getMongoOperations().getCollection(collectionName).find(
				new BasicDBObject("term", child),
				new BasicDBObject("successors", "1"));

		Iterator<DBObject> iterator = find.iterator();
		List<String> terms = new ArrayList<String>();
		terms.add(child);
		if (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			List<String> childrenSuccessors = (List<String>) ((DBObject) (iterator
					.next())).get("successors");
			terms.addAll(childrenSuccessors);
		}
		return terms;
	}

	

	@Override
	public List<GeneProductDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, GeneProductDoc.class);
	}

	
	/**
	 * @return the mongoOperations
	 */
	public MongoOperations getMongoOperations() {
		return mongoTemplate;
	}

	/**
	 * @param mongoOperations
	 *            the mongoOperations to set
	 */
	public void setMongoOperations(MongoOperations mongoOperations) {
		this.mongoTemplate = mongoOperations;
	}

	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return Criteria.where("_id").is(id);
	}

	@Override
	public GeneProductDoc findByName(String symbol) {
		Assert.hasText(symbol, "The given symbol must not be empty or null!");
		return mongoTemplate.findOne(new Query(Criteria.where(Sequence.NAME).is(symbol)), GeneProductDoc.class);
		
	}

	@Override
	public GeneProductDoc findByGene(String organism, String locusTag) {
		Assert.hasText(organism, "The given organism must not be empty or null!");
		Assert.hasText(locusTag, "The given locusTag must not be empty or null!");
		return mongoTemplate.findOne(new Query(Criteria.where(Sequence.NAME).is(locusTag).and("organism").is(organism)), GeneProductDoc.class);
	}
	
	@Override
	public List<GeneProductDoc> findBySymbolList(List<String> symbolList) {		
		return this.mongoTemplate.find(new Query(Criteria.where(Sequence.NAME).in(symbolList)), GeneProductDoc.class);
	}
	
	@Override
	public <S extends GeneProductDoc> S save(S entity) {
		this.mongoTemplate.save(entity);
		return entity;
	}

	@Override
	public <S extends GeneProductDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<GeneProductDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

	@Override
	public GeneProductDoc findByGene(String locusTag) {
		return mongoTemplate.findOne(new Query(Criteria.where("gene").is(locusTag)), GeneProductDoc.class);
		
	}
	
	@Override
	public GeneProductDoc findByAlias(String locusTag) {
		return mongoTemplate.findOne(new Query(Criteria.where("alias").is(locusTag)), GeneProductDoc.class);
		
	}

	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	@Override
	public BasicDBObject addPropToProtein(Map<String, Object> props) {
		return  new BasicDBObject("$push",new BasicDBObject("properties",props));		
//		this.getMongoTemplate().getCollection("proteins").update(new BasicDBObject("_id",new ObjectId(proteinId)) , push );
		
	}

	@Override
	public BasicDBObject removePropFromProtein( String uploader,String property) {
		BasicDBObject filter = new BasicDBObject("_type",uploader).append("property",property);
		BasicDBObject pull = new BasicDBObject("$pull",new BasicDBObject("properties",filter));	
		return pull;
//		this.getMongoTemplate().getCollection("proteins").update(new BasicDBObject("_id",new ObjectId(proteinId)) , pull );
		
	}	
	@Override
	public void removePropFromAllProteins(String collection, String property, String uploader) {
		BasicDBObject filter = new BasicDBObject("organism",collection).append("properties._type", uploader);
		BasicDBObject unset = new BasicDBObject("$unset",new BasicDBObject("properties.$."+ property,true));
		this.getMongoTemplate().getCollection("proteins").update(filter , unset ,false,true );
		filter = new BasicDBObject("organism",collection);
		unset = new BasicDBObject("$unset",new BasicDBObject("search."+  uploader +  "." + property,true));
		this.getMongoTemplate().getCollection("proteins").update(filter , unset,false,true );
		
	}	

	@Override
	public BasicDBObject addSearchToProtein( String _type, String property ,Object value) {		
		return new BasicDBObject("$set",new BasicDBObject("search." + _type + "." + property ,value));		
//		this.getMongoTemplate().getCollection("proteins").update(new BasicDBObject("_id",new ObjectId(proteinId)) , set );		
	}

	@Override
	public GeneProductDoc findByID(ObjectId id) {		
		return  this.getMongoTemplate().findOne(getIdQuery(id),GeneProductDoc.class);
	}
	
	
	public List<GeneProductDoc> scored(List<DruggabilityParam> dps,String organism,int size,int skip) {	
		String mapFunction = "function() {this.score = this.druggability * 2;  emit(this._id, this); }";
		String reduceFunction = "function(id, doc) { return doc.druggability;}";
		MapReduceOutput mapReduce = this.getMongoTemplate().getCollection("proteins").mapReduce( mapFunction, reduceFunction,"pepe",
				new BasicDBObject("organism",organism));
		DBCursor sort = mapReduce.getOutputCollection().find().sort(new BasicDBObject("score",1)).skip(skip).limit(size);
		
		
		List<GeneProductDoc>  result = new ArrayList<>();
		
		for (DBObject dbObject : sort) {
			DBObject object = (DBObject) dbObject.get("value");
			result.add( this.getMongoOperations().getConverter().read(GeneProductDoc.class,object ));
		}
		return  result;
	}

	
	
}
