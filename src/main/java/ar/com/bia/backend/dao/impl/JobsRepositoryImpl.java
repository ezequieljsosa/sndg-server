package ar.com.bia.backend.dao.impl;

import ar.com.bia.entity.JobDoc;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class JobsRepositoryImpl implements PagingAndSortingRepository<JobDoc, String>   {
	
	@Autowired
	private MongoOperations mongoTemplate;

	
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	

	@Override
	public JobDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(new ObjectId(id), JobDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), JobDoc.class) != null;
	}

	@Override
	public Iterable<JobDoc> findAll() {
		
		return mongoTemplate.findAll(JobDoc.class);
	}

	public long count(Query query) {
		return mongoTemplate.count(query, JobDoc.class);
	}
	
	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(JobDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), JobDoc.class);
	}



	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(JobDoc.class);
		mongoTemplate.remove(query, collectionName);		
	}

	@Override
	public void delete(JobDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getId());
	}

	@Override
	public void delete(Iterable<? extends JobDoc> entities) {
		for (JobDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(JobDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}
	
	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return Criteria.where("_id").is(id);
	}
	
	
	public JobDoc findUser(String username) {		
		List<JobDoc> findAll = this.findAll(new Query(Criteria.where("username").is(username)));
		if(findAll.size() == 1)
			return findAll.get(0);
		if(findAll.size() > 1 ) throw new IllegalStateException("Repeated User");
		return null;
		
	}

	

	@Override
	public Iterable<JobDoc> findAll(Sort sort) {
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<JobDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<JobDoc> list = 
				this.findAll(new Query().with( pageable));

		return new PageImpl<JobDoc>(list, pageable, count);
	}
	
	
	public List<JobDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, JobDoc.class);
	}

	
	
	
	
	@Override
	public <S extends JobDoc> S save(S entity) {
		this.mongoTemplate.save(entity);

		return entity;
	}

	@Override
	public <S extends JobDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<JobDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}
	
}
