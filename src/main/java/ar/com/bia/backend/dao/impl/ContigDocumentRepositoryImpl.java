package ar.com.bia.backend.dao.impl;

import ar.com.bia.backend.dao.ContigDocumentRepository;
import ar.com.bia.entity.ContigDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Repository
public class ContigDocumentRepositoryImpl implements ContigDocumentRepository {
	
	@Autowired
	private MongoOperations mongoTemplate;
	
	/**
	 * Parametro para realizar pruebas, solo se utiliza en los tests. En produccion deberia ser null.
	 */
	private String collectionName;
	
	public ContigDocumentRepositoryImpl(String collectionName, MongoTemplate mongoTemplate) {
		super();
		this.collectionName = collectionName;
		this.mongoTemplate = mongoTemplate;
	}

	public ContigDocumentRepositoryImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterable<ContigDoc> findAll(Sort sort) {
		
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<ContigDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<ContigDoc> list = 
				this.findAll(new Query().with(pageable));

		return new PageImpl<ContigDoc>(list, pageable, count);
	}

	

	@Override
	public ContigDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, ContigDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), ContigDoc.class) != null;
	}

	@Override
	public Iterable<ContigDoc> findAll() {
		
		return mongoTemplate.findAll(ContigDoc.class);
	}

	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, ContigDoc.class);
	}
	
	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(ContigDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), ContigDoc.class);
	}



	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(ContigDoc.class);
		mongoTemplate.remove(query, collectionName);		
	}

	@Override
	public void delete(ContigDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getCompositeId());
	}

	@Override
	public void delete(Iterable<? extends ContigDoc> entities) {
		for (ContigDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(ContigDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}

	@Override
	public ContigDoc findTerm(String goTerm) {		
		List<ContigDoc> findAll = this.findAll(new Query(Criteria.where("term").is(goTerm)));
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
	public List<ContigDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, ContigDoc.class);
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
	public <S extends ContigDoc> S save(S entity) {
		this.mongoTemplate.save(entity);

		return entity;
	}

	@Override
	public <S extends ContigDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<ContigDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

	

	
	
}
