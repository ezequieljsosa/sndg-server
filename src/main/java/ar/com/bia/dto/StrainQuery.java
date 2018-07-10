package ar.com.bia.dto;

import ar.com.bia.dto.druggability.DruggabilityParam;

import java.util.List;


public class StrainQuery {

	

	
	private String reference;
	private List<String> strains;
	private List<String> variant_type;
	private List<StrainComp> strainComp;
	
	public List<DruggabilityParam> proteinFilters; 
	
	
	
	
	private int offset;
	private int pageNumber;
	private int pageSize;
	
	
	
	public StrainQuery() {
		super();
		this.pageNumber = 0;
		this.pageSize = 50;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public List<String> getStrains() {
		return strains;
	}
	public void setStrains(List<String> strains) {
		this.strains = strains;
	}
	
	
	
	public List<StrainComp> getStrainComp() {
		return strainComp;
	}
	public void setStrainComp(List<StrainComp> strainComp) {
		this.strainComp = strainComp;
	}
	public List<String> getVariant_type() {
		return variant_type;
	}
	public void setVariant_type(List<String> variant_type) {
		this.variant_type = variant_type;
	}
	
	public List<DruggabilityParam> getProteinFilters() {
		return proteinFilters;
	}
	public void setProteinFilters(List<DruggabilityParam> proteinFilters) {
		this.proteinFilters = proteinFilters;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
	
}
