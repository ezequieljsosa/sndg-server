package ar.com.bia.backend.dao.impl;

import ar.com.bia.backend.dao.GeneDocumentRepository;
import ar.com.bia.entity.ContigDoc;
import ar.com.bia.entity.GeneDoc;
import com.mongodb.*;
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
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Repository
public class GeneDocumentRepositoryImpl implements GeneDocumentRepository {

	@Autowired
	private MongoOperations mongoTemplate;

	/**
	 * Parametro para realizar pruebas, solo se utiliza en los tests. En
	 * produccion deberia ser null.
	 */
	private String collectionName;

	public GeneDocumentRepositoryImpl(String collectionName,
			MongoTemplate mongoTemplate) {
		super();
		this.collectionName = collectionName;
		this.mongoTemplate = mongoTemplate;
	}

	public GeneDocumentRepositoryImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterable<GeneDoc> findAll(Sort sort) {
		return this.findAll(new Query().with(sort));
	}

	@Override
	public Page<GeneDoc> findAll(Pageable pageable) {
		Long count = this.count();
		List<GeneDoc> list = this.findAll(new Query().with(pageable));

		return new PageImpl<GeneDoc>(list, pageable, count);
	}

	@Override
	public GeneDoc findByName(String name) {
		return this.findBy("identifier",name);
	}
	
	@Override
	public GeneDoc findOne(String id) {
		Assert.notNull(id, "The given id must not be null!");
		ObjectId value = new ObjectId(id);
		return this.findBy("_id",value);
	}
	

	
	
	public GeneDoc findBy(String field,Object value) {
		
		BasicDBObject matchConting = new BasicDBObject("$match",
				new BasicDBObject("features." + field, value));
		BasicDBObject project = new BasicDBObject("$project",
				new BasicDBObject("features", 1).append("seq_collection_id", 1).append("name", 1) );
		
		BasicDBObject unwind = new BasicDBObject("$unwind", "$features");
		BasicDBObject matchFeature = new BasicDBObject("$match",
				new BasicDBObject("features." + field, value));

		BasicDBObject removeFeaturePrefix = new BasicDBObject("_id",
				"$features._id");
		removeFeaturePrefix.put("location", "$features.location");
		removeFeaturePrefix.put("name", "$features.identifier");
		removeFeaturePrefix.put("type", "$features.type");
		removeFeaturePrefix.put("size", "$features.size");
		removeFeaturePrefix.put("locus_tag", "$features.locus_tag");
		
		removeFeaturePrefix.put("description", "$features.description");
		
		removeFeaturePrefix.put("product_id", "$features.product_id");
		
		
		removeFeaturePrefix.put("reference", "$name");
	
		
		removeFeaturePrefix.put("seq_collection_id",
				"$seq_collection_id");

		BasicDBObject projectRemoveFeatureColPrefix = new BasicDBObject(
				"$project", removeFeaturePrefix);

		AggregationOutput aggregate = mongoTemplate.getCollection("contig_collection")
				.aggregate(matchConting, project, unwind, matchFeature,
						projectRemoveFeatureColPrefix);
		Iterator<DBObject> iterator = aggregate.results().iterator();
		if (iterator.hasNext()) {

			DBObject dbObject = iterator.next();
			 
			GeneDoc read = mongoTemplate.getConverter().read(GeneDoc.class, dbObject);
			read.getLocation().setReference(dbObject.get( "reference").toString());
			
			 
			BasicDBList substrParams =  new BasicDBList();
			substrParams.add("$seq");
			substrParams.add(read.getLocation().getStart());
			substrParams.add(read.getLocation().getEnd() - read.getLocation().getStart() );
			DBObject proj1 = new BasicDBObject("seq",new BasicDBObject("$substr",substrParams));
            proj1.put("bigseq",1);
			DBObject projectSeq = new BasicDBObject("$project",proj1);
			DBObject dbobjresult = mongoTemplate.getCollection("contig_collection")
					.aggregate(matchConting, projectSeq).results().iterator().next();
			if ( dbobjresult.containsField("bigseq")){
			    try {
                String seq = new String((byte[])dbobjresult.get("bigseq")).substring(read.getLocation().getStart(),
                        read.getLocation().getEnd() );
                read.setSeq(seq );
                } catch (Exception ex){
                    Object object = dbobjresult.get("seq");
                    read.setSeq(object.toString() );
                }
            } else {
                Object object = dbobjresult.get("seq");
                read.setSeq(object.toString() );
            }

			if(!dbObject.containsField("size")){
				read.setUnit("bp");
				read.initializeSize();
			}
			return read;
		}
		throw new NotFoundException("Gene not found:" + field + " -> " + value);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)),
				GeneDoc.class) != null;
	}

	@Override
	public Iterable<GeneDoc> findAll() {

		return mongoTemplate.findAll(GeneDoc.class);
	}

	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, GeneDoc.class);
	}

	@Override
	public long count() {
		String collectionName = mongoTemplate.getCollectionName(GeneDoc.class);
		return mongoTemplate.getCollection(collectionName).count();
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoTemplate.remove(getIdQuery(id), GeneDoc.class);
	}

	public void delete(Query query) {
		String collectionName = mongoTemplate.getCollectionName(GeneDoc.class);
		mongoTemplate.remove(query, collectionName);
	}

	@Override
	public void delete(GeneDoc entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entity.getId());
	}

	@Override
	public void delete(Iterable<? extends GeneDoc> entities) {
		for (GeneDoc entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		String collectionName = mongoTemplate.getCollectionName(GeneDoc.class);
		mongoTemplate.remove(new Query(), collectionName);
	}

	@Override
	public GeneDoc findTerm(String goTerm) {
		List<GeneDoc> findAll = this.findAll(new Query(Criteria.where("term")
				.is(goTerm)));
		if (findAll.size() == 1)
			return findAll.get(0);
		if (findAll.size() > 1)
			throw new IllegalStateException(
					"Hay mas de un termino con el nombre:" + goTerm);
		return null;

	}

	public List<String> succesors(String child) {

		DBCursor find = this
				.getMongoOperations()
				.getCollection(collectionName)
				.find(new BasicDBObject("term", child),
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
	public List<GeneDoc> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}
		return mongoTemplate.find(query, GeneDoc.class);
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

	public Long featuresFromGenome(String genomeId) {
		DBObject match = new BasicDBObject("$match", new BasicDBObject(
				"seq_collection_id", genomeId).append("features",
				new BasicDBObject("$exists", true)));
		DBObject unwind = new BasicDBObject("$unwind", "features");
		DBObject filter = new BasicDBObject("$match", new BasicDBObject(
				"features.type", "SO:0000996"));
		DBObject count = new BasicDBObject("$group", new BasicDBObject("_id",
				new BasicDBObject()).append("count", new BasicDBObject("$sum",
				1)));
		return Long.parseLong(this.mongoTemplate
				.getCollection(collectionName())
				.aggregate(match, unwind, filter, count).results().iterator()
				.next().get("count").toString());
	}

	private String collectionName() {
		return this.mongoTemplate.getCollectionName(GeneDoc.class);
	}

	public GeneDoc save(GeneDoc gene, ContigDoc contig) {
		if (gene.getId() == null) {
			gene.setId(new ObjectId().toStringMongod());
		}
		contig.addFeature(gene);
		this.mongoTemplate.save(contig);
		return gene;
	}

	@Override
	public Iterable<GeneDoc> findAll(Iterable<String> entities) {
		throw new IllegalStateException("method not allowed");
	}

	@Override
	public <S extends GeneDoc> S save(S arg0) {
		throw new IllegalStateException(
				"method not allowed, use save(GeneDoc entity,ContigDoc contig) instead");
	}

	@Override
	public <S extends GeneDoc> Iterable<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public GeneDoc findOrganismGene(String organism, List<Object> locusTags) {
		for (Object locusTag : locusTags) {
			try{
				return this.findBy("identifier", locusTag);	
			} catch (NotFoundException ex){
				System.out.println(locusTag.toString() + " not found in " + locusTag);
			}
			
		}
		throw new NotFoundException("Gene not found:" + locusTags.toString() + " -> " + organism);
	}

}
