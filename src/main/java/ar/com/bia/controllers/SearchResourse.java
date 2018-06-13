package ar.com.bia.controllers;

import java.io.IOException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import ar.com.bia.DataTablesUtils;
import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.backend.dao.SeqCollectionRepository;
import ar.com.bia.controllers.services.SessionService;
import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.dto.PathwayDTO;
import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.dto.druggability.DruggabilitySearch;
import ar.com.bia.dto.druggability.ScoreDTO;
import ar.com.bia.dto.druggability.Scoreable;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.OntologyTerm;
import ar.com.bia.entity.SearchProtDoc;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.UserDoc;
import ar.com.bia.entity.druggability.SeqColDruggabilityParam;
import ar.com.bia.services.OntologyService;
import ar.com.bia.services.UserService;
import ar.com.bia.services.exception.OrganismNotFoundException;

@Controller
@RequestMapping("/search")
public class SearchResourse {

	@Autowired
	private OntologyService ontologyService;

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private GeneProductDocumentRepository geneProductRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private SeqCollectionRepository seqCollectionRepo;

	private @Autowired DataTablesUtils dataTablesUtils;

	private @Autowired SessionService sessionService;

	@Autowired
	private ObjectMapper mapperJson;

	@RequestMapping(value = "/{keywords}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String organismGeneProds(@PathVariable("keywords") String keywords, Model model, Principal principal)
			throws JsonProcessingException {

		UserDoc user = this.userService.findUser(principal.getName());
		String organisms = String.join(",", this.userService.organisms(user));
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));
		model.addAttribute("organisms", organisms);

		model.addAttribute("organism", null);

		model.addAttribute("keywords", keywords);
		model.addAttribute("gene", null);

		model.addAttribute("user", principal);

		return "search/Search";
	}

	@RequestMapping(value = "/{genome_name:.+}/gene/{gene_name}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String organismGeneProds(@PathVariable("genome_name") String genomeName,
			@PathVariable("gene_name") String geneName, Model model, Principal principal)
			throws JsonProcessingException {

		return this.geneProducts(genomeName, model, principal, null, geneName);
	}

	@RequestMapping(value = "/{genome_id}/product/{keywords}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String geneProds(@PathVariable("genome_id") String genomeId, @PathVariable("keywords") String keywords,
			Model model, Principal principal) throws JsonProcessingException {

		return this.geneProducts(genomeId, model, principal, keywords, null);
	}

	@RequestMapping(value = "/{genome_id}/product/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String allGeneProds(@PathVariable("genome_id") String genomeId, Model model, Principal principal)
			throws JsonProcessingException {

		return this.geneProducts(genomeId, model, principal, null, null);
	}

	private String geneProducts(String genomeId, Model model, Principal principal, String keywords, String geneName)
			throws JsonProcessingException {
		SeqCollectionDoc genome = this.seqCollectionRepo.findByName(genomeId);

		model.addAttribute("user", principal);
		model.addAttribute("genome_id", genomeId);
		model.addAttribute("organismName", genome.getDescription());
		model.addAttribute("organism", genomeId);
		model.addAttribute("keywords", keywords);
		model.addAttribute("gene", geneName);

		model.addAttribute("ontologies", mapperJson.writeValueAsString(this.ontologyService.ontologies_go_ec(genome)) );
		
		
		List<SeqColDruggabilityParam> druggabilityParams = genome.getDruggabilityParams().stream().filter(x -> {
			return (x.getUploader() == null) || (x.getUploader().equals("demo"))
					|| (x.getUploader().equals(principal.getName()));
		}).collect(Collectors.toList());
		model.addAttribute("searchProps", mapperJson.writeValueAsString(druggabilityParams));
		model.addAttribute("statistics", mapperJson.writeValueAsString(genome.getStatistics()));

		UserDoc user = this.userService.findUser(principal.getName());
		String organisms = String.join(",", this.userService.organisms(user));
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));
		model.addAttribute("organisms", organisms);

		return "search/Search";
	}

	@RequestMapping(value = "/{genome_name:.+}/pathways/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomePathwaysScore(@PathVariable("genome_name") String genomeName, Model model, Principal principal)
			throws JsonProcessingException {
		SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);

		//List<OntologyTerm> ontologies = this.ontologyService.ontologies(genome);
		//model.addAttribute("ontologies", mapperJson.writeValueAsString(ontologies));
		
		model.addAttribute("ontologies", mapperJson.writeValueAsString(this.ontologyService.ontologies_go_ec(genome)) );
		List<SeqColDruggabilityParam> druggabilityParams = genome.getDruggabilityParams().stream().filter(x -> {
			return (x.getUploader() == null) || (x.getUploader().equals("demo"))
					|| (x.getUploader().equals(principal.getName()));
		}).collect(Collectors.toList());
		model.addAttribute("searchProps", mapperJson.writeValueAsString(druggabilityParams));
		model.addAttribute("user", principal);
		model.addAttribute("genome", mapperJson.writeValueAsString(genome));
		model.addAttribute("organism", genome.getName());
		model.addAttribute("statistics", mapperJson.writeValueAsString(genome.getStatistics()));
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));

		return "search/PathwaysSearch";
	}

	@RequestMapping(value = "/{genome_id}/product/{keywords}/download", method = RequestMethod.GET, produces = "application/csv")
	@ResponseBody
	public String download(@PathVariable(value = "keywords") String keywords,
			// @PathVariable(value = "ontologies") String ontologies,
			@PathVariable(value = "genome_id") String organism, Principal principal, HttpServletResponse response)
			throws IOException {
		String ontologies = "";

		Criteria[] searchFilterCriteria = dataTablesUtils.createCriteriaFromQueryString("keywords", keywords);
		Criteria[] searchFilterCriteria2 = dataTablesUtils.createCriteriaFromQueryString("ontologies", ontologies);
		if (searchFilterCriteria2.length > 0) {
			searchFilterCriteria = dataTablesUtils.andCriteriaOperator(searchFilterCriteria, searchFilterCriteria2);
		}

		UserDoc user = this.userService.findUser(principal.getName());
		List<String> organisms = this.userService.organisms(user);

		Criteria criteria = new Criteria().andOperator(searchFilterCriteria);
		if (organism.isEmpty()) {
			criteria = Criteria.where("organism").in(organisms).andOperator(searchFilterCriteria);
		} else {
			criteria = Criteria.where("organism").is(organism).andOperator(searchFilterCriteria);
		}

		Query query = new Query(criteria);

		List<GeneProductDoc> genomeList = this.geneProductRepository.findAll(query);

		StringBuilder sb = new StringBuilder();
		sb.append("name;description;genes;druggability\r\n");

		for (GeneProductDoc geneProductDoc : genomeList) {
			String druggability = "0";
			if (geneProductDoc.getSearch().getOrDefault("druggability", null) != null)
				druggability = geneProductDoc.getSearch().get("druggability").toString().replace('.', ',');
			sb.append(MessageFormat.format("{0};{1};{2};{3}\r\n", geneProductDoc.getName().replace(",", "_"),
					geneProductDoc.getDescription().replace(",", " - "), geneProductDoc.getGene().replace(",", " - "),
					druggability));
		}

		response.setContentType("application/csv");
		response.setHeader("Content-Disposition", "attachment; filename=xomeq.csv");

		return sb.toString();

	}

	@Cacheable(cacheNames = "queries", key = "T(ar.com.bia.MD5).hash(#search ,#genomeName + '_' + #perPage.toString() + '_' + #offset.toString(),#request,null)")
	@RequestMapping(value = "{genomeName}/pathways/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResult<PathwayDTO> pathways(@PathVariable(value = "genomeName") String genomeName,

			@RequestParam(value = "length", defaultValue = "10") Integer perPage,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "search[value]", defaultValue = "") String search, Principal principal,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {

		

		PaginatedResult<PathwayDTO> result = new PaginatedResult<>();

		SeqCollectionDoc genome = this.seqCollectionRepo.findByName(genomeName);

		List<PathwayDTO> pathwaysDtos = genome.getPathways().stream().map(x -> {
			PathwayDTO p = x.clone();
			p.setProperties(new ArrayList<>());
			p.setScore(0);
			return p;
		}).collect(Collectors.toList());

		Map<String, Map<String, Object>> pathways = genome.getPathways().stream()
				.collect(Collectors.toMap(pw -> pw.getTerm(), pw -> pw.getProperties2()));

		if (search.isEmpty()) {

			result.setData(pathwaysDtos);
			return result;
		}

		ScoreDTO dto = mapperJson.readValue(search.replace(".-", "").toLowerCase(), ScoreDTO.class);
		if (!dto.getFilters().isEmpty()) {
			pathwaysDtos = pathwaysDtos.stream()
					.filter(p -> dto.getFilters().stream().allMatch(f -> f.matches(p.getProperties2())))
					.collect(Collectors.toList());
		}

		Criteria filtered = Criteria.where("reactions.0").exists(true).and("organism").is(genomeName);

		final List<SearchProtDoc> proteins = new ArrayList<>();

		DBObject projections = new BasicDBObject();  
		dto.getScores().stream().filter(x -> !(x.getName().equals("keyword") || x.getName().equals("ontology") ) ).forEach(x -> {
			 x.mongoProject(projections);
		});
		
		
		if (dto.getScores().size() > 0){
			
			proteins.addAll( fillData(System.nanoTime(), filtered,projections, 0,0)); 
			
		}
		

		result.setRecordsTotal((long) genome.getPathways().size());
		result.setRecordsFiltered((long) pathwaysDtos.size());

//		List<GeneProductDoc> finalProtList = proteins.stream().map(p -> {
//			GeneProductDoc gp = this.geneProductRepository.findByID(new ObjectId(p.getId()));
//			gp.setScore(p.getScore());
//			return gp;
//		}).collect(Collectors.toList());
		
		List<SearchProtDoc> finalProtList = sortData(dto, proteins);

		final List<DruggabilityParam> scoresProt = dto.getScores().stream()
				.filter(p -> !p.getTarget().equals("pathway")).collect(Collectors.toList());
		final List<DruggabilityParam> scoresPw = dto.getScores().stream().filter(p -> p.getTarget().equals("pathway"))
				.collect(Collectors.toList());

		// Key (pw) Val ( Key pwPor Value valor de prop )
		final Map<String, Map<String, List<Double>>> pathwayScores = new HashMap<String, Map<String, List<Double>>>();

		pathwaysDtos.forEach(pathway -> {
			pathwayScores.put(pathway.getTerm(), new HashMap<String, List<Double>>());

			scoresPw.stream().forEach(score -> {
				Map<String, Object> pwProperties = pathways.get(pathway.getTerm());
				double protScore = score.score(null, pwProperties);
				if (!pathwayScores.get(pathway.getTerm()).containsKey(score.getCompleteName())) {
					pathwayScores.get(pathway.getTerm()).put(score.getCompleteName(), new ArrayList<Double>());
				}

				Map<String, Object> props = new HashMap<String, Object>();
				props.put("name", score.getCompleteName());
				props.put("result", protScore);
				props.put("operation", "");
				pathway.getProperties().add(props);

				pathwayScores.get(pathway.getTerm()).get(score.getCompleteName()).add(protScore);
			});

			finalProtList.stream().filter(protein -> {
				return protein.getReactions().stream().filter(p -> p.getPathways().contains(pathway.getTerm()))
						.collect(Collectors.toList()).size() > 0;
			}).forEach(protein -> {
				scoresProt.stream().forEach(score -> {
					double protScore = score.score(protein.getKeywords(), protein.getSearch());
					if (!pathwayScores.get(pathway.getTerm()).containsKey(score.getCompleteName())) {
						pathwayScores.get(pathway.getTerm()).put(score.getCompleteName(), new ArrayList<Double>());
					}
					pathwayScores.get(pathway.getTerm()).get(score.getCompleteName()).add(protScore);
				});
			});
		});

		pathwaysDtos.stream().forEach(pathway -> {
			Map<String, List<Double>> scoreMap = pathwayScores.get(pathway.getTerm());
			double pathwayScore = 0;
			if (scoreMap != null) {
				if (scoresProt.size() != 0) {
					pathwayScore = scoresProt.stream().filter(s -> scoreMap.get(s.getCompleteName()) != null)
							.map(score -> score.applyGroupOperation(pathway, scoreMap.get(score.getCompleteName())))
							.mapToDouble(x -> x).sum();
				}
				pathwayScore += scoresPw.stream().map(s -> scoreMap.get(s.getName()).get(0)).mapToDouble(p -> p).sum();

			}
			pathway.setScore(pathwayScore);
		});

		pathwaysDtos = pathwaysDtos.stream().sorted((u1, u2) -> {
			// return Integer.signum( (int) Math.round( (u1.getScore() >
			// u2.getScore())) );
			if (u1.getScore() > u2.getScore())
				return -1;
			else if (u1.getScore() < u2.getScore())
				return 1;
			else
				return 0;
		}).collect(Collectors.toList());

		result.setData(pathwaysDtos);

		
		

		return result;

	}

	@RequestMapping(value = "{organism}/download", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String geneProducts(@PathVariable(value = "organism") String organism,
			// @RequestParam(value = "search", defaultValue = "") String search,
			Principal principal, HttpServletResponse response, HttpSession httpSession) throws Exception {

		if (httpSession.getAttribute("search") == null) {
			response.setContentType("application/csv");
			response.setHeader("Content-Disposition", "attachment; filename=error.csv");
			return "refresh list first".toString();
		}
		String search = httpSession.getAttribute("search").toString();

		long protCount = this.geneProductRepository.count(new Query(Criteria.where("organism").is(organism)));

		ScoreDTO dto = new ScoreDTO();
		if (!search.isEmpty()) {

			dto = mapperJson.readValue(search.replace(".-", "").toLowerCase(), ScoreDTO.class);
			if (dto.getFilters().isEmpty() && dto.getScores().isEmpty()) {
				response.setContentType("application/csv");
				response.setHeader("Content-Disposition", "attachment; filename=error.csv");
				return "no filter nor scores found, remember to refresh the list".toString();
			}
		} else {
			response.setContentType("application/csv");
			response.setHeader("Content-Disposition", "attachment; filename=error.csv");
			return "no filter nor scores found, remember to refresh the list".toString();
		}
		final List<String> scoreParamNames = dto.getScores().stream().map(x -> x.getCompleteName())
				.collect(Collectors.toList());
		String keywords = dto.filters.stream().filter(p -> DruggabilityParam.defaultSearchParams.contains(p.getName()))
				.map(p -> (p.getOperation().equals("non equal") ? "!" : "") + p.getValue())
				.collect(Collectors.joining(" "));

		List<DruggabilityParam> inMemoryCriteria = dto.filters.stream()
				.filter(p -> !DruggabilityParam.defaultSearchParams.contains(p.getName())).collect(Collectors.toList());

		Criteria[] allCriterias = dataTablesUtils.createCriteriaFromQueryString2("keywords", keywords);

		Criteria filtered = Criteria.where("organism").is(organism);
		if (allCriterias.length > 0) {

			filtered = filtered.andOperator(allCriterias);
		}

		List<SearchProtDoc> data = new ArrayList<>();

		PaginatedResult<GeneProductDoc> result = new PaginatedResult<>();
		// data = mongoTemplate.find(new Query(filtered), SearchProtDoc.class);

		DBCursor find = mongoTemplate.getCollection("proteins").find(new Query(filtered).getQueryObject());
		for (DBObject dbObject : find) {
			data.add(new SearchProtDoc(dbObject, mongoTemplate));
		}

		result.setRecordsTotal(protCount);

		if (!inMemoryCriteria.isEmpty()) {

			data = data.stream()
					.filter(geneProd -> inMemoryCriteria.stream()
							.allMatch(paramFilter -> paramFilter.matches(geneProd.getSearch())))
					.collect(Collectors.toList());
		}

		result.setRecordsFiltered((long) data.size());

		data = sortData(dto, data);

		StringJoiner export = new StringJoiner("\n\r");
		StringBuilder sb = new StringBuilder("gene,");
		scoreParamNames.forEach(x -> {
			sb.append(x).append(",");
		});
		sb.append("score");
		export.add(sb.toString());
		data.forEach(x -> {
			StringBuilder sb2 = new StringBuilder(x.getGene().replaceAll(",", "_") + ",");
			scoreParamNames.forEach(y -> {
				Object param = x.getSearch().get(y);
				if (param == null) {
					param = "";
				}
				sb2.append(param.toString().replace(",", "_")).append(",");
			});
			sb2.append(x.getScore().toString());
			export.add(sb2.toString());
		});

		response.setContentType("application/csv");
		response.setHeader("Content-Disposition", "attachment; filename=protlist.csv");
		return export.toString();

	}

	public static <T> T[] concat(T[] first, T[] second) {
		  T[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
		}
	
	@Cacheable(cacheNames = "queries", key = "T(ar.com.bia.MD5).hash(#search , #perPage.toString() + '_' + #offset.toString(),#request,#httpSession)")
	@RequestMapping(value = "/product", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String geneProducts(
			@RequestParam(value = "length", defaultValue = "10") Integer perPage,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "search[value]", defaultValue = "") String search, Principal principal,
			HttpSession httpSession, HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException, OrganismNotFoundException {

		long startTime = System.nanoTime();

		
		printDuration(startTime, "guardado en session");

		List<String> auth = dataTablesUtils.authCriteria(principal);
		

		long protCount = this.geneProductRepository
				.count(new Query(Criteria.where("organism").is(this.dataTablesUtils.getOrganismFromColumn())));

		ScoreDTO dto = new ScoreDTO();
		if (!search.isEmpty()) {

			dto = mapperJson.readValue(search.replace(".-", ""), ScoreDTO.class);
			if (dto.getFilters().isEmpty() && dto.getScores().isEmpty()) {

				PaginatedResult<SearchProtDoc> result = defaultSearch(perPage, offset, "", auth, protCount);
				result.setData(sortData(dto, result.getData()));
				return  mapperJson.writeValueAsString(result);
			}
		} else {
			PaginatedResult<SearchProtDoc> result = defaultSearch(perPage, offset, search, auth, protCount);

			return  mapperJson.writeValueAsString(result);
		}

		String keywords = dto.filters.stream().filter(p -> DruggabilityParam.defaultSearchParams.contains(p.getName()))
				.map(p -> (p.getOperation().equals("non equal") ? "!" : "") + p.getValue())
				.collect(Collectors.joining(" "));

		List<DruggabilityParam> inMemoryCriteria = dto.filters.stream()
				.filter(p -> !DruggabilityParam.defaultSearchParams.contains(p.getName())).collect(Collectors.toList());

		Criteria[] searchFilterCriteria = dataTablesUtils.createCriteriaFromQueryString2("keywords", keywords);
		Criteria[] columnsSeach = dataTablesUtils.createCriteriaFromQueryStringColumns();
		Criteria[] allCriterias = null;
		if (searchFilterCriteria.length > 0) {
			allCriterias = dataTablesUtils.andCriteriaOperator(searchFilterCriteria, columnsSeach);
		} else {
			allCriterias = columnsSeach;
		}
		List<Criteria> filters = dto.getFilters().stream().filter(x -> !(x.getName().equals("keyword") || x.getName().equals("ontology") ) ).map(x -> {
			 return x.mongoFilter();
		}).collect(Collectors.toList());
		
		DBObject projections = new BasicDBObject();  
		dto.getScores().stream().filter(x -> !(x.getName().equals("keyword") || x.getName().equals("ontology") ) ).forEach(x -> {
			 x.mongoProject(projections);
		});
		
		
		Criteria arrFilters[] = new Criteria[filters.size()];
		allCriterias = concat( allCriterias , filters.toArray(arrFilters)); 
		
		Criteria filtered = null;
		if (allCriterias.length == 0) {
			filtered = new Criteria();
		} else if (allCriterias.length == 1) {
			filtered = allCriterias[0];
		} else {
			
			filtered = new Criteria().andOperator( allCriterias)  ;
		}

		 

		PaginatedResult<SearchProtDoc> result = new PaginatedResult<>();
		printDuration(startTime, "preparacion query");

		List<SearchProtDoc> data = fillData(startTime, filtered,projections,(dto.getScores().size() == 0) ?  perPage: 0,offset);		
		
		
		Set<String> keyset = dto.getScores().stream().filter(x -> (x.getName().equals("keyword") || x.getName().equals("ontology") ) ).map(x -> {
			return x.getValue();
		}).collect(Collectors.toSet());;
		
		
		printDuration(startTime, "query2");
		
		result.setRecordsTotal(protCount);


		printDuration(startTime, "filtro de resultado query");
		result.setRecordsFiltered( this.geneProductRepository.count( new Query(filtered) ) );
		final List<DruggabilityParam> scores = dto.getScores();
		data = sortData(dto, data);

		
		List<SearchProtDoc> subList = data.subList((dto.getScores().size() == 0) ?   0:offset,
				(data.size() > offset + perPage) ? (offset + perPage) : data.size());

		
		subList.forEach(x -> {
			ArrayList<String> keywords2 = new ArrayList<String>();
			if(keyset.size() > 0){
				HashSet<String> hashSet = new HashSet<>(x.getKeywords());
				hashSet.retainAll(keyset  );
				keywords2.addAll( hashSet);	
			}  
			x.setKeywords(keywords2);
			
			
			Map<String, Object> completeSearch = x.getSearch();
			x.setSearch(new HashMap<>());
			scores.stream().forEach(score -> {

				if (score.getUploader().equals("demo")) {
					x.getSearch().put(score.getName(), completeSearch.get(score.getName()));
				} else {
					x.getSearch().put(score.getUploader(), completeSearch.get(score.getUploader()));
				}

			});
			x.getSearch().put("druggability", completeSearch.get("druggability"));
		});

		result.setData(subList);

		printDuration(startTime, "quitar datos");

		return mapperJson.writeValueAsString(result);

	}

	
	
	private List<SearchProtDoc> fillData(long startTime, Criteria filtered, DBObject projections, int limit, Integer offset ) {
		List<SearchProtDoc> data = new ArrayList<>();
		
		Arrays.asList(new String[]{"_id","search.druggability","gene","organism","name","size.len","description","reactions","keywords"}).forEach(x ->{
			projections.put(x,true);	
		}); 
		
		DBCursor find = mongoTemplate.getCollection("proteins").find(
				new Query(filtered).getQueryObject(),projections);
		
		if (limit != 0){
			find.limit(limit);
			find.skip(offset);
		}
		
		Integer pepe = 1;
		for (DBObject dbObject : find) {
			if ((pepe % 100) == 0){
				printDuration(startTime, "pepe: " + pepe.toString());	
			}			
			pepe++;
			data.add(new SearchProtDoc(dbObject, mongoTemplate));
			
		}
		return data;
	}

	private void printDuration(Long startTime, String msg) {
		Long endTime = System.nanoTime();
		System.err.println(msg + ":" + new Double(((endTime - startTime) / 1000000000.0)).toString());
	}

	private static <E extends Scoreable> List<E> sortData(ScoreDTO dto, List<E> data) {
		final List<DruggabilityParam> scores = dto.getScores();

		if (scores.size() > 0) {
			data.stream().forEach(p -> {
				p.updateScore(scores);
			});
			data = data.stream().sorted((u1, u2) -> {
				// return Integer.signum( (int) Math.round( (u1.getScore() >
				// u2.getScore())) );
				if (u1.getScore() > u2.getScore())
					return -1;
				else if (u1.getScore() < u2.getScore())
					return 1;
				else
					return 0;
			}).collect(Collectors.toList());

			data.stream().forEach(p -> {

				DecimalFormat df = new DecimalFormat("#.##");

				p.setScore(Double.valueOf(df.format(p.getScore())));
			});

		}
		return data;
	}

	private PaginatedResult<SearchProtDoc> defaultSearch(Integer perPage, Integer offset, String search,
			List<String> auth, long protCount) {
		int queryOffset = new Long(new Double(Math.ceil(offset / perPage)).longValue()).intValue();
		Criteria[] searchFilterCriteria = dataTablesUtils.createCriteriaFromQueryString("keywords", search);
		Criteria[] columnsSeach = dataTablesUtils.createCriteriaFromQueryStringColumns();
		Criteria[] allCriterias = null;
		if (searchFilterCriteria.length > 0) {
			allCriterias = dataTablesUtils.andCriteriaOperator(searchFilterCriteria, columnsSeach);
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
		// Sort sortObj = dataTablesUtils.createSortFromQueryString();
		Sort sortObj = null;
		PaginatedResult<SearchProtDoc> result = DataTablesUtils.queryCollection(queryOffset, perPage, sortObj, auth,
				filtered, SearchProtDoc.class, mongoTemplate, protCount);
		return result;
	}

	@RequestMapping(value = "/{genomeName}/druggability", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResult<GeneProductDoc> customSort(@PathVariable(value = "genomeName") String organism,
			DruggabilitySearch dSearch) {

		return null;
	}

}
