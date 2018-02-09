package ar.com.bia.controllers;


import ar.com.bia.entity.BarcodeDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dbstats")
public class DBStats {

    @Autowired
    private MongoOperations mongoTemplate;


    @Autowired
    private ObjectMapper mapperJson;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String stats(@RequestParam(value = "tour", defaultValue = "") String tour,
                        Principal principal) throws JsonProcessingException {

        DBCursor genomes = this.mongoTemplate.getCollection("sequence_collection")
                .find(new BasicDBObject("submitters", new BasicDBObject("$exists", 1)),
                        new BasicDBObject("submitters", 1).append("name", 1)
                                .append("organism", 1)
                                .append("_id", 0));

        List<DBObject> collections = new ArrayList<>();
        for (DBObject t: genomes) {
            collections.add(t);
        }

        return mapperJson.writeValueAsString(collections);
    }

}
