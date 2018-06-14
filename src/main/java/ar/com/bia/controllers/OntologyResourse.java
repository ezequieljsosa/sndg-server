package ar.com.bia.controllers;

import ar.com.bia.entity.OntologyTerm;
import ar.com.bia.entity.OrgOntIndexElement;
import ar.com.bia.services.OntologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/ontologies")
public class OntologyResourse {
	
	public static final String[] ROOT_GO = new String[] {"go:0008150", "go:0005575", "go:0003674"};
	
	
	private @Autowired OntologyService  ontologyService;


	@RequestMapping(value = "/terms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OntologyTerm> ontologies(@RequestBody String parameters)
			throws UnsupportedEncodingException {
		
		List<String> ontologies_arr = new ArrayList<String>();

		String[] fields = parameters.split("&");
		String[] kv;
		String ontologies = "";
		for (int i = 0; i < fields.length; ++i) {
			kv = fields[i].split("=");

			if (2 == kv.length) {
				kv[0] = java.net.URLDecoder.decode(kv[0], "UTF-8")
						.replace("[", "").replace("]", "");
				if (kv[0].trim().equals("search")) {
					ontologies = java.net.URLDecoder.decode(kv[1], "UTF-8");
				}
			}
		}

		for (String string : ontologies.split(",")) {
			ontologies_arr.add(java.net.URLDecoder.decode(string, "UTF-8")
					.toLowerCase().trim());
		}
		ontologies_arr.remove("root");
		
		// for (String ontology : ontologies.split(",")) {
		// OntologyTerm term = new OntologyTerm();
		// term.setOntology("");
		// term.setTerm(java.net.URLDecoder.decode(
		// ontology,"UTF-8").toLowerCase());
		// term.setDescription("");
		// term.setLink("#");
		// result.add(term);
		// }
		Set<String> hs = new HashSet<>();
		hs.addAll(ontologies_arr);
//		hs.remove(ROOT_GO[0]);
//		hs.remove(ROOT_GO[1]);
//		hs.remove(ROOT_GO[2]);
		
//		for (String ontology : hs) {
//			result.addAll(ontologyRepository.findAll(new Query(Criteria.where("term").is(
//					ontology))));
//		}
		return this.ontologyService.ontologies(hs,new ArrayList<String>()) ;

	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OrgOntIndexElement> ontSearch(
			@RequestParam(value = "organism", defaultValue = "") String organism,
			@RequestParam(value = "q", defaultValue = "") String query,
			@RequestParam(value = "ontologies", defaultValue = "") String ontologies) {
		if (query.isEmpty()) {
			return new ArrayList<OrgOntIndexElement>();
		}
		String[] ontologies_list = new String[]{};
		List<String> ontologyList = new ArrayList<>();
		if(!ontologies.isEmpty()){
			ontologies_list = ontologies.split(",");
			
			if (ontologies_list.length > 0) {
				ontologyList = Arrays.asList(ontologies_list);
			}
		}
		
		return this.ontologyService.ontSearch(organism, query, ontologyList);
	}

	@RequestMapping(value = "/terms/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OntologyTerm> organismSearch(
			@RequestParam(value = "q") String query,
			@RequestParam(value = "ontologies", defaultValue = "") String ontologies) {
		String[] ontologies_list = ontologies.split(",");

		
		List<String> ontologyList = new ArrayList<>();
		if (ontologies_list.length > 0) {
			ontologyList = Arrays.asList(ontologies_list);
		}
		
		
		return this.ontologyService.searchTerm(query, ontologyList);
	}

	/**
	 * Devuelve los terminos que coincidan en el campo nombre o término
	 * 
	 * @param query
	 *            : Cadena de búsqueda
	 * @return Listado de terminos de ontologías que coinciden con la búsqueda
	 */
	@RequestMapping(value = "/organism/{organism}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OrgOntIndexElement> search(
			@PathVariable("organism") String organism,
			@RequestParam(value = "q") String query,
			@RequestParam(value = "ontologies", defaultValue = "") String ontologies,

			HttpServletResponse response) {

		

		String[] ontologies_list = ontologies.split(",");		
		List<String> ontologyList = new ArrayList<>();
		if (ontologies_list.length > 0) {
			ontologyList = Arrays.asList(ontologies_list);
		}		
		
		String[] words = query.split(" ");
		
		
		
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		return this.ontologyService.searchTermByKeyword(organism,Arrays.asList(words), ontologyList);

	}

	
}
