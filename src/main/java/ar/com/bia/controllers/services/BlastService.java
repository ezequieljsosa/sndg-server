package ar.com.bia.controllers.services;

import ar.com.bia.BlastRunner;
import ar.com.bia.backend.dao.impl.JobsRepositoryImpl;
import ar.com.bia.dto.BlastJob;
import ar.com.bia.dto.BlastParameters;
import ar.com.bia.entity.JobDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/blast")
public class BlastService {

	// get log4j handler
	private static final Logger logger = Logger.getLogger(BlastService.class);

	private @Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private JobsRepositoryImpl jobsRepo;

	public BlastService() {
		super();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BlastJob blast(@RequestBody String parameters,Principal principal) throws IOException {
		logger.info("Blast Job received");
		final BlastParameters blastParameters = parseBlastParameters(parameters);
		
		final String name = (principal != null) ? principal.getName() : "guest" ;
		
		final File database = getDatabase( blastParameters.getDatabase());
		final File query = createQueryFile(blastParameters);

		final File result = File.createTempFile("temp-xomeq-", ".fasta");

		BlastJob job = new BlastJob();
		job.setDate(new Date());
		job.setUser(name);
		job.setStatus("new");
		job.setType(blastParameters.getBlastType());
		job.setPatameters(blastParameters);

		this.jobsRepo.save(job);

		final String jobid = job.getId();

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				JobDoc jobdb = jobsRepo.findOne(jobid);

				BlastRunner blastRunner = new BlastRunner(job.getType());
				blastRunner.setDatabaseFile(database.getAbsolutePath());
				blastRunner.setQueryFile(query.getAbsolutePath());
				blastRunner.setResultFile(result.getAbsolutePath());

				try {
					blastRunner.runBlast(blastParameters);

					jobdb.setResult(blastRunner.table(job.getPatameters().getDatabase()));
					jobdb.setStatus("finished");
					jobsRepo.save(jobdb);

				} catch (Exception ex) {
					List<Map<String, String>> result = new ArrayList<Map<String, String>>();
					Map<String, String> record = new HashMap<String, String>();
					record.put("hit",
							"error running blast: " + ex.getLocalizedMessage());
					result.add(record);

					jobdb.setResult(result);
					jobdb.setStatus("error");
					jobsRepo.save(jobdb);
				}

			}
		});
		thread.start();

		return job;
		// mongoTemplate.getConverter().read(BlastJob.class, job);
	}

	private File getDatabase( String database) {
		Map<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("SNDGNr","/data/databases/sndg/proteins.fasta");
		dataMap.put("SNDGNt","/data/databases/sndg/nucleotides.fasta");
		dataMap.put("PDB","/data/databases/sndg/structures.fasta");
		dataMap.put("Barcodes","/data/databases/sndg/barcodes.fasta");
		
		return new File(dataMap.get(database));
	}

	private BlastParameters parseBlastParameters(String parameters)
			throws UnsupportedEncodingException {
		String[] fields = parameters.split("&");
		String[] kv;
		DBObject dbObject = new BasicDBObject();
		for (int i = 0; i < fields.length; ++i) {
			kv = fields[i].split("=");
			if (2 == kv.length) {
				kv[0] = java.net.URLDecoder.decode(kv[0], "UTF-8")
						.replace("[", "").replace("]", "").trim();

				if (!kv[0].equals("_csrf")) {
					dbObject.put(kv[0], java.net.URLDecoder.decode(kv[1], "UTF-8").trim());
				}
			}
		}
		final BlastParameters blastParameters = mongoTemplate.getConverter()
				.read(BlastParameters.class, dbObject);
		return blastParameters;
	}

	private File createQueryFile(final BlastParameters blastParameters)
			throws IOException, FileNotFoundException,
			UnsupportedEncodingException {
		final File query = File.createTempFile("temp-xomeq-", ".fasta");
		PrintWriter writer = new PrintWriter(query, "UTF-8");
		writer.println(">query");
		writer.println(blastParameters.getSeq().trim());
		writer.close();
		return query;
	}	
	
	@RequestMapping(value = "/{jobid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JobDoc job(@PathVariable("jobid") String jobid) {
		JobDoc job = this.jobsRepo.findOne(jobid);
		return job;
	}

}
