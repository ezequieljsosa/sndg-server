package ar.com.bia;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import ar.com.bia.dto.BlastParameters;

/**
 * @author eze
 * 
 */
public class BlastRunner {

	// seq;
	// List<String> genomes;
	// String matrix;
	// Integer gap_open;
	// Integer gap_extend;
	// Float max_evalue;
	// Boolean low_complexity;
	// Integer max_results;

	private String template = "{9} -query {0} -db {1} -max_target_seqs {2}  -evalue {3} -gapopen {4} -gapextend {5} -outfmt {6} -soft_masking {7} -out {8} ";

	private String databaseFile;
	private String queryFile;
	private String resultFile;
	private String outputFormat = "6";
	private String rawOutput;

	private String makeBlastDbtype = "prot";
	private String blastType = "blastp";

	public BlastRunner(String blastType) {
		super();
		assert blastType != null;
		assert Arrays.asList(new String[] { "tblastn", "blastx", "blastp", "blastn" }).contains(blastType);
		this.blastType = blastType;
		// if (blastType.equals("blastp")){
		// this.makeBlastDbtype = "prot";
		// template = template + " -matrix {9}";
		// } else {
		// this.makeBlastDbtype = "nucl";
		// }

	}

	public void runBlast(BlastParameters parameters) throws IOException, InterruptedException {

		// -query {0}
		// -db {1}
		// -max_target_seqs {2}
		// -evalue {3}
		// -gapopen {4}
		// -gapextend {5}

		// -outfmt {6}
		// -soft_masking {7}
		// -out {8}

		// -matrix {9}

		// if (!new File(this.getDatabaseFile() + ".phr").exists()){
		//
		//
		//
		// String command = "makeblastdb -dbtype " + this.makeBlastDbtype + "
		// -in "
		// + this.getDatabaseFile();
		// CommandLine cmdLine = CommandLine.parse(command);
		// DefaultExecutor executor = new DefaultExecutor();
		// int exitValue = executor.execute(cmdLine);
		// if (exitValue != 0)
		// throw new RuntimeException("Error running makeblastdb");
		// }

		String command = "";

		command = MessageFormat.format(template, this.getQueryFile(), this.getDatabaseFile(),
				parameters.getMax_results(), String.format("%f", parameters.getMax_evalue()), parameters.getGap_open(),
				parameters.getGap_extend(), this.getOutputFormat(), parameters.getLow_complexity(),
				this.getResultFile(), this.blastType);

		// "blastp -query {0} -db {1} -max_target_seqs {2} -evalue {3} -gapopen
		// {4} -gapextend {5} -matrix {6} -outfmt {7} -nofilter {8} -out {9}";
		// p = Runtime.getRuntime().exec(command);
		// p.waitFor();

		CommandLine cmdLine = CommandLine.parse(command);
		DefaultExecutor executor = new DefaultExecutor();
		int exitValue = executor.execute(cmdLine);

		// if (p.exitValue() != 0) {
		if (exitValue != 0) {

			throw new RuntimeException("Error running " + this.blastType);
			// + this.streamToString(p.getErrorStream()));
		} else {
			byte[] encoded = Files.readAllBytes(Paths.get(this.getResultFile()));
			this.rawOutput = new String(encoded, "UTF-8");
		}

	}

	public String streamToString(InputStream is) throws IOException {
		StringBuffer output = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line = "";
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}
		return output.toString();
	}

	public List<Map<String, String>> table(String database) {

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (String rawLine : this.rawOutput.split("\n")) {
			// 0 Query The query sequence id
			// 1 Subject The matching subject sequence id
			// 2 % id
			// 3 alignment length
			// 4 mistmatches
			// 5 gap openings
			// 6 q.start
			// 7 q.end
			// 8 s.start
			// 9 s.end
			// 10 e-value
			// 11 bit score
			if (!rawLine.trim().isEmpty()) {

				String[] line = rawLine.trim().split("\t");

				Map<String, String> record = new HashMap<String, String>();
				String query = line[1];
				
				

				record.put("query", query);
				record.put("hit", line[1]);
				record.put("length", line[3]);
				record.put("score", line[11]);
				record.put("identity", line[2]);
				record.put("evalue", line[10]);
				
				record.put("qstart", line[6]);
				record.put("qend", line[7]);
				record.put("sstart", line[8]);
				record.put("send", line[9]);
				
				result.add(record);
			}

		}

		return result;
	}

	public String getDatabaseFile() {
		return databaseFile;
	}

	public void setDatabaseFile(String databaseFile) {
		this.databaseFile = databaseFile;
	}

	public String getQueryFile() {
		return queryFile;
	}

	public void setQueryFile(String queryFile) {
		this.queryFile = queryFile;
	}

	public String getResultFile() {
		return resultFile;
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

}
