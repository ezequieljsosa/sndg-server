package ar.com.bia.controllers;

import ar.com.bia.controllers.exceptions.ForbiddenException;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.UserDoc;
import ar.com.bia.services.KEGGService;
import ar.com.bia.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/kegg")
public class KEGGController {

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private ObjectMapper mapperJson;

    @Autowired
    private UserService userService;

    @Autowired
    private KEGGService kEGGService;


    @RequestMapping(value = "/{genome_name:.+}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String genome(@PathVariable("genome_name") String genomeName, Model model, Principal principal,
                         @RequestParam(value = "pathway", defaultValue = "ko00010") String pathway)
            throws Exception {


        SeqCollectionDoc genome = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
                SeqCollectionDoc.class);

        if (genome == null) {
            genome = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(genomeName))),
                    SeqCollectionDoc.class);
            genomeName = genome.getName();
        }


        UserDoc user = this.userService.findUser(principal.getName());
        if (!genome.getAuth().equals(UserDoc.publicUserId)) {
            if (!genome.getAuth().equals(user.getAuthId())) {
                throw new ForbiddenException();
            }
        }
        DBObject q = new BasicDBObject("organism", genome.getName());
        q.put("ontologies", pathway.replace("ko", "kegg_pw:"));

        DBObject project = new BasicDBObject("gene", 1);
        project.put("ontologies", "1");
        project.put("_id", "0");
        List<String[]> proteins = new ArrayList<>();
        String data = genome.getKegg().stream().filter(x -> x.getTerm().equals(pathway.replace("ko", "kegg_pw:")))
                .findFirst().get().getProperties2().get("kos").toString();
        data = data.substring(1, data.length() - 1);
        Collection<String> kos = new ArrayList<>();
        for (String x : data.split(",")) {
            kos.add(x.trim());
        }


        for (DBObject doc : this.mongoTemplate.getCollection("proteins").find(q, project)) {
            proteins.add(
                    new String[]{doc.get("gene").toString(),doc.get("ontologies").toString()}
            );

        }

        model.addAttribute("user", principal);
        model.addAttribute("logged_user", isValidUser(principal));
        model.addAttribute("genome", mapperJson.writeValueAsString(genome));
        model.addAttribute("proteins", mapperJson.writeValueAsString(proteins));
        model.addAttribute("pathway", pathway);
        model.addAttribute("kegg", mapperJson.writeValueAsString(kEGGService.keggPathwayData(pathway, kos)));


        return "omic/KEGG";
    }

//    @RequestMapping(value = "/{genome_name:.+}/genes/{kos}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String geneProducts(
//            @PathVariable("genome_name") String genomeName,
//            @PathVariable("kos") String kos,
//            Principal principal,
//            HttpSession httpSession, HttpServletRequest request)
//            throws Exception {
//
//
//
//        return mapperJson.writeValueAsString(result);
//    }

    private boolean isValidUser(Principal principal) {
        if ((principal.getName() == null) || (principal.getName().isEmpty()))
            return false;
        return !principal.getName().equals("demo");
    }
}
