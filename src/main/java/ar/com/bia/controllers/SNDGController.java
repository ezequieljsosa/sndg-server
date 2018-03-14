package ar.com.bia.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import ar.com.bia.config.CollectionConfig;
import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.entity.BarcodeDoc;
import ar.com.bia.entity.ContigDoc;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.ToolDoc;
import ar.com.bia.pdb.StructureDoc;
import ar.com.bia.services.exception.OrganismNotFoundException;

@Controller
@RequestMapping("/search")
public class SNDGController {

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private MongoOperations mongoTemplateStruct;

    @Autowired
    private ObjectMapper mapperJson;

    public static Map<String, BiConsumer<String, DBObject>> reqFilters() {
        Map<String, BiConsumer<String, DBObject>> map = new HashMap<>();
        map.put("species", (value, dbObject) -> {
            dbObject.put("sndg_index.tax", value.toLowerCase());
        });
        map.put("taxonomia", (value, dbObject) -> {
            dbObject.put("sndg_index.tax", value.toLowerCase());
        });
        map.put("ensayo", (value, dbObject) -> {
            dbObject.put("experiment", "/" + value.toLowerCase() + "/");
        });
        map.put("has_ligand", (value, dbObject) -> {
            dbObject.put("ligands.0", new BasicDBObject("$exists",true));
        });
        map.put("has_structure", (value, dbObject) -> {
            dbObject.put("keywords", "has_structure");
        });
        map.put("length", (value, dbObject) -> {
            dbObject.put("size.len", value.toLowerCase());
        });
        map.put("assembly_level", (value, dbObject) -> {
            dbObject.put("assembly_level", value.toLowerCase());
        });
        map.put("markercode", (value, dbObject) -> {
            dbObject.put("sequences.sequence.markercode", value.toLowerCase());
        });
        map.put("tool_type", (value, dbObject) -> {
            dbObject.put("type", value.toLowerCase());
        });
        return map;
    }

    private Map<String, BiConsumer<String, DBObject>> reqFilters = reqFilters();


    public Map<String, CollectionConfig> typesMap() {
        Map<String, CollectionConfig> types = new HashMap<>();

        types.put("seq", new CollectionConfig("seq", "contig_collection", ContigDoc.class, mongoTemplate));
        types.put("genome",
                new CollectionConfig("genome", "sequence_collection", SeqCollectionDoc.class, mongoTemplate));
        types.put("struct", new CollectionConfig("struct", "structures", StructureDoc.class, mongoTemplateStruct));
        types.put("tool", new CollectionConfig("tool", "tools", ToolDoc.class, mongoTemplate));
        types.put("prot", new CollectionConfig("prot", "proteins", GeneProductDoc.class, mongoTemplate));
        types.put("barcode", new CollectionConfig("barcode", "barcodes", BarcodeDoc.class, mongoTemplate));
        return types;
    }

    @RequestMapping(value = {"results"}, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String resultsTable(@RequestParam(value = "query", defaultValue = "") String query,
                               @RequestParam(value = "type", defaultValue = "seq") String type,
                               @RequestParam(value = "start", defaultValue = "0") Integer offset,
                               @RequestParam(value = "pageSize", defaultValue = "50") Integer perPage,

                               Model model, Principal principal) {

        model.addAttribute("user", principal);
        model.addAttribute("query", query);
        model.addAttribute("datatype", type);

        if (type.equals("all")) {
            Set<String> keywords = extractKw(query);

            typesMap().keySet().forEach(k -> {
                model.addAttribute(k, queryCount(k, typesMap(), keywords, new HashMap<>()));
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
                           @RequestParam(value = "pageSize", defaultValue = "50") Integer perPage,
                           @RequestParam(value = "start", defaultValue = "0") Integer offset,
                           @RequestParam(value = "query", defaultValue = "") String query,
                           @RequestParam Map<String, String> reqParams)
            throws IOException {

        Set<String> keywords = extractKw(query);

        long count = typesMap().get(type).getMongoTemplate().count(new Query(), typesMap().get(type).getClazz());

        PaginatedResult<DBObject> result = new PaginatedResult<>();
        result.setRecordsTotal(count);


        result.setRecordsFiltered(queryCount(type, typesMap(), keywords, reqParams));


        BasicDBObject projection = new BasicDBObject().append("name", 1).append("description", 1).append("organism", 1)
                .append("url", 1).append("ncbi_assembly", 1).append("processid", 1);

        List<DBObject> list = queryList(type, perPage, offset, typesMap(), keywords, projection, reqParams);

        if (type.equals("prot")) {
            list.stream().forEach(p -> {
                DBObject genome = this.mongoTemplate.getCollection("sequence_collection").findOne(
                        new BasicDBObject("name", p.get("organism")), new BasicDBObject("description", 1));
                p.put("organism", genome.get("description"));
            });
        }

        result.setData(list);

        return mapperJson.writeValueAsString(result);

    }

    private List<DBObject> queryList(String type, Integer perPage, Integer offset, Map<String, CollectionConfig> types,
                                     Set<String> keywords, BasicDBObject projection,
                                     Map<String, String> reqParams) {
        DBObject query = getDbObjectQuery(keywords,reqParams);



        List<DBObject> list = types.get(type).getMongoTemplate().getCollection(types.get(type).getCollection())
                .find(query, projection).limit(perPage).skip(offset).toArray();
        list.stream().forEach(x -> {
            x.put("_id", x.get("_id").toString());

        });
        return list;
    }

    private long queryCount(String type, Map<String, CollectionConfig> types, Set<String> keywords,
                            Map<String, String> reqParams) {
        DBObject query = getDbObjectQuery(keywords,reqParams);


        return types.get(type).getMongoTemplate().getCollection(types.get(type).getCollection())
                .count(query);
    }

    private DBObject getDbObjectQuery(Set<String> keywords,Map<String, String> reqParams) {
        DBObject query;
        if (!keywords.isEmpty()) {
            BasicDBList keyquery = new BasicDBList();
            keywords.forEach(x -> keyquery.add(new BasicDBObject("keywords", x)));
            query = new BasicDBObject("$and", keyquery);
        } else {
            query = new BasicDBObject();
        }
        final DBObject queryf = query;
        reqParams.keySet().stream().forEach(x -> {
            if (reqFilters.containsKey(x)) {
                reqFilters.get(x).accept(reqParams.get(x), queryf);
            }
        });
        return query;
    }

    private Set<String> extractKw(String query) {
        return Arrays.stream(query.trim().split(" ")).map(x -> x.trim().toLowerCase()).filter(x -> x.length() > 1)
                .collect(Collectors.toSet());
    }

}
