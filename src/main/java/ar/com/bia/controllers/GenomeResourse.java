package ar.com.bia.controllers;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.util.FileUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.bia.DataTablesUtils;
import ar.com.bia.backend.dao.GeneDocumentRepository;
import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.backend.dao.impl.VarDocRepositoryImpl;
import ar.com.bia.controllers.exceptions.ForbiddenException;
import ar.com.bia.controllers.services.SessionService;
import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.entity.GeneDoc;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.OntologyTerm;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.UserDoc;
import ar.com.bia.entity.druggability.SeqColDruggabilityParam;
import ar.com.bia.services.DruggabilityService;
import ar.com.bia.services.OntologyService;
import ar.com.bia.services.UserService;
import ar.com.bia.services.exception.PropFileLoadException;

@Controller
@RequestMapping("/genome")
public class GenomeResourse {

	@Autowired
	private OntologyService ontologyService;

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private GeneDocumentRepository geneRepository;

	@Autowired
	private DruggabilityService druggabilityService;

	@Autowired
	private GeneProductDocumentRepository geneProductRepository;

	@Autowired
	private VarDocRepositoryImpl varDocRepository;
	
	@Autowired
	private ObjectMapper mapperJson;

	private @Autowired DataTablesUtils dataTablesUtils;

	private @Autowired SessionService sessionService;
	
	

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomes(Model model, Principal principal) {
		model.addAttribute("user", principal);
		return "omic/GenomeList";
	}

	@RequestMapping(value = "/{genome_name:.+}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genome(@PathVariable("genome_name") String genomeName, Model model, Principal principal)
			throws JsonProcessingException, ForbiddenException {

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);
		
		if (genome == null){
			genome = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(genomeName))),
					SeqCollectionDoc.class);
			genomeName = genome.getName();
		}

		if (genome == null){
			genome = this.mongoTemplate.findOne(new Query(Criteria.where("ncbi_assembly").is(genomeName)),
					SeqCollectionDoc.class);
			genomeName = genome.getName();
		}

		UserDoc user = this.userService.findUser(principal.getName());
		if (!genome.getAuth().equals(UserDoc.publicUserId)) {
			if (!genome.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}

		List<Map<String, Object>> druggabilityTable = this.druggabilityService.druggabilityTable(genome);

		List<Float> druggabilityDistribution = new ArrayList<Float>();
		for (Float float1 : genome.getMetricValues("druggabilityDistribution")) {
			druggabilityDistribution.add(float1);
		}
		model.addAttribute("jbrowse_enabled", new File("/data/xomeq/jbrowse/data/" + genomeName).exists());
		
		model.addAttribute("user", principal);
		model.addAttribute("logged_user", isValidUser(principal));
		model.addAttribute("genome_id", genomeName);
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));

		model.addAttribute("druggability_distribution", mapperJson.writeValueAsString(druggabilityDistribution));
		model.addAttribute("druggability_table", mapperJson.writeValueAsString(druggabilityTable));

		return "omic/Genome";
	}

	@RequestMapping(value = "/{genome_name:.+}/gene/{locus_tag:.+}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomeGene(@PathVariable("genome_name") String genomeName, @PathVariable("locus_tag") String locusTag,
			Model model, Principal principal) throws JsonProcessingException {

		GeneProductDoc protein = this.geneProductRepository.findByGene(genomeName, locusTag);
		@SuppressWarnings("unchecked")
		List<Object> collect =  this.mapperJson.convertValue( protein.getGeneList(),List.class );
		GeneDoc gene = this.geneRepository.findOrganismGene("identifier", collect );

		model.addAttribute("user", principal);
		model.addAttribute("genome_id", genomeName);
		model.addAttribute("protein", mapperJson.writeValueAsString(protein));
		model.addAttribute("gene", mapperJson.writeValueAsString(gene));
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));

		return "geneprod/Gene";
	}
	
	@RequestMapping(value = "/{genome_name:.+}/contig/{contig:.+}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomePart(@PathVariable("genome_name") String genomeName, @PathVariable("contig") String contig,
			@RequestParam(value = "start") String start ,
			@RequestParam(value = "end") String end ,
			Model model, Principal principal) throws JsonProcessingException {
		
		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);
		
		model.addAttribute("user", principal);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("contig", contig);
		model.addAttribute("genome_id", genomeName);
		model.addAttribute("genome_desc", genome.getDescription());
		
		
		

		return "omic/GenomePart";
	}
	
	@RequestMapping(value = "/{genome_name:.+}/strain/{strain:.+}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomeReads(@PathVariable("genome_name") String genomeName, @PathVariable("strain") String strain,
			@RequestParam(value = "variantId") String variantId ,			
			Model model, Principal principal) throws JsonProcessingException {

		
		model.addAttribute("user", principal);
		model.addAttribute("genome_id", genomeName);
		model.addAttribute("strain", strain);
		model.addAttribute("variant", mapperJson.writeValueAsString(
				varDocRepository.findOne(variantId)
				));
		
		

		return "comparative/Strain";
	}
	

	@RequestMapping(value = "/{genome_name:.+}/pathway", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomePathways(@PathVariable("genome_name") String genomeName, Model model, Principal principal)
			throws JsonProcessingException {
		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		List<OntologyTerm> ontologies = this.ontologyService.pathwayOntologies(genome);

		model.addAttribute("user", principal);
		model.addAttribute("genome", mapperJson.writeValueAsString(genome));
		model.addAttribute("ontologies", mapperJson.writeValueAsString(ontologies));
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));

		return "omic/Pathways";
	}

	@RequestMapping(value = "/{genome_name:.+}/pathway/{pathways}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomePathways(@PathVariable("genome_name") String genomeName,
			@PathVariable(value = "pathways") String pathways, Model model, Principal principal)
			throws JsonProcessingException {
		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		List<OntologyTerm> ontologies = this.ontologyService.pathwayOntologies(genome);

		model.addAttribute("user", principal);
		model.addAttribute("genome", mapperJson.writeValueAsString(genome));
		model.addAttribute("pathways", pathways);
		model.addAttribute("ontologies", mapperJson.writeValueAsString(ontologies));
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));

		return "omic/Pathways";
	}
	

	@RequestMapping(value = "/{genome_name:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SeqCollectionDoc genome(@PathVariable("genome_name") String genomeName, Principal principal)
			throws ForbiddenException {

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);
		UserDoc user = this.userService.findUser(principal.getName());
		if (!genome.getAuth().equals(new ObjectId("563b9440b1b50423d1fd1fee"))) {
			if (!genome.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}

		return genome;
	}

	@RequestMapping(value = "/organism/{organism}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SeqCollectionDoc organism(@PathVariable("organism") String organism, Principal principal)
			throws ForbiddenException {

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("organism").is(organism)),
				SeqCollectionDoc.class);
		UserDoc user = this.userService.findUser(principal.getName());
		if (!genome.getAuth().equals(new ObjectId("563b9440b1b50423d1fd1fee"))) {
			if (!genome.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}
		return genome;
	}

	@ResponseBody
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedResult<SeqCollectionDoc> genomeList(
			@RequestParam(value = "length", defaultValue = "10") Integer perPage,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "search[value]", defaultValue = "") String search, Principal principal) {

		List<ObjectId> auth = this.dataTablesUtils.authCriteria(principal);
		Query query = new Query(Criteria.where("auth").in(auth));

		long protCount = mongoTemplate.count(query, SeqCollectionDoc.class);

		int queryOffset = new Long(new Double(Math.ceil(offset / perPage)).longValue()).intValue();
		//Sort sortObj = this.dataTablesUtils.createSortFromQueryString();
		Sort sortObj = null;

		Criteria[] searchFilterCriteria = this.dataTablesUtils.createCriteriaFromQueryString("organism", search);

		Criteria filtered = new Criteria();
		if (searchFilterCriteria.length != 0) {
			filtered = filtered.andOperator(searchFilterCriteria);
		}

		Criteria[] columnsSeach = this.dataTablesUtils.createCriteriaFromQueryStringColumns();
		if (columnsSeach.length != 0) {
			filtered = filtered.andOperator(columnsSeach);
		}

		PaginatedResult<SeqCollectionDoc> result = DataTablesUtils.queryCollection(queryOffset, perPage, sortObj, auth,
				filtered, SeqCollectionDoc.class, mongoTemplate, protCount);

		return result;

	}

	//@Cacheable(cacheNames = "queries")
	@RequestMapping(value = "{genome_name:.+}/pathway/{pathways}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> geneProducts2(@PathVariable(value = "genome_name") String genomeName,
			@PathVariable(value = "pathways") String pathways) throws JsonProcessingException {

		Map<String, Object> result = new HashMap<String, Object>();
		Criteria criteria = Criteria.where("organism").is(genomeName);

		String[] split = pathways.split(",");
		if (split.length > 1) {
			Criteria[] allCriterias = new Criteria[split.length];
			int i = 0;
			for (String pathway : split) {
				allCriterias[i] = Criteria.where("reactions.pathways").is(pathway.toUpperCase());
				i++;
			}
			criteria = criteria.andOperator(new Criteria().orOperator(allCriterias));
		} else {
			criteria = criteria.and("reactions.pathways").is(pathways.toUpperCase());
		}

		Query query = new Query(criteria);
		List<GeneProductDoc> proteins = this.geneProductRepository.findAll(query);
		List<String> ontologies = new ArrayList<String>();
		ontologies.add("biocyc_pw");
		ontologies.add("biocyc_reac");
		ontologies.add("biocyc_comp");
		List<OntologyTerm> terms = new ArrayList<OntologyTerm>();
		for (GeneProductDoc geneProductDoc : proteins) {
			terms.addAll(this.ontologyService.ontologies(geneProductDoc, ontologies));
		}
		proteins.stream().forEach(x -> {
			x.setSequence("");
			x.setKeywords(new ArrayList<>());
			x.setSearch(new HashMap<>());
			x.setDbxrefs(new ArrayList<>());
			x.setLocation(null);
			x.setFeatures(null);
			//x.setSeqCollectionId(null);
			x.setAlias(null);
			x.setProperties(null);
		});
		terms.forEach(x -> {
			x.setDescription("");
			x.setChildren(new ArrayList<>());
			x.setLink(null);			
			
		});
		result.put("proteins", proteins);
		result.put("ontologies", terms);
System.out.println("OK");
		return result;
	}

	@RequestMapping(value = "/geneproducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResult<GeneProductDoc> geneProducts(
			@RequestParam(value = "length", defaultValue = "10") Integer perPage,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "search[value]", defaultValue = "") String search, Principal principal) {

		List<ObjectId> auth = this.dataTablesUtils.authCriteria(principal);
		UserDoc user = this.userService.findUser(principal.getName());
		long protCount = Long.parseLong(user.getPre_loaded().get("protein_count").toString());

		int queryOffset = new Long(new Double(Math.ceil(offset / perPage)).longValue()).intValue();
		Criteria[] searchFilterCriteria = this.dataTablesUtils.createCriteriaFromQueryString("keywords", search);
		Criteria[] columnsSeach = this.dataTablesUtils.createCriteriaFromQueryStringColumns();
		Criteria[] allCriterias = null;
		if (searchFilterCriteria.length > 0) {
			allCriterias = this.dataTablesUtils.andCriteriaOperator(searchFilterCriteria, columnsSeach);
		} else {
			allCriterias = columnsSeach;
		}

		Criteria filtered = null;
		if (allCriterias.length == 0) {
			filtered = new Criteria();
		} else if (allCriterias.length == 1) {
			filtered = allCriterias[0];
		} else {
			filtered = new Criteria().andOperator(allCriterias);
		}
		//Sort sortObj = this.dataTablesUtils.createSortFromQueryString();
		Sort sortObj = null;
		PaginatedResult<GeneProductDoc> result = DataTablesUtils.queryCollection(queryOffset, perPage, sortObj, auth,
				filtered, GeneProductDoc.class, mongoTemplate, protCount);

		return result;
	}

	@RequestMapping(value = "/{genome_name:.+}/download/gff", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public byte[] downloadAnnotation(@PathVariable("genome_name") String genomeName, HttpServletResponse response,
			Principal principal) throws IOException, ForbiddenException {

		UserDoc user = this.userService.findUser(principal.getName());

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		if (!genome.getAuth().equals(new ObjectId("563b9440b1b50423d1fd1fee"))) {
			if (!genome.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}

		File file = new File("/data/organismos/" + genomeName + "/anotacion/anotacion.gff3");

		response.setContentType("application/gff3");
		response.setHeader("Content-Disposition", "attachment; filename=" + genomeName + ".gff3");

		return FileUtil.readAsByteArray(file);
	}

	@RequestMapping(value = "/{genome_name:.+}/download/fasta", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public byte[] downloadContigs(@PathVariable("genome_name") String genomeName, HttpServletResponse response,
			Principal principal) throws IOException, ForbiddenException {

		UserDoc user = this.userService.findUser(principal.getName());

		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		if (!genome.getAuth().equals(new ObjectId("563b9440b1b50423d1fd1fee"))) {
			if (!genome.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}

		File file = new File("/data/organismos/" + genomeName + "/contigs/genoma.fasta");

		response.setContentType("application/fasta");
		response.setHeader("Content-Disposition", "attachment; filename=" + genomeName + ".fasta");

		return FileUtil.readAsByteArray(file);
	}

	@RequestMapping(value = "/{genome_name:.+}/properties", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> properties(@PathVariable("genome_name") String genomeName,			 
			@RequestBody String parameters, Principal principal) throws IOException, ForbiddenException {

		Map<String, String> result = new HashMap<>();
		result.put("status", "ok");

		try {

			SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
					SeqCollectionDoc.class);

			List<SeqColDruggabilityParam> otherParams = genome.getDruggabilityParams().stream()
					.filter(p -> !p.getUploader().equals(principal.getName())).collect(Collectors.toList());

			
			List<SeqColDruggabilityParam> dto = mapperJson.readValue(parameters, new TypeReference<List<SeqColDruggabilityParam>>(){});
			dto.stream().forEach(x -> {
				if(x.getDescription() == null){
					x.setDescription("");
				}
			});
			otherParams.addAll(dto);

			genome.setDruggabilityParams(otherParams);

			this.mongoTemplate.save(genome);

		} catch (Exception ex) {

			result.put("status", "error");
			result.put("error", ex.getMessage());
			ex.printStackTrace();

		}

		return result;
	}

	@RequestMapping(value = "{genome_name:.+}/properties/upload", method = RequestMethod.POST)
	public String uploadFile(@PathVariable("genome_name") String genomeName, 
			@RequestParam(value="tour",defaultValue="") String tour,
			MultipartFile input_file, Model model,
			Principal principal) throws ForbiddenException {
		
		
		validateLoggedUser(principal);
		
		List<String> errors = new ArrayList<String>();

		try {
			druggabilityService.loadPropFiles(genomeName, input_file.getInputStream(), principal.getName());
		} catch (PropFileLoadException e) {
			errors.add(e.getMessage());
		} catch (IOException e) {
			errors.add("error unknown reading uploaded file...");
		}

		model.addAttribute("uploadErrors", errors);
		model.addAttribute("isUpload", true);
		String tourQS = "";
		if(tour.equals("2")){
			tourQS = "?tour=2&uploadComplete=1&skipFirst=1&isUpload=true";
		}
		return "redirect:/genome/" + genomeName + tourQS;
	}

	private void validateLoggedUser(Principal principal) throws ForbiddenException {
		if(!isValidUser(principal)	){
			throw new ForbiddenException();
		}
	}

	private boolean isValidUser(Principal principal) {
		if((principal.getName() == null) || (principal.getName().isEmpty()))
			return false;
		return !principal.getName().equals("demo");
	}

	@RequestMapping(value = "{genome_name:.+}/properties/delete", method = RequestMethod.POST)
	public String deleteProp(@PathVariable("genome_name") String genomeName, String name, String uploader, Model model,
			Principal principal) throws ForbiddenException {

		validateLoggedUser(principal);
		
		this.geneProductRepository.removePropFromAllProteins(genomeName, name , uploader);
		model.addAttribute("isUpload", true);
		
		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		List<SeqColDruggabilityParam> otherParams = genome.getDruggabilityParams().stream().filter(p -> !(p.getName().equals(name) &&  p.getUploader().equals(principal.getName() )  )).collect(Collectors.toList());
		if (otherParams.size() == (genome.getDruggabilityParams().size() - 1)){
			genome.setDruggabilityParams(otherParams);
			this.mongoTemplate.save(genome);	
		}
		
		return "redirect:/genome/" + genomeName;
	}

}
