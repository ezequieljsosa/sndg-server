package ar.com.bia.controllers.services;

import ar.com.bia.backend.dao.SeqCollectionRepository;
import ar.com.bia.dto.jstree.JSTreeNodeAbstract;
import ar.com.bia.dto.jstree.JSTreeNodeAjax;
import ar.com.bia.entity.OrgOntIndexElement;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.services.JSTreeService;
import ar.com.bia.services.OntologyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/tree")
public class GOService {

	private @Autowired MongoTemplate mongoTemplate;

	// get log4j handler
	private static final Logger logger = Logger.getLogger(GOService.class);

	private @Autowired SeqCollectionRepository repositorySeqCol;
	private @Autowired JSTreeService jsTreeService;
	private @Autowired OntologyService ontologyService;

	@Autowired
	private ObjectMapper mapperJson;

	public GOService() {
		super();
	}

	@RequestMapping(value = "/{genome_name}/go", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genome_go(@PathVariable("genome_name") String genomeName, Model model, Principal principal)
			throws JsonProcessingException {

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		model.addAttribute("user", principal);
		model.addAttribute("genome", this.mapperJson.writeValueAsString(genome));

		return "search/GOSearch";
	}

	@RequestMapping(value = "/{genome_name}/go/{search}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genome_go_seach(@PathVariable("genome_name") String genomeName, @PathVariable("search") String search,
			Model model, Principal principal) throws JsonProcessingException {

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		model.addAttribute("user", principal);
		model.addAttribute("genome", this.mapperJson.writeValueAsString(genome));
		model.addAttribute("search", search);

		return "search/GOSearch";
	}

	@RequestMapping(value = "/{genome_name}/go/{search}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<JSTreeNodeAbstract> search(@PathVariable("genome_name") String genome,
			@PathVariable("search") String search,
			@RequestParam(value = "includeEmpty", defaultValue = "false") boolean includeEmpty,
			@RequestParam(value = "id") String id) {

		logger.debug("GO tree search received");
		List<JSTreeNodeAbstract> result = new ArrayList<>();
		if ( id.equals(JSTreeService.INITIAL_JSTREE_NODE_ID) ) {

			List<String> keywords = new ArrayList<>();
			Arrays.asList(search.split(" ")).stream().map(p -> p.toLowerCase()).iterator()
					.forEachRemaining(keywords::add);

			List<OrgOntIndexElement> ontologies = this.ontologyService.searchTermByKeyword(genome, keywords,
					Arrays.asList(new String[]{"go"}));
			for (OrgOntIndexElement ontSearch : ontologies) {
				JSTreeNodeAjax jsTreeNode = new JSTreeNodeAjax();
				jsTreeNode.setId(ontSearch.getTerm());

				jsTreeNode.setText(ontSearch.getName() + " -> " + ontSearch.getTerm().toUpperCase() + " ("
						+ ontSearch.getCount() + ")");
				jsTreeNode.setChildren(true);
				jsTreeNode.setParent("#");
				jsTreeNode.setType("term");

				result.add(jsTreeNode);
			}
		} else {
			SeqCollectionDoc seqCol = repositorySeqCol.findByName(genome);
			result = jsTreeService.nodeChildren(id, includeEmpty, seqCol);
		}

		return result;
	}

	@RequestMapping(value = "/{genome_name}/go/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<JSTreeNodeAbstract> openJSTreeNode(@PathVariable("genome_name") String genome,
			@RequestParam(value = "id") String id,
			@RequestParam(value = "includeEmpty", defaultValue = "false") boolean includeEmpty) {

		// String seqCollection = genome;
		SeqCollectionDoc seqCol = repositorySeqCol.findByName(genome);
		// if (seqCol.getProteomeId() != null)
		// seqCollection = seqCol.getProteomeId();

		return jsTreeService.nodeChildren(id, includeEmpty, seqCol);
	}

	// @RequestMapping(value = "/{genome}/go/", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseBody
	// public List<String> search(@PathVariable("genome") String genome,
	// @RequestParam(value = "str") String search) {
	// // List<JSTreeNodeAbstract> list = new ArrayList<JSTreeNodeAbstract>();
	// List<String> list = new ArrayList<String>();
	//
	// JSTreeNodeAjax jsTreeNode = new JSTreeNodeAjax();
	// jsTreeNode.setId("go-0008150");
	// jsTreeNode.setText("Oh NOOOOOOOO");
	// jsTreeNode.setChildren(true);
	// jsTreeNode.setType("term");
	//
	// list.add("go-0008150");
	//
	// jsTreeNode = new JSTreeNodeAjax();
	// jsTreeNode.setId("go:0051234");
	// jsTreeNode.setText("Oh NOOOOOOOO");
	// jsTreeNode.setChildren(false);
	// jsTreeNode.setParent("go:0008150");
	//
	// jsTreeNode.setType("term");
	//
	// list.add("go-0051234");
	// list.add("go-0045184");
	// list.add("go-0071693");
	//
	// return list;
	// }

	
	
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public SeqCollectionRepository getRepositorySeqCol() {
		return repositorySeqCol;
	}

	public void setRepositorySeqCol(SeqCollectionRepository repositorySeqCol) {
		this.repositorySeqCol = repositorySeqCol;
	}

	public JSTreeService getJsTreeService() {
		return jsTreeService;
	}

	public void setJsTreeService(JSTreeService jsTreeService) {
		this.jsTreeService = jsTreeService;
	}

	public OntologyService getOntologyService() {
		return ontologyService;
	}

	public void setOntologyService(OntologyService ontologyService) {
		this.ontologyService = ontologyService;
	}

	public ObjectMapper getMapperJson() {
		return mapperJson;
	}

	public void setMapperJson(ObjectMapper mapperJson) {
		this.mapperJson = mapperJson;
	}
	
}
