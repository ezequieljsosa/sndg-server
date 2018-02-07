package ar.com.bia.dto;

import java.util.Arrays;
import java.util.List;

public class BlastParameters {

	private static final List<String> MATRIXLIST = Arrays.asList("BLOSUM80",
			"BLOSUM62", "BLOSUM50", "BLOSUM45", "PAM250", "BLOSUM90", "PAM30",
			"PAM70");

	private String seq;
	private String database;
	private String matrix;
	private Integer gap_open;
	private Integer gap_extend;
	private Float max_evalue;
	private Boolean low_complexity;
	private Integer max_results;
	
	private String blastType;
	
	
	private String searchIn ;

	public BlastParameters() {
		super();
		
	}

	
	
	public String getDatabase() {
		return database;
	}



	public void setDatabase(String database) {
		this.database = database;
	}



	public BlastParameters(String seq, String database, String matrix,
			Integer gap_open, Integer gap_extend, Float max_evalue,
			Boolean low_complexity, Integer max_results) {
		super();
		this.seq = seq;
		this.database = database;
		this.matrix = matrix;
		this.gap_open = gap_open;
		this.gap_extend = gap_extend;
		this.max_evalue = max_evalue;
		this.low_complexity = low_complexity;
		this.max_results = max_results;
		
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	

	public String getBlastType() {
		return blastType;
	}

	public void setBlastType(String blastType) {
		this.blastType = blastType;
	}

	public String getMatrix() {
		return matrix;
	}

	public void setMatrix(String matrix) {
		if (MATRIXLIST.contains(matrix)) {
			this.matrix = matrix;
		} else {
			throw new IllegalArgumentException("Blast matrix not valid");
		}
	}

	public Integer getGap_open() {
		return gap_open;
	}

	public void setGap_open(Integer gap_open) {
		this.gap_open = gap_open;
	}

	public Integer getGap_extend() {
		return gap_extend;
	}

	public void setGap_extend(Integer gap_extend) {
		this.gap_extend = gap_extend;
	}

	public Float getMax_evalue() {
		return max_evalue;
	}

	public void setMax_evalue(Float max_evalue) {
		this.max_evalue = max_evalue;
	}

	public Boolean getLow_complexity() {
		return low_complexity;
	}

	public void setLow_complexity(Boolean low_complexity) {
		this.low_complexity = low_complexity;
	}

	public Integer getMax_results() {
		return max_results;
	}

	public void setMax_results(Integer max_results) {
		this.max_results = max_results;
	}

	

	public String getSearchIn() {
		return searchIn;
	}

	public void setSearchIn(String searchIn) {
		this.searchIn = searchIn;
	}
	
	

}
