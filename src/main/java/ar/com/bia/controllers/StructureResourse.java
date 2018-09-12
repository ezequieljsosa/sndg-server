package ar.com.bia.controllers;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.dto.PocketData;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.OntologyTerm;
import ar.com.bia.pdb.HmmScanResultFeature;
import ar.com.bia.pdb.StructureDoc;
import ar.com.bia.services.OntologyService;
import ar.com.bia.services.StructureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/structure")
public class StructureResourse {

	@Autowired
	private ObjectMapper mapperJson;

	
	@Autowired
	private StructureService structureService;
	
	@Autowired
	private GeneProductDocumentRepository gprepo;
	
	@Autowired
	private OntologyService ontologyService;

	
	@RequestMapping(value = "/{structure_id:.+}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String organismGeneProds(@PathVariable("structure_id") String structureId, 
			@RequestParam(value="subset",defaultValue="") String subset,
			@RequestParam(value = "protein", defaultValue = "") String proteinId,
			Model model, Principal principal) throws JsonProcessingException,IOException, ClassNotFoundException {
		List<String> chains = Arrays.asList(subset.split(";"));
		if (subset.equals("")){
			chains = new ArrayList<String>();
		} 		
		
		StructureDoc structure = this.structureService.structure(structureId,chains);
		
		StructureDoc template = new StructureDoc();
		boolean hasTemplate = false;
		
		if((structure.getTemplates() != null) &&( structure.getTemplates().size() > 0)){
			String pdbName = structure.getTemplates().get(0).getAln_hit().getName().split("_")[0];
			template = this.structureService.structure(pdbName);
			
			hasTemplate = true;
		}
		
		
		List<GeneProductDoc> proteins =  new ArrayList<GeneProductDoc>();;
		if (!proteinId.isEmpty()){
//			proteins = this.structureService.proteins(structure);
//		} else {

			GeneProductDoc findOne = this.gprepo.findOne(proteinId);
			proteins.add( findOne );
		}
		
		
		
		//List<OntologyTerm> ontologies = (proteins != null) ? ontologyService.ontologies(proteins) : new ArrayList<OntologyTerm>();
		//Map<Integer,String> pocket_pdbs = this.structureService.pockets_pdbs(structure,chains);
		List<PocketData> pocket_pdbs = this.structureService.pockets_pdb(structure,chains);
		String raw_str = this.structureService.pdb_structure(structure,chains);
		StringJoiner joiner = new StringJoiner("---");
		Arrays.asList( raw_str.split("\n") ).stream().map(x->x.trim()).forEach(joiner::add);
		 String pdb = joiner.toString();

		
		model.addAttribute("user", principal);
		model.addAttribute("pdb", mapperJson.writeValueAsString(new ArrayList<String>(Arrays.asList(pdb.split("---")))));
		model.addAttribute("proteins", mapperJson.writeValueAsString(proteins));
		
		model.addAttribute("structure", mapperJson.writeValueAsString(structure));
		
		model.addAttribute("pdbName",structure.getName());
		if (hasTemplate){
			model.addAttribute("template", mapperJson.writeValueAsString(template));	
		} else {
			model.addAttribute("template",mapperJson.writeValueAsString( new Object()));
		}
		
		model.addAttribute("pocket_pdbs", mapperJson.writeValueAsString(pocket_pdbs));
		List<HmmScanResultFeature> model_features = new ArrayList<>();
		List<OntologyTerm> ontologies = new ArrayList<>();
		if(!proteins.isEmpty()){		
			ontologies = this.ontologyService.ontologies(proteins.get(0));
			model_features = this.structureService.model_features(structure,proteins.get(0).getOrganism());	
		}
		
		model.addAttribute("features", mapperJson.writeValueAsString(model_features));
		
		model.addAttribute("ontologies", mapperJson.writeValueAsString(ontologies));
		return "geneprod/Structure";
	}
	
	
	@RequestMapping(value = "/{structure_id}/pdb", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String pdb_structure(@PathVariable("structure_id") String structureId) throws IOException {

		StructureDoc structure = this.structureService.structure(structureId);
		String pdb_structure = this.structureService.pdb_structure(structure);
		
		return pdb_structure;

	}

	@RequestMapping(value = "/{structure_id}/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public byte[] download(@PathVariable("structure_id") String structureId, HttpServletResponse response)
			throws IOException {
		
		
		
		 
		
		StructureDoc structure = this.structureService.structure(structureId);
		File file = this.structureService.zipFile(structure);
		
		response.setContentType("application/zip");		
		response.setHeader("Content-Disposition", "attachment; filename=" + structure.getName() + ".zip");
		
		return FileUtil.readAsByteArray(file);
	}

//	@RequestMapping(value = "/{structure_id}/heatatom", method = RequestMethod.GET)
//	@ResponseBody
//	public String pdb_heatatom(@PathVariable("structure_id") String structureId) throws IOException {
//		
//		StructureDoc structure = this.structureService.structure(structureId);		
//		return this.structureService.pdb_heatatom(structure);
//	}
//
//	@RequestMapping(value = "/{structure_id}/pocket/{pocket_index}", method = RequestMethod.GET)
//	@ResponseBody
//	public String pdb_pocket(@PathVariable("structure_id") String structureId,
//			@PathVariable("pocket_index") Integer pocketIndex) throws IOException {
//
//		StructureDoc structure = this.structureService.structure(structureId);
//		
//		return this.structureService.pdb_pocket(structure, pocketIndex);
//
//	}

//	@RequestMapping(value = "/{structure_id}/features", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public List<HmmScanResultFeature> model_features(@PathVariable("structure_id") String structureId) throws IOException {
//
//		StructureDoc structure = this.structureService.structure(structureId);		
//		List<HmmScanResultFeature> model_features = this.structureService.model_features(structure);
//		return model_features;
//
//	}	

	
	@RequestMapping(value = "/{structure_id:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StructureDoc pockets(@PathVariable("structure_id") String structureId) throws IOException {
		return this.structureService.structure(structureId);	// this.structureRepository.findOne(structureId.replace("__", "."));

	}

}
