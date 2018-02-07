package ar.com.bia.backend.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import ar.com.bia.backend.dao.GODocumentRepository;
import ar.com.bia.entity.OntologyDoc;

@Repository
public class GODocumentRepositoryImpl implements GODocumentRepository {
	
	@Autowired
	private MongoOperations mongoTemplate;
	
	private Set<String> slimSet;
	
	
	public GODocumentRepositoryImpl(MongoTemplate mongoTemplate) {
		super();
		this.mongoTemplate = mongoTemplate;
	}

	public GODocumentRepositoryImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterable<OntologyDoc> findAll(Sort sort) {
		return this.findAll(new Query().with( sort));
	}

	@Override
	public Page<OntologyDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<OntologyDoc> list = 
				this.findAll(new Query().with( pageable));

		return new PageImpl<OntologyDoc>(list, pageable, count);
	}

	

	@Override
	public OntologyDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findById(id, OntologyDoc.class);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), OntologyDoc.class) != null;
	}

	@Override
	public Iterable<OntologyDoc> findAll() {
		
		return mongoTemplate.findAll(OntologyDoc.class);
	}

	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, OntologyDoc.class);
	}
	
	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(OntologyDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), OntologyDoc.class);
	}



	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(OntologyDoc.class);
		mongoTemplate.remove(query, collectionName);		
	}

	@Override
	public void delete(OntologyDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getCompositeId());
	}

	@Override
	public void delete(Iterable<? extends OntologyDoc> entities) {
		for (OntologyDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(OntologyDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}

	@Override
	public OntologyDoc findTerm(String ontology,String goTerm,Boolean slim) {		
		Criteria criteria = Criteria.where("ontology").is(ontology).and("term").is(goTerm);
		if(slim && "go".equals(ontology)){
			criteria.and("databases").is("generic") ;
		}
		List<OntologyDoc> findAll = this.findAll(new Query(criteria ));
		if(findAll.size() == 1)
			return findAll.get(0);
		if(findAll.size() > 1 ) throw new IllegalStateException("Hay mas de un termino con el nombre:" + goTerm);
		return null;
		
	}
	
	public List<String> succesors(String ontology,String child) {
		
		
		DBCursor find = this.getMongoOperations().getCollection("ontologies").find(
				new BasicDBObject("ontology",ontology).append("term", child),
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
	public List<OntologyDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, OntologyDoc.class);
	}

	public List<String> removeInferedTerms(List<String> terms) {
		if (terms.size() <= 1)
			return terms;
		List<String> termsWithoutInferred = new ArrayList<String>(terms);
		Set<OntologyDoc> inferedTerms = new HashSet<OntologyDoc>();
		for (String term : terms) {

			Criteria inferedTermCriteria = Criteria.where("subclases").is(term)
					.and("term").in(terms);
			List<OntologyDoc> inferedTermsTmp = this.findAll(new Query(inferedTermCriteria));

			inferedTerms.addAll(inferedTermsTmp);

		}
		for (OntologyDoc goDocument : inferedTerms) {
			String inferedTerm = goDocument.getTerm();
			if (termsWithoutInferred.contains(inferedTerm)) {
				termsWithoutInferred.remove(inferedTerm);
			}
		}

		return termsWithoutInferred;
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
	public <S extends OntologyDoc> S save(S entity) {
		this.mongoTemplate.save(entity);
		return entity;
	}

	@Override
	public <S extends OntologyDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public Iterable<OntologyDoc> findAll(Iterable<String> ids) {
		throw new RuntimeException("No implementado");
	}

	@Override
	public Set<String> slimSet() {
		if (this.slimSet == null ){
			List<OntologyDoc> gos = this.findAll(new Query(Criteria.where("ontology").is("go").and("slims").is("generic")));
			this.slimSet = new HashSet<String>();
			for (OntologyDoc ontologyDoc : gos) {
				this.slimSet.add(ontologyDoc.getTerm());
			}
			
		}
		return this.slimSet;
	}

	
	
}
