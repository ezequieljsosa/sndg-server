package ar.com.bia.backend.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.UserDoc;

@Repository
public class UserRepositoryImpl implements PagingAndSortingRepository<UserDoc, String>   {
	
	@Autowired
	private MongoOperations mongoTemplate;

	
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	

	@Override
	public UserDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, UserDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), UserDoc.class) != null;
	}

	@Override
	public Iterable<UserDoc> findAll() {
		
		return mongoTemplate.findAll(UserDoc.class);
	}

	public long count(Query query) {
		return mongoTemplate.count(query, UserDoc.class);
	}
	
	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(UserDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), UserDoc.class);
	}



	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(UserDoc.class);
		mongoTemplate.remove(query, collectionName);		
	}

	@Override
	public void delete(UserDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getCompositeId());
	}

	@Override
	public void delete(Iterable<? extends UserDoc> entities) {
		for (UserDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(UserDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}
	
	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return Criteria.where("_id").is(id);
	}
	
	
	public UserDoc findUser(String username) {		
		List<UserDoc> findAll = this.findAll(new Query(Criteria.where("username").is(username)));
		if(findAll.size() == 1)
			return findAll.get(0);
		if(findAll.size() > 1 ) throw new IllegalStateException("Repeated User");
		return null;
		
	}

	

	@Override
	public Iterable<UserDoc> findAll(Sort sort) {
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<UserDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<UserDoc> list = 
				this.findAll(new Query().with( pageable));

		return new PageImpl<UserDoc>(list, pageable, count);
	}
	
	
	public List<UserDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, UserDoc.class);
	}

	
	
	
	
	@Override
	public <S extends UserDoc> S save(S entity) {
		this.mongoTemplate.save(entity);

		return entity;
	}

	@Override
	public <S extends UserDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<UserDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

	public List<String> organisms(UserDoc user) {
		ObjectId authId = user.getAuthId();
		Set<String> organisms = new HashSet<String>();
		List<SeqCollectionDoc> seqCollections = this.mongoTemplate.find(new Query(Criteria.where("auth").is(authId)), SeqCollectionDoc.class);
		for (SeqCollectionDoc seqCollectionDoc : seqCollections) {
			organisms.add( seqCollectionDoc.getName() + "|" + seqCollectionDoc.getOrganism());
		}
		ArrayList<String> organismsList = new ArrayList<String>(organisms);
		Collections.sort( organismsList );
		return organismsList;	}
	
}
