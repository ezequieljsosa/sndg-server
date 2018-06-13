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
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import ar.com.bia.entity.ProjectDoc;

@Repository
public class ProjectRepositoryImpl implements PagingAndSortingRepository<ProjectDoc, String> {

	@Autowired
	private MongoOperations mongoTemplate;

	
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	

	@Override
	public ProjectDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, ProjectDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), ProjectDoc.class) != null;
	}

	@Override
	public Iterable<ProjectDoc> findAll() {
		
		return mongoTemplate.findAll(ProjectDoc.class);
	}

	public long count(Query query) {
		return mongoTemplate.count(query, ProjectDoc.class);
	}
	
	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(ProjectDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), ProjectDoc.class);
	}



	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(ProjectDoc.class);
		mongoTemplate.remove(query, collectionName);		
	}

	@Override
	public void delete(ProjectDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getCompositeId());
	}

	@Override
	public void delete(Iterable<? extends ProjectDoc> entities) {
		for (ProjectDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(ProjectDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}
	
	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return Criteria.where("_id").is(id);
	}
	
	
	public ProjectDoc findUser(String username) {		
		List<ProjectDoc> findAll = this.findAll(new Query(Criteria.where("username").is(username)));
		if(findAll.size() == 1)
			return findAll.get(0);
		if(findAll.size() > 1 ) throw new IllegalStateException("Repeated User");
		return null;
		
	}

	

	@Override
	public Iterable<ProjectDoc> findAll(Sort sort) {
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<ProjectDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<ProjectDoc> list = 
				this.findAll(new Query().with( pageable));

		return new PageImpl<ProjectDoc>(list, pageable, count);
	}
	
	
	public List<ProjectDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, ProjectDoc.class);
	}

	
	
	
	
	@Override
	public <S extends ProjectDoc> S save(S entity) {
		this.mongoTemplate.save(entity);

		return entity;
	}

	@Override
	public <S extends ProjectDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<ProjectDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

	public List<ProjectDoc> projects_from_user(String userId) {
		List<ProjectDoc> findAll = this.findAll(new Query(Criteria.where("user_id").is(userId)));		
		return findAll;
	}

}
