package ar.com.bia.backend.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import ar.com.bia.backend.dao.FeatureRepository;
import ar.com.bia.entity.ChangeEmbedDoc;
import ar.com.bia.entity.GeneProductDoc;

@Repository
public class FeatureRepositoryImpl implements FeatureRepository {

	@Autowired
	private MongoOperations mongoTemplate;
	
	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	
	@Override
	/**
	 * Returns a map<key,value> with all the variants (value) of each protein symbol (key)
	 * @param proteins list of protein symbols to look for variants
	 * @param strains list of strain names to look for variants 
	 * 
	 */
	public Map<String,List<ChangeEmbedDoc>> variants(List<GeneProductDoc> proteins,
			List<String> strains,Map<String,String> strainOfCollection) {
		
//		{ $match: 	{id:{$in: proteins}} }
//		{ $project:	{id:1,variants:1}}
//		{ $unwind:	"$variants" }
//		{ $match: {$variants.strain : {$in:strains}}}			
//		BasicDBList proteinList = new BasicDBList();
//		proteinList.addAll(proteins);
//		BasicDBList strainList = new BasicDBList();
//		strainList.addAll(strains);
//		
//		DBObject matchingProteins = new BasicDBObject("$match",new BasicDBObject("id",new BasicDBObject("$in",proteinList )));
//		DBObject projection = new BasicDBObject("$project",new BasicDBObject("variants",1).put("gene", 1));
//		DBObject unwind = new BasicDBObject("$unwind","$variants");
//		DBObject marchingStrains = new BasicDBObject("$match",new BasicDBObject("variants.strain",new BasicDBObject("$in",strainList)));		
//		
//		AggregationOutput aggregate = this.mongoTemplate.getCollection("proteins").aggregate(matchingProteins,projection,unwind,marchingStrains);
//		CommandResult commandResult = aggregate.getCommandResult();
//		System.out.println(commandResult);
		
		
		
		
		Map<String,List<ChangeEmbedDoc>> result = new HashMap<String,List<ChangeEmbedDoc>>();
	
		
//		for (GeneProductDoc proteinDoc : proteins) {
//			result.put(proteinDoc.getName(), new ArrayList<VariantEmbedDoc>());			
//			List<VariantEmbedDoc> proteinVariantList = result.get(proteinDoc.getName());
//			
//			String refStrain = strainOfCollection.get(proteinDoc.getSeqCollectionId());
//			if(strains.contains(refStrain)) {
//				proteinVariantList.add(proteinDoc.toVariant(refStrain));
//			}
//				
//			if(proteinDoc.getVariants() == null)
//				continue;
//
//			for (VariantEmbedDoc variant : proteinDoc.getVariants()) {
//				if(strains.contains( variant.getStrain() )){					
//					proteinVariantList.add(variant);
//				}					
//			}
//		}		
		return result;
	}
	
	

}
