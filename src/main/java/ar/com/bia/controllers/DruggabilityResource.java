package ar.com.bia.controllers;

import ar.com.bia.controllers.exceptions.ForbiddenException;
import ar.com.bia.dto.druggability.DruggabilitySearch;
import ar.com.bia.entity.PropertyUpload;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.UserDoc;
import ar.com.bia.services.DruggabilityService;
import ar.com.bia.services.UserService;
import ar.com.bia.services.exception.PropFileLoadException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/druggability")
public class DruggabilityResource {


	@Autowired
	private ObjectMapper mapperJson;

	@Autowired
	private DruggabilityService druggabilityService;

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/{genome_name}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomes(@PathVariable("genome_name") String genomeName, Model model, Principal principal)
			throws JsonProcessingException, ForbiddenException {
		
		
		SeqCollectionDoc seqCol = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);
		
		UserDoc user = this.userService.findUser(principal.getName());
		if (!seqCol.getAuth().equals(UserDoc.publicUserId)) {
			if (!seqCol.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}
		
		

		model.addAttribute("genome_name", seqCol.getName());
		model.addAttribute("genomeDruggabilityParams", mapperJson.writeValueAsString(seqCol.getDruggabilityParams()));
		model.addAttribute("user", principal);
		
		return "omic/Druggability";
	}

	@RequestMapping(value = "/{genome_name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String, Object>> scoreTargets(@PathVariable("genome_name") String genomeName, 
			@RequestBody DruggabilitySearch ds, Principal principal) throws ForbiddenException {

		SeqCollectionDoc seqCol = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);
		
		UserDoc user = this.userService.findUser(principal.getName());
		if (!seqCol.getAuth().equals(UserDoc.publicUserId)) {
			if (!seqCol.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}
		
		int length = 10;
		if (!ds.getGenes().isEmpty()) {
			length = ds.getGenes().size();
		}
		return druggabilityService.druggabilityList(genomeName, ds, length);
	}

	@RequestMapping(value = "/properties/{genome_name}", method = RequestMethod.POST)
	public String uploadFile(@PathVariable("genome_name") String genomeName,   MultipartFile props_file,
			Model model, Principal principal) throws ForbiddenException {
		
		SeqCollectionDoc seqCol = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(genomeName)),
				SeqCollectionDoc.class);
		
		UserDoc user = this.userService.findUser(principal.getName());
		if (!seqCol.getAuth().equals(UserDoc.publicUserId)) {
			if (!seqCol.getAuth().equals(user.getAuthId())) {
				throw new ForbiddenException();
			}
		}
		
		List<String> errors = new ArrayList<String>();
		PropertyUpload upload = null;
		try {
			upload = druggabilityService.loadPropFiles(genomeName, props_file.getInputStream(),principal.getName());
			this.mongoTemplate.save(upload);
		} catch (PropFileLoadException e) {
			errors.add(e.getMessage());
		} catch (IOException e) {
			errors.add("error unknown reading uploaded file...");
		}
		
		model.addAttribute("uploadErrors",errors);
		model.addAttribute("isUpload",true);
		return "redirect:/genome/" + genomeName;
	}
	
	

}
