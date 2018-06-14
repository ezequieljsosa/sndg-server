package ar.com.bia.backend.dao.impl;

import ar.com.bia.backend.dao.StructureRepository;
import ar.com.bia.entity.ProjectDoc;
import ar.com.bia.pdb.StructureDoc;
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
public class StructureRepositoryImpl implements StructureRepository {

	@Autowired()
	private MongoOperations mongoTemplateStruct;

	public MongoOperations getMongoTemplate() {
		return mongoTemplateStruct;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplateStruct = mongoTemplate;
	}

	@Override
	public StructureDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplateStruct.findById(id, StructureDoc.class);
	}
	
	
	public StructureDoc findByName(String name) {
		Assert.notNull(name, "The given name must not be null!");
		return mongoTemplateStruct.findOne(new Query(Criteria.where("name").is(name)),
				StructureDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplateStruct.findOne(new Query(Criteria.where("_id").is(id)),
				StructureDoc.class) != null;
	}

	@Override
	public Iterable<StructureDoc> findAll() {

		return mongoTemplateStruct.findAll(StructureDoc.class);
	}

	public long count(Query query) {
		return mongoTemplateStruct.count(query, ProjectDoc.class);
	}

	@Override
	public long count() {
		String collectionName = mongoTemplateStruct
				.getCollectionName(ProjectDoc.class);
		return mongoTemplateStruct.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplateStruct.remove(getIdQuery(id), ProjectDoc.class);
	}

	public void delete(Query query) {
		String collectionName = mongoTemplateStruct
				.getCollectionName(ProjectDoc.class);
		mongoTemplateStruct.remove(query, collectionName);
	}

	@Override
	public void delete(StructureDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getId().toString());
	}

	@Override
	public void delete(Iterable<? extends StructureDoc> entities) {
		for (StructureDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplateStruct
				.getCollectionName(ProjectDoc.class);
		mongoTemplateStruct.remove(new Query(), collectionName);
	}

	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return Criteria.where("_id").is(id);
	}

	@Override
	public Iterable<StructureDoc> findAll(Sort sort) {
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<StructureDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<StructureDoc> list = this.findAll(new Query().with(pageable));

		return new PageImpl<StructureDoc>(list, pageable, count);
	}

	public List<StructureDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		
		return mongoTemplateStruct.find(query, StructureDoc.class);
	}

	@Override
	public <S extends StructureDoc> S save(S entity) {
		this.mongoTemplateStruct.save(entity);

		return entity;
	}

	@Override
	public <S extends StructureDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<StructureDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

}
