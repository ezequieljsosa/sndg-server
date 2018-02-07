package ar.com.bia.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.backend.dao.impl.JobsRepositoryImpl;
import ar.com.bia.controllers.services.SessionService;
import ar.com.bia.entity.JobDoc;
import ar.com.bia.entity.Sequence;
import ar.com.bia.entity.UserDoc;
import ar.com.bia.services.UserService;

@Controller
@RequestMapping("/tool")
public class ToolResourse {

	@Autowired
	private UserService userService;



	@Autowired
	private GeneProductDocumentRepository geneProductRepository;

	@Autowired
	private JobsRepositoryImpl jobsRepository;

	@Autowired
	private ObjectMapper mapperJson;

	private @Autowired
	SessionService sessionService;

	@RequestMapping(value = "blast", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String blast(@RequestParam(value = "seq_name", defaultValue = "") String seqName,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "end", defaultValue = "-1") Integer end,
			Model model,Principal principal) throws JsonProcessingException {

		UserDoc user = this.userService.findUser(principal.getName());
		List<String> organisms =  this.userService.organisms(user);
		Sequence seq = null;
		if (!seqName.isEmpty()) {
			seq = this.geneProductRepository.findByName(seqName);
		}

		model.addAttribute("user",principal);
		model.addAttribute("start",start);
		model.addAttribute("end",end);
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));
		model.addAttribute("job", "null");
		model.addAttribute("organisms", this.mapperJson.writeValueAsString(organisms));
		if (seq != null) {
			model.addAttribute("seq", this.mapperJson.writeValueAsString(seq));
		} else {
			model.addAttribute("seq", "null");
		}

		return "process/Blast";
	}
	
	@RequestMapping(value = "blastn", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String blastg(@RequestParam(value = "seq_name", defaultValue = "") String seqName,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "end", defaultValue = "-1") Integer end,
			Model model,Principal principal) throws JsonProcessingException {

		UserDoc user = this.userService.findUser(principal.getName());
		List<String> organisms =  this.userService.organisms(user);
		Sequence seq = null;
		if (!seqName.isEmpty()) {
			seq = this.geneProductRepository.findByName(seqName);
		}

		model.addAttribute("user",principal);
		model.addAttribute("start",start);
		model.addAttribute("end",end);		
		model.addAttribute("job", "null");
		model.addAttribute("organisms", this.mapperJson.writeValueAsString(organisms));
		if (seq != null) {
			model.addAttribute("seq", this.mapperJson.writeValueAsString(seq));
		} else {
			model.addAttribute("seq", "null");
		}

		return "process/Blastg";
	}
	

	@RequestMapping(value = "job", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genome(Model model, Principal principal) {
		model.addAttribute("user",principal);
		return "process/JobList";
	}

	@RequestMapping(value = "msa", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String genomeGene(Model model, Principal principal) throws JsonProcessingException {

		model.addAttribute("user",principal);
		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));
		
		return "process/MultipleAligment";
	}

	@RequestMapping(value = "blastp/{jobId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String blastJob(
			@PathVariable(value = "jobId") String jobId,
			@RequestParam(value = "seq_name", defaultValue = "") String seqName, 
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "end", defaultValue = "-1") Integer end,			
			Model model, Principal principal)
					throws JsonProcessingException {

		
		UserDoc user = this.userService.findUser(principal.getName());
		Sequence seq = null;
		if (!seqName.isEmpty()) {
			seq = this.geneProductRepository.findByName(seqName);
		};
		JobDoc job = this.jobsRepository.findOne(jobId);
		List<String> organisms =  this.userService.organisms(user);

		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));
		model.addAttribute("user",principal);
		model.addAttribute("start",start);
		model.addAttribute("end",end);
		model.addAttribute("organisms", organisms);
		model.addAttribute("job", this.mapperJson.writeValueAsString(job));
		if (seq == null) {
			model.addAttribute("seq", "null");			
		} else {
			model.addAttribute("seq", this.mapperJson.writeValueAsString(seq));
		}

		return "process/Blast";
	}

	

	
	

	@RequestMapping(value = "maling/{jobId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String malingJob(@PathVariable(value = "jobId") String jobId, Model model, Principal principal)
					throws JsonProcessingException {

		UserDoc user = this.userService.findUser(principal.getName());
		
		JobDoc job = this.jobsRepository.findOne(jobId);
		List<String> organisms =  this.userService.organisms(user);

		model.addAttribute("sessionProts", mapperJson.writeValueAsString(sessionService.proteins()));
		model.addAttribute("organisms", organisms);
		model.addAttribute("job", this.mapperJson.writeValueAsString(job));

		

		return "process/MultipleAligment";
	}

}
