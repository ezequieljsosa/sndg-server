package ar.com.bia.backend.dao;

import ar.com.bia.entity.SeqCollectionDoc;

import java.util.List;
import java.util.Map;

public interface SeqCollectionRepository {

	List<SeqCollectionDoc> seqCollectionsFromOrganism(String organism);

	void drop();

	void save(SeqCollectionDoc seqCollection);

	Map<String, String> idStrainMap(List<String> correctedStrains);
	
	SeqCollectionDoc findOne(String id);

	SeqCollectionDoc findByName(String name) ;

	void addDrugParam(String collectionName,String description, String propName, String propType, String[] split);

	void addDrugParam(String collectionName,String description, String propName, String propType);
	
	void removeDrugParam(String collectionName,String propName, String propType);

	SeqCollectionDoc findByProject(String projectId);
	
}
