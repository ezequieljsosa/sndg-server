package ar.com.bia.backend.dao.impl;

import ar.com.bia.backend.dao.CompoundRepository;
import ar.com.bia.backend.dao.StructureRepository;
import ar.com.bia.entity.CompoundDoc;
import ar.com.bia.entity.CompoundDoc;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class CompoundRepositoryImpl implements CompoundRepository {

	@Autowired
	private MongoOperations mongoTemplate;

	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public CompoundDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, CompoundDoc.class);
	}
	
	
	public CompoundDoc findByName(String name) {
		Assert.notNull(name, "The given name must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("name").is(name)),
				CompoundDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)),
				CompoundDoc.class) != null;
	}

	@Override
	public Iterable<CompoundDoc> findAll() {

		return mongoTemplate.findAll(CompoundDoc.class);
	}

	public long count(Query query) {
		return mongoTemplate.count(query, CompoundDoc.class);
	}

	@Override
	public long count() {
		String collectionName = mongoTemplate
				.getCollectionName(CompoundDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), CompoundDoc.class);
	}

	public void delete(Query query) {
		String collectionName = mongoTemplate
				.getCollectionName(CompoundDoc.class);
		mongoTemplate.remove(query, collectionName);
	}

	@Override
	public void delete(CompoundDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getId().toString());
	}

	@Override
	public void delete(Iterable<? extends CompoundDoc> entities) {
		for (CompoundDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate
				.getCollectionName(CompoundDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}

	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return Criteria.where("_id").is(id);
	}

	@Override
	public Iterable<CompoundDoc> findAll(Sort sort) {
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<CompoundDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<CompoundDoc> list = this.findAll(new Query().with(pageable));

		return new PageImpl<CompoundDoc>(list, pageable, count);
	}

	public List<CompoundDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		
		return mongoTemplate.find(query, CompoundDoc.class);
	}

	@Override
	public <S extends CompoundDoc> S save(S entity) {
		this.mongoTemplate.save(entity);

		return entity;
	}

	@Override
	public <S extends CompoundDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<CompoundDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

}
