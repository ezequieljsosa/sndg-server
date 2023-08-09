package ar.com.bia.controllers;

import ar.com.bia.BioPage;
import ar.com.bia.DataTablesUtils;
import ar.com.bia.backend.dao.CompoundRepository;
import ar.com.bia.backend.dao.GeneDocumentRepository;
import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.dto.druggability.ScoreDTO;
import ar.com.bia.entity.*;
import ar.com.bia.services.UserService;
import ar.com.bia.services.exception.OrganismNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/compound")
public class CompoundResourse {

    @Autowired
    private CompoundRepository compoundRepository;

    @Autowired
    private ObjectMapper mapperJson;

    @Autowired
    private MongoTemplate mongoTemplate;

    /*@RequestMapping(value = "/compounds/{proteinID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CompoundDoc> compounds(@PathVariable("proteinID") String proteinId) {
        //new Query().with(pageable)
        //Pageable pageable
        return this.compoundRepository.findAll(new Query(Criteria.where("proteinId").is(proteinId)));
    }*/

    @RequestMapping(value = "/{organism}/{gene}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String compounds(@PathVariable("gene") String gene,
                            @PathVariable("organism") String organism,
                                       Model model, Principal principal) throws JsonProcessingException {
        /*UserDoc user = this.userService.findUser(principal.getName());
        List<String> auths2 = new ArrayList<String>();
        auths2.add(UserDoc.publicUserId.toString());
        auths2.add(user.getAuthId());
        Query query = new Query(Criteria.where("auth").in(auths2));
        model.addAttribute("user", principal);*/

       // Query proteinCompounds = new Query(Criteria.where("gene").is(gene).and("organism").is(organism));
       // List<CompoundDoc> all = this.compoundRepository.findAll(proteinCompounds);
       // model.addAttribute("compounds", mapperJson.writeValueAsString(all));
        model.addAttribute("genome", organism);
        model.addAttribute("gene", gene);
        return "omic/CompoundList";
    }

    //@Cacheable(cacheNames = "queries", key = "T(ar.com.bia.MD5).hash(#search , #perPage.toString() + '_' + #offset.toString(),#request,#httpSession)")
    @RequestMapping(value = "/search/{organism}/{gene}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String compounds(

            @PathVariable("gene") String gene,
            @PathVariable("organism") String organism,

            @RequestParam(value = "length", defaultValue = "10") Integer perPage,
            @RequestParam(value = "start", defaultValue = "0") Integer offset,
            @RequestParam(value = "search[value]", defaultValue = "") String search, Principal principal,
            HttpSession httpSession, HttpServletRequest request)
            throws JsonParseException, JsonMappingException, IOException, OrganismNotFoundException {


        Query proteinCompounds = new Query(Criteria.where("gene").is(gene).and("organism").is(organism));
        Long count = this.compoundRepository.count(proteinCompounds);


        BioPage pageObj = new BioPage(offset, perPage, null);
        proteinCompounds.with(pageObj);

        List<CompoundDoc> compoundList = mongoTemplate.find(proteinCompounds, CompoundDoc.class);

        PaginatedResult<CompoundDoc> result = new PaginatedResult<CompoundDoc>();
        result.setRecordsTotal(count);
        result.setRecordsFiltered(count);
        result.setData(compoundList);
        return mapperJson.writeValueAsString( result);



    }



}
