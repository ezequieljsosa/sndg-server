package ar.com.bia.controllers.services;

import ar.com.bia.backend.dao.GeneDocumentRepository;
import ar.com.bia.backend.dao.StructureRepository;
import ar.com.bia.entity.ContigDoc;
import ar.com.bia.entity.GeneDoc;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.pdb.StructureDoc;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.MessageFormat;

@Controller
@RequestMapping("/redirect")
public class RedirectService {

	private @Autowired
	MongoOperations mongoTemplate;
	private @Autowired
	HttpServletRequest request;

	private @Autowired
	GeneDocumentRepository geneRepository;

	@Autowired
	private StructureRepository structureRepository;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String search(
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "organism", required = false) String organism)
			throws URISyntaxException, UnsupportedEncodingException {

		String uri = request.getRequestURL().toString().split("rest")[0];

		if (type.equals("filter")) {
			uri = this.redirectToSeach(organism, key, value, uri);
		} else if (type.equals("structure")) {
			uri = this.redirectToSructure(key, value, uri);
		} else if (type.equals("job")) {
			uri = this.redirectToJob(key, value, uri);
		} else if (type.equals("pathway")) {
			uri = this.redirectToPathway(key, value, organism, uri);
		} else {
			uri = this.redirectToSequence(type, key, value, uri);
		}
		return "redirect:" + new URI(uri).toString();
	}

	private String redirectToPathway(String key, String value, String organism,String uri) throws UnsupportedEncodingException {
		if (key.equals("pathway")) {
		
				return "genome/" +  URLEncoder.encode(organism,java.nio.charset.StandardCharsets.UTF_8.toString()) + "/pathway/" + value;
			
		}
		throw new RuntimeException("invalid job type");
	}

	private String redirectToJob(String key, String value, String uri) {
		if (key.equals("blast")) {
			return "../views/Blast.jsp?job=" + value;
		}
		if (key.equals("msa")) {
			return "../views/MultipleAligment.jsp?job=" + value;
		}
		throw new RuntimeException("invalid job type");
	}

	private String redirectToSructure(String key, String value, String uri) {
		StructureDoc struct = null;
		String sturct_id = value.toLowerCase();
		if (key.equals("id")){
			 struct = structureRepository.findOne(sturct_id);
		} else {
			 struct = structureRepository.findByName(sturct_id);
		}
		
		
		
		uri += MessageFormat.format(
				"views/Structure.jsp?structure={0}&seqId={1}", struct.getId(),
				struct.getSeqId());

		return uri;
	}

	private String redirectToSeach(String organism, String key, String value,
			String uri) throws UnsupportedEncodingException {
		assert (key.equals("keywords"));
		assert (organism != null);
		uri += MessageFormat.format(
				"views/Search.jsp?organism={0}&keywords={1}",
				URLEncoder.encode(organism,java.nio.charset.StandardCharsets.UTF_8.toString()), URLEncoder.encode(value,java.nio.charset.StandardCharsets.UTF_8.toString()));

		return uri;
	}

	public String redirectToSequence(String type, String key, String value,
			String uri) {
		assert (key.equals("symbol") || key.equals("_id"));
		assert (type.equals("gene") || type.equals("protein"));

		if (type.equals("gene")) {
			// ContigDoc gene = mongoTemplate.findOne(
			// new Query(Criteria.where("features." + key).is(new
			// ObjectId(value))), ContigDoc.class);
			GeneDoc gene = null;
			if (key.equals("name")) {
				String valueToSearch = value;
				if (value.contains(":"))
					valueToSearch = valueToSearch.split(":")[0];
				gene = geneRepository.findByName(value);
			} else {
				gene = geneRepository.findOne(value);
			}

			if (gene == null) {
				uri += "views/GenomeList.jsp";
			} else {
				uri += "views/Gene.jsp?gene=" + gene.getId();
			}
		} else if (type.equals("protein") || type.equals("product")) {
			Object valueToSearch = value;
			GeneProductDoc protein = null;
			if (key.endsWith("_id")) {
				valueToSearch = new ObjectId(value);
			}
			if (value.toString().contains(":"))
				valueToSearch = valueToSearch.toString().split(":")[0];
			protein = this.mongoTemplate.findOne(new Query(Criteria.where(key)
					.is(valueToSearch)), GeneProductDoc.class);
			if (protein == null) {
				ContigDoc gene = this.mongoTemplate.findOne(new Query(Criteria
						.where(key).is(value)), ContigDoc.class);
				uri += "views/Gene.jsp?gene=" + gene.getId();
			} else {
				uri += "views/Protein.jsp?protein=" + protein.getId();
			}
		} else if (type.equals("collection")) {
			Object valueToSearch = value;
			if (key.endsWith("_id")) {
				valueToSearch = new ObjectId(value);
			}
			SeqCollectionDoc collection = this.mongoTemplate.findOne(new Query(
					Criteria.where(key).is(valueToSearch)),
					SeqCollectionDoc.class);
			uri += "views/Genome.jsp?genome=" + collection.getId();
		}
		return uri;
	}

}
