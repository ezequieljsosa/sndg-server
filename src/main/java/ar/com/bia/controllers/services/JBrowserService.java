package ar.com.bia.controllers.services;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/jbrowse")
public class JBrowserService {
	
	

	@RequestMapping(value = "/stats/global", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String statsGlobal() {
		
		String str = "{" + "\"featureDensity\": 0.02, "
				+ "\"featureCount\": 234235,	" + "\"scoreMin\": 87,"
				+ "\"scoreMax\": 87," + "\"scoreMean\": 42,"
				+ "\"scoreStdDev\": 2.1" + "}";

		return str;
	}

	@RequestMapping(value = "/stats/region/{refseq_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String statsRegion(@PathVariable("refseq_name") String refseqName,
			@RequestParam(value="start",required=true) String start, @RequestParam(value="stop",required=true) String stop) {
		String str = "{" + "\"featureDensity\": 0.02, "
				+ "\"featureCount\": 234235,	" + "\"scoreMin\": 87,"
				+ "\"scoreMax\": 87," + "\"scoreMean\": 42,"
				+ "\"scoreStdDev\": 2.1" + "}";
		return str;
	}

	@RequestMapping(value = "/features/{refseq_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String features(@PathVariable("refseq_name") String refseqName,
			@RequestParam(value="start",required=true) String start, @RequestParam(value="stop",required=true) String stop) {
		String str = "{ 																													"
				+ "\"features\": [ 																								"
				+ "             { \"start\": 123, \"end\": 456 },																	"
				+ "             { \"start\": 123, \"end\": 456, \"score\": 42 },														"
				+ "             {\"seq\": \"gattacagattaca\", \"start\": 0, \"end\": 14},												"
				+ "             { \"type\": \"mRNA\", \"start\": 5975, \"end\": 9744, \"score\": 0.84, \"strand\": 1,						"
				+ "               \"name\": \"au9.g1002.t1\", \"uniqueID\": \"globallyUniqueString3\",									"
				+ "               \"subfeatures\": [																				"
				+ "                  { \"type\": \"five_prime_UTR\", \"start\": 5975, \"end\": 6109, \"score\": 0.98, \"strand\": 1 },		"
				+ "                  { \"type\": \"start_codon\", \"start\": 6110, \"end\": 6112, \"strand\": 1, \"phase\": 0 },			"
				+ "                  { \"type\": \"CDS\",         \"start\": 6110, \"end\": 6148, \"score\": 1, \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 6615, \"end\": 6683, \"score\": 1, \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 6758, \"end\": 7040, \"score\": 1, \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 7142, \"end\": 7319, \"score\": 1, \"strand\": 1, \"phase\": 2 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 7411, \"end\": 7687, \"score\": 1, \"strand\": 1, \"phase\": 1 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 7748, \"end\": 7850, \"score\": 1, \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 7953, \"end\": 8098, \"score\": 1, \"strand\": 1, \"phase\": 2 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 8166, \"end\": 8320, \"score\": 1, \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 8419, \"end\": 8614, \"score\": 1, \"strand\": 1, \"phase\": 1 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 8708, \"end\": 8811, \"score\": 1, \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 8927, \"end\": 9239, \"score\": 1, \"strand\": 1, \"phase\": 1 },"
				+ "                  { \"type\": \"CDS\",         \"start\": 9414, \"end\": 9494, \"score\": 1, \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"stop_codon\",  \"start\": 9492, \"end\": 9494,             \"strand\": 1, \"phase\": 0 },"
				+ "                  { \"type\": \"three_prime_UTR\", \"start\": 9495, \"end\": 9744, \"score\": 0.86, \"strand\": 1 }		"
				+ "               ]																								"
				+ "             }																								"
				+ "           ]																									"
				+ "         }																									";
		return str;
	}

}
