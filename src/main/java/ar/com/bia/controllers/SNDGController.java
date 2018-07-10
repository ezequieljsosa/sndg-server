package ar.com.bia.controllers;

import ar.com.bia.config.CollectionConfig;
import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.entity.*;
import ar.com.bia.pdb.StructureDoc;
import ar.com.bia.services.exception.OrganismNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SNDGController {

	@Autowired
	private MongoOperations mongoTemplate;
	@Autowired
	private MongoOperations mongoTemplateStruct;

	@Autowired
	private ObjectMapper mapperJson;

	public Map<String, CollectionConfig> typesMap() {
		Map<String, CollectionConfig> types = new HashMap<>();

		types.put("seq", new CollectionConfig("seq", "contig_colletion", ContigDoc.class, mongoTemplate));
		types.put("genome",
				new CollectionConfig("genome", "sequence_collection", SeqCollectionDoc.class, mongoTemplate));
		types.put("struct", new CollectionConfig("struct", "structures", StructureDoc.class, mongoTemplateStruct));
		types.put("tool", new CollectionConfig("tool", "tools", ToolDoc.class, mongoTemplate));
		types.put("prot", new CollectionConfig("prot", "proteins", GeneProductDoc.class, mongoTemplate));
		types.put("barcode", new CollectionConfig("barcode", "barcodes", BarcodeDoc.class, mongoTemplate));
		return types;
	}

	@RequestMapping(value = { "results" }, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String resultsTable(@RequestParam(value = "query", defaultValue = "") String query,
			@RequestParam(value = "type", defaultValue = "seq") String type,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer perPage,

			Model model, Principal principal) {

		model.addAttribute("user", principal);
		model.addAttribute("query", query);
		model.addAttribute("datatype", type);

		if (type.equals("all")) {
			Set<String> keywords = extractKw(query);

			typesMap().keySet().forEach(k -> {
				model.addAttribute(k, queryCount(k, typesMap(), keywords));
			});

			return "sndg/search";
		} else {

			model.addAttribute("page", new Integer(offset / perPage));
			return "sndg/results";

		}

	}

	// @Cacheable(cacheNames = "queries", key = "T(ar.com.bia.MD5).hash(#search
	// , #perPage.toString() + '_' + #offset.toString(),#request,#httpSession)")
	@RequestMapping(value = "/data_result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String listData(@RequestParam(value = "type", defaultValue = "seq") String type,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer perPage,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "query", defaultValue = "") String query, Principal principal,
			HttpSession httpSession, HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException, OrganismNotFoundException {

		Set<String> keywords = extractKw(query);

		long count = typesMap().get(type).getMongoTemplate().count(new Query(), typesMap().get(type).getClazz());

		PaginatedResult<DBObject> result = new PaginatedResult<>();
		result.setRecordsTotal(count);

		result.setRecordsFiltered(queryCount(type, typesMap(), keywords));

		BasicDBObject projection = new BasicDBObject().append("name", 1).append("description", 1).append("organism", 1)
				.append("url", 1).append("ncbi_assembly", 1).append("processid", 1);

		List<DBObject> list = queryList(type, perPage, offset, typesMap(), keywords, projection);
		
		if(type.equals("prot")){
			list.stream().forEach(p -> {
				DBObject genome = this.mongoTemplate.getCollection("sequence_collection").findOne(
						new BasicDBObject("name",p.get("organism")),new BasicDBObject("description",1));
				p.put("organism",genome.get("description"));
			});
		}
		
		result.setData(list);

		return mapperJson.writeValueAsString(result);

	}

	private List<DBObject> queryList(String type, Integer perPage, Integer offset, Map<String, CollectionConfig> types,
			Set<String> keywords, BasicDBObject projection) {

		BasicDBList keyquery = new BasicDBList();
		keywords.forEach(x -> keyquery.add(new BasicDBObject("keywords", x)));

		List<DBObject> list = types.get(type).getMongoTemplate().getCollection(types.get(type).getCollection())
				.find(new BasicDBObject("$and", keyquery), projection).limit(perPage).skip(offset).toArray();
		list.stream().forEach(x -> {
			x.put("_id", x.get("_id").toString());

		});
		return list;
	}

	private long queryCount(String type, Map<String, CollectionConfig> types, Set<String> keywords) {
		BasicDBList keyquery = new BasicDBList();
		keywords.forEach(x -> keyquery.add(new BasicDBObject("keywords", x)));
		return types.get(type).getMongoTemplate().getCollection(types.get(type).getCollection())
				.count(new BasicDBObject("$and", keyquery));
	}

	private Set<String> extractKw(String query) {
		return Arrays.stream(query.trim().split(" ")).map(x -> x.trim().toLowerCase()).filter(x -> x.length() > 1)
				.collect(Collectors.toSet());
	}

}
