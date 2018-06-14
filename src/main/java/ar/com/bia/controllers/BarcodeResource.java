package ar.com.bia.controllers;

import ar.com.bia.entity.BarcodeDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;



	@Controller
	@RequestMapping("/barcode")
	public class BarcodeResource {

		@Autowired
		private MongoOperations mongoTemplate;


		@Autowired
		private ObjectMapper mapperJson;
		
		@RequestMapping(value = "/{processid}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
		public String organismGeneProds(@PathVariable("processid") String processId, Model model, Principal principal) throws JsonProcessingException {

			DBObject dbobjetc = this.mongoTemplate.getCollection("barcodes").findOne(new BasicDBObject("processid",processId));
			((BasicDBObject)dbobjetc.get("taxonomy")).removeField("identification_provided_by");
			((BasicDBObject)dbobjetc.get("taxonomy")).removeField("identification_method");
			
			//dbobjetc.put("taxonomy",removeField);
			BarcodeDoc bc = this.mongoTemplate.getConverter().read( BarcodeDoc.class,dbobjetc);
			model.addAttribute("user", principal);			
			model.addAttribute("bc",bc);

			return "sndg/barcode";
		}
	}
