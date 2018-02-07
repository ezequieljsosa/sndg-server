package ar.com.bia.backend.dao;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mongodb.BasicDBObject;

import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.entity.GeneProductDoc;

public interface GeneProductDocumentRepository extends PagingAndSortingRepository<GeneProductDoc, String> {

	GeneProductDoc findTerm(String gene);

	long count(Query query);

	List<GeneProductDoc> findAll(Query query);

	GeneProductDoc findByName(String symbol);

	List<GeneProductDoc> findBySymbolList(
			List<String> proteinRepository);

	GeneProductDoc findByGene(String locusTag);
	GeneProductDoc findByGene(String organism, String locusTag);

	BasicDBObject addPropToProtein(Map<String, Object> props);

	BasicDBObject removePropFromProtein( String uploader,String property);


	BasicDBObject addSearchToProtein(String _type,  String property ,Object value) ;

	void removePropFromAllProteins(String collection, String property, String uploader);

	GeneProductDoc findByID(ObjectId id);
	
	List<GeneProductDoc> scored(List<DruggabilityParam> dps,String organism,int size,int skip);

	GeneProductDoc findByAlias(String alias);
	
}
