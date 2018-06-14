package ar.com.bia.controllers;

import ar.com.bia.config.CollectionConfig;
import ar.com.bia.entity.*;
import ar.com.bia.pdb.StructureDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/")
public class FrontController {

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
        types.put("bioproject", new CollectionConfig("bioproject", "bioprojects", BioprojectDoc.class, mongoTemplate));


        return types;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) throws JsonProcessingException {

        Map<String, Object> generalStats = new HashMap<String, Object>();

        typesMap().keySet().stream().forEach(x -> {
            CollectionConfig cc = typesMap().get(x);

            Query query = new Query();
            if(x.equals("struct")){
                query = new Query(Criteria.where("_cls").is("Structure.ExperimentalStructure"));
            }
            generalStats.put(x, cc.getMongoTemplate().count(query, cc.getClazz()));
        });

        List<DBObject> pipeline = Arrays.asList(new BasicDBObject("$group",
                new BasicDBObject("_id", "$type").append("count",
                        new BasicDBObject("$sum", 1))));
        AggregationOutput aggregate = mongoTemplate.getCollection("tools").aggregate(
                pipeline);


        Map<Object, Object> map = StreamSupport.stream(aggregate.results().spliterator(), false)
                .map(DBObject.class::cast).collect(Collectors.toMap(x -> x.get("_id"), y -> y.get("count")));


        model.addAttribute("tooltypes", mapperJson.writeValueAsString(map.keySet()));
        model.addAttribute("toolvalues", map.keySet().stream().map(x -> map.get(x)).collect(Collectors.toList()));

        model.addAttribute("generalStats", generalStats);

        return "user/main";

    }
}
