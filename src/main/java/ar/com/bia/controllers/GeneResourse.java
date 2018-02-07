package ar.com.bia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.bia.backend.dao.GeneDocumentRepository;
import ar.com.bia.entity.GeneDoc;

@Controller
@RequestMapping("/gene")
public class GeneResourse {

	@Autowired
	private GeneDocumentRepository repositoryGene;

	@RequestMapping(value = "/{locus_tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public GeneDoc gene(@PathVariable("locus_tag") String geneId) {
		return this.repositoryGene.findOne(geneId);
	}

}
