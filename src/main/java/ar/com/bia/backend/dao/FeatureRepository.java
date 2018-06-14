package ar.com.bia.backend.dao;

import ar.com.bia.entity.ChangeEmbedDoc;
import ar.com.bia.entity.GeneProductDoc;

import java.util.List;
import java.util.Map;

public interface FeatureRepository {

	Map<String,List<ChangeEmbedDoc>> variants(List<GeneProductDoc> proteins,
			List<String> strains,Map<String,String> seqCollectionIdStrain);
	
}
