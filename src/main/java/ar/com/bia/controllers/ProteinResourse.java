package ar.com.bia.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.controllers.exceptions.ResourceNotFoundException;
import ar.com.bia.controllers.services.SessionService;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.OntologyTerm;
import ar.com.bia.pdb.StructureDoc;
import ar.com.bia.services.OntologyService;
import ar.com.bia.services.StructureService;

@Controller
@RequestMapping("/protein")
public class ProteinResourse {

	@Autowired
	private OntologyService ontologyService;
	
	@Autowired
	private StructureService structureService;
	
	@Autowired
	private GeneProductDocumentRepository geneProductRepository;
	
	@Autowired
	private MongoOperations mongoTemplate;
	
	@Autowired
	private ObjectMapper mapperJson;

	private @Autowired
	SessionService sessionService;
	
	@RequestMapping(value = "/{protein_id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String organismGeneProds(@PathVariable("protein_id") String proteinId, Model model, Principal principal) throws JsonProcessingException {

		
		GeneProductDoc protein = this.protein(proteinId);
		List<OntologyTerm> ontologies = ontologyService.ontologies(protein);
		List<StructureDoc> structures = this.structureService.structures(protein);
		
		model.addAttribute("user", principal);
		
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));
		
		model.addAttribute("protein", mapperJson.writeValueAsString(protein));
		model.addAttribute("ontologies", mapperJson.writeValueAsString(ontologies));
		model.addAttribute("structures", mapperJson.writeValueAsString(structures));

		return "geneprod/Protein";
	}
	
	@RequestMapping(value = "/gene/{locus_tag:.+}/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)	
	public String genomeProtein(@PathVariable("locus_tag") String locusTag,
							Model model,Principal principal) throws JsonProcessingException {	
		
		//FIXME: si hay nombre de genes repetidos se mezclan los genomas
		GeneProductDoc protein = this.geneProductRepository.findByGene(locusTag);
		if (protein == null){
			protein = this.geneProductRepository.findByAlias(locusTag);
		}
		
		return "redirect:../../" + protein.getId() ;
	}

	@RequestMapping(value = "/{protein_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public GeneProductDoc protein(@PathVariable("protein_id") String proteinId) {
		// TODO agregar validacion de seguridad/autorizacion
		GeneProductDoc protein = mongoTemplate.findOne(new Query(Criteria.where("_id").is(proteinId)),
				GeneProductDoc.class);

		if (protein == null) {
			throw new ResourceNotFoundException("protein: " + proteinId);
		}

		return protein;

	}

	@RequestMapping(value = "/fasta_from_ids", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String proteins(@RequestParam(value = "id_list[]") String[] id_list) {
		StringBuilder sb = new StringBuilder();
		for (String proteinId : id_list) {
			GeneProductDoc protein = this.protein(proteinId);
			sb.append(">" + protein.getName() + " " + protein.getDescription() + "\n" + protein.getSequence() + "\n");
		}
		return sb.toString();
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public GeneProductDoc proteinOfGene(@RequestParam(value = "gene") String gene) {
		// TODO agregar validacion de seguridad/autorizacion
		GeneProductDoc protein = mongoTemplate.findOne(new Query(Criteria.where("gene").regex("^" + gene + "$", "i")),
				GeneProductDoc.class);
		if (protein == null) {
			protein = new GeneProductDoc();
		}
		return protein;

	}

	@RequestMapping(value = "/gene/{gene:.+}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public GeneProductDoc gene(@PathVariable("gene") String gene_name) {
		// TODO agregar validacion de seguridad/autorizacion		
		
		GeneProductDoc protein = mongoTemplate.findOne(new Query(Criteria.where("gene").is(gene_name)),
				GeneProductDoc.class);
		if (protein == null) {
			protein = mongoTemplate.findOne(new Query(Criteria.where("gene_id").is(gene_name)), GeneProductDoc.class);
		}
		return protein;

	}

	@RequestMapping(value = "feature/{feature_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<GeneProductDoc> fromFeature(@PathVariable("feature_id") String featureId) {
		// TODO agregar validacion de seguridad/autorizacion
		List<String> ids = Arrays.asList(featureId.replace("___", ".").split(","));
		List<GeneProductDoc> proteins = mongoTemplate.find(new Query(Criteria.where("name").in(ids)),
				GeneProductDoc.class);

		// if (proteins.isEmpty()) {
		// List<ObjectId> idsObj = new ArrayList<ObjectId>();
		// for (String id : ids) {
		// idsObj.add(new ObjectId(id));
		// }
		// proteins = mongoTemplate.find(
		// new Query(Criteria.where("features._id").in(idsObj)),
		// GeneProductDoc.class);
		// }
		return proteins;

	}

}
