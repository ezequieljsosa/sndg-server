package ar.com.bia.controllers.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.bia.backend.dao.impl.JobsRepositoryImpl;
import ar.com.bia.dto.MSAJob;
import ar.com.bia.entity.JobDoc;

@Controller
@RequestMapping("msa")
public class ClustalService {

	// get log4j handler
	private static final Logger logger = Logger.getLogger(ClustalService.class);



	@Autowired
	private JobsRepositoryImpl jobsRepo;



	public ClustalService() {
		super();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MSAJob blast(String fasta) throws IOException {
		logger.info("MSA job received");
		final String txt = fasta.replace("&gt;", ">");

		User authenticatedUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		final String name = authenticatedUser.getUsername();

		final MSAJob job = new MSAJob();
		job.setId(new ObjectId().toString());
		job.setStatus("initialized");
		job.setDate(new Date());
		job.setUser(name);
		jobsRepo.save(job);

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				String file_path = null;
				try {
					file_path = writeFile(txt);
				} catch (IOException ex) {
					Map<String, String> record = new HashMap<String, String>();
					job.getResult().add(record);

					record.put("error",
							"error running msa: " + ex.getLocalizedMessage());
					jobsRepo.save(job);

					return;
				}
				Process p = null;
				try {
					p = Runtime.getRuntime().exec(
							"clustalo -i " + file_path + " -o " + file_path
									+ ".out");
					job.setStatus("running");
					jobsRepo.save(job);
					p.waitFor();
					job.setStatus("parsing");
					jobsRepo.save(job);
					String output = new String(Files.readAllBytes(Paths.get(file_path
							+ ".out")));
					this.parseResults(
							output, job);
					job.setStatus("finished");
					jobsRepo.save(job);

				} catch (Exception ex) {

					Map<String, String> record = new HashMap<String, String>();
					job.getResult().add(record);

					record.put("error",
							"error running msa: " + ex.getLocalizedMessage());
					jobsRepo.save(job);
				} finally{
					try {
						p.getOutputStream().close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			private void parseResults(String string, MSAJob currentjob) {
				
				for (String str : string.split("\n>")) {
					
					if(!str.trim().isEmpty()){
						Map<String,String> seqmap = new HashMap<String,String>();
						String[] lines = str.split("\n");
						seqmap.put("id", lines[0]);
						String[] seq = Arrays.copyOfRange(lines, 1, lines.length) ;
						StringBuilder sb = new StringBuilder();
						for (String subseq : seq) {
							sb.append(subseq.replace("\n", "").trim());
						}
						seqmap.put("name", lines[0].trim());
						seqmap.put("seq", sb.toString());
						currentjob.getResult().add(seqmap);
					}
					
				}
			}

			private String writeFile(final String txt) throws IOException {
				BufferedWriter writer = null;
				try {
					String filename = "/tmp/"
							+ new BigInteger(130, new Random()).toString(32)
							+ ".fasta";
					writer = new BufferedWriter(new FileWriter(filename));
					writer.write(txt);
					return filename;
				} finally {
					if (writer != null)
						writer.close();
				}
			}
		});
		thread.start();

		return job;

	}

	@RequestMapping(value = "/{jobid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JobDoc job(@PathVariable("jobid") String jobid) {

		User authenticatedUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		String name = authenticatedUser.getUsername();
		

		JobDoc job = this.jobsRepo.findOne(jobid);
		assert name == job.getUser();
		return job;
	}

	

}
