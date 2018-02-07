package ar.com.bia.dto.druggability;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class DruggabilitySearch {
	
	private List<DruggabilityParam> filters;
	private List<DruggabilityParam> formula;
	private List<String> genes;	
	
	private MultipartFile genesFile;
	private MultipartFile geneScoresFile;
	
	
	public DruggabilitySearch() {
		super();
		this.formula = new ArrayList<DruggabilityParam>();
		this.filters = new ArrayList<DruggabilityParam>();
		this.genes = new ArrayList<String>();
	}
		
	public List<DruggabilityParam> getFilters() {
		return filters;
	}

	public void setFilters(List<DruggabilityParam> filters) {
		this.filters = filters;
	}

	public List<DruggabilityParam> getFormula() {
		return formula;
	}

	public void setFormula(List<DruggabilityParam> formula) {
		this.formula = formula;
	}

	public List<String> getGenes() {
		return genes;
	}
	public void setGenes(List<String> genes) {
		this.genes = genes;
	}
	public MultipartFile getGenesFile() {
		return genesFile;
	}
	public void setGenesFile(MultipartFile genesFile) {
		this.genesFile = genesFile;
	}
	public MultipartFile getGeneScoresFile() {
		return geneScoresFile;
	}
	public void setGeneScoresFile(MultipartFile geneScoresFile) {
		this.geneScoresFile = geneScoresFile;
	}
	

	
	
}
