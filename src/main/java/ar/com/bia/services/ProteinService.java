package ar.com.bia.services;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.backend.dao.StructureRepository;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.SeqFeature;
import ar.com.bia.entity.SeqFeatureEmbedDoc;
import ar.com.bia.pdb.StructureDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProteinService {

	@Autowired
	private StructureRepository structureRepository;
	
	@Autowired
	private GeneProductDocumentRepository geneProdRepository;
	
	public List<StructureDoc> proteinStructures(String protId) throws IOException {
		GeneProductDoc protein = this.geneProdRepository.findOne(protId);

		List<StructureDoc> findAll = this.structureRepository
				.findAll(new Query(Criteria.where("templates.aln_query.name").in(protein.getName())));
		List<String> addedStructs = new ArrayList<String>();
		List<SeqFeatureEmbedDoc> features = protein.getFeatures();
		for (SeqFeature feature : features) {
			if (feature.getType().equals("SO:0001079")) {
				String strId = feature.getIdentifier().substring(0, 4);
				if(!addedStructs.contains(strId)){
					addedStructs.add(strId);
					StructureDoc s = this.structureRepository
							.findAll(new Query(Criteria.where("name").in(strId))).get(0);
					findAll.add(s);
				}
				
			}
		}
		return findAll;

	}
	
}
