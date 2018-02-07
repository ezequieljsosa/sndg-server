package ar.com.bia.backend.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import ar.com.bia.backend.dao.OntologyDocumentRepository;
import ar.com.bia.entity.OntologyTerm;

@Repository
public class OntologyDocumentRepositoryImpl implements OntologyDocumentRepository {
	@Autowired
	private MongoOperations mongoTemplate;

	
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	

	@Override
	public OntologyTerm findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, OntologyTerm.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), OntologyTerm.class) != null;
	}

	@Override
	public Iterable<OntologyTerm> findAll() {
		
		return mongoTemplate.findAll(OntologyTerm.class);
	}

	public long count(Query query) {
		return mongoTemplate.count(query, OntologyTerm.class);
	}
	
	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(OntologyTerm.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), OntologyTerm.class);
	}



	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(OntologyTerm.class);
		mongoTemplate.remove(query, collectionName);		
	}

	@Override
	public void delete(OntologyTerm entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void delete(Iterable<? extends OntologyTerm> entities) {
		for (OntologyTerm entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(OntologyTerm.class);
		mongoTemplate.remove(new Query(), collectionName);
	}
	
	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return Criteria.where("_id").is(id);
	}
	
	
	public OntologyTerm findUser(String username) {		
		List<OntologyTerm> findAll = this.findAll(new Query(Criteria.where("username").is(username)));
		if(findAll.size() == 1)
			return findAll.get(0);
		if(findAll.size() > 1 ) throw new IllegalStateException("Repeated User");
		return null;
		
	}

	

	@Override
	public Iterable<OntologyTerm> findAll(Sort sort) {
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<OntologyTerm> findAll(Pageable pageable) {
		Long count = this.count();
		List<OntologyTerm> list = 
				this.findAll(new Query().with( pageable));

		return new PageImpl<OntologyTerm>(list, pageable, count);
	}
	
	
	public List<OntologyTerm> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		DBCursor find = mongoTemplate.getCollection("ontologies").find(query.getQueryObject());
		
		List<DBObject> raw = new ArrayList<DBObject>();
		for (DBObject dbObject : find) {
			raw.add(dbObject);
		}
		List<OntologyTerm> result = new ArrayList<OntologyTerm>();
		for (DBObject dbObject : raw) {
			result.add(mongoTemplate.getConverter().read(OntologyTerm.class, dbObject));
		}
		
			
		return result; //mongoTemplate.find(query, OntologyTerm.class);
	}

	
	
	
	
	@Override
	public <S extends OntologyTerm> S save(S entity) {
		this.mongoTemplate.save(entity);

		return entity;
	}

	@Override
	public <S extends OntologyTerm> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<OntologyTerm> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

	public List<OntologyTerm> projects_from_user(ObjectId userId) {
		List<OntologyTerm> findAll = this.findAll(new Query(Criteria.where("user_id").is(userId)));		
		return findAll;
	}

}
