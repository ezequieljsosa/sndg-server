package ar.com.bia.controllers;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.GeneProductDoc.ExpressionData;
import ar.com.bia.entity.SeqCollectionDoc;

@Controller
@RequestMapping("/expression")
public class ExpressionResource {

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private GeneProductDocumentRepository geneProductRepository;

	@RequestMapping(value = "/{genome_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String expression(
			@PathVariable("genome_id") String genomeId,
			@RequestParam(value = "replicas", defaultValue = "true") Boolean showReplicas)
			throws IOException {
		if (genomeId.trim().isEmpty()) {
			throw new RuntimeException(
					"genome parameter in querystring cannot be empty");
		}


		StringBuilder sb = new StringBuilder();

		List<GeneProductDoc> proteins = geneProductRepository
				.findAll(new Query(Criteria.where("seq_collection_id").is(
						new ObjectId(genomeId))));

		if (showReplicas){
			sb.append("samples\tsymbol\texpression\n");
		}
		else{
			sb.append("subtype\tsymbol\texpression\tfold change\tsignificant\n");
		}
		
		
		for (GeneProductDoc protein : proteins) {
			if (protein.getExpression() != null) {
				for (String subtype : protein.getExpression().keySet()) {
					ExpressionData expressionData = protein.getExpression().get(subtype);
					if (showReplicas) {
						for (String sample : expressionData.getFpkms().keySet()) {
							sb.append(sample
									+ "\t"
									+ protein.getName()
									+ "\t"
									+ expressionData
											.getFpkms().get(sample) + "\n");
						}
					} else {
						
						sb.append(subtype
								+ "\t"
								+ protein.getName()
								+ "\t"
								+ expressionData.getFpkm() 
								+ "\t" + expressionData.getFold_change() 
								+ "\t" + expressionData.getSignificant() + "\n")  ;
					}

				}
			}
		}
		return sb.toString();
	}

	@RequestMapping(value = "/{genome_id}/genes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String genes(@PathVariable("genome_id") String genomeId)
			throws IOException {

		StringBuilder sb = new StringBuilder();

		List<GeneProductDoc> proteins = geneProductRepository
				.findAll(new Query(Criteria.where("seq_collection_id").is(
						new ObjectId(genomeId))));

		sb.append("symbol\tgene\n");
		for (GeneProductDoc protein : proteins) {
			if (protein.getExpression() != null) {
				sb.append(protein.getName() + "\t" + protein.getGene() + "\n");
			}
		}

		return sb.toString();

	}

	@RequestMapping(value = "/{genome_id}/samples", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String samples(
			@PathVariable("genome_id") String genomeId,
			@RequestParam(value = "replicas", defaultValue = "true") Boolean showReplicas)
			throws IOException {

		StringBuilder sb = new StringBuilder();

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria
				.where("_id").is(genomeId)), SeqCollectionDoc.class);

		if (showReplicas) {
			sb.append("samples\tsubtype\n");
		} else {
			sb.append("subtype\n");
		}

		for (String subtype : genome.getExpression_samples().keySet()) {
			if (showReplicas) {
				for (String sample : genome.getExpression_samples()
						.get(subtype)) {
					sb.append(sample + "\t" + subtype + "\n");
				}
			} else {
				sb.append(subtype + "\n");
			}
		}

		return sb.toString();

	}

}
