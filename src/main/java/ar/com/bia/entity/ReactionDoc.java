package ar.com.bia.entity;

import java.util.List;

public class ReactionDoc {
	private List<String> pathways;
	private String name;
	private List<SpecieDoc> products;
	private List<SpecieDoc> substrates;
	
	public List<String> getPathways() {
		return pathways;
	}
	public void setPathways(List<String> pathways) {
		this.pathways = pathways;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SpecieDoc> getProducts() {
		return products;
	}
	public void setProducts(List<SpecieDoc> products) {
		this.products = products;
	}
	public List<SpecieDoc> getSubstrates() {
		return substrates;
	}
	public void setSubstrates(List<SpecieDoc> substrates) {
		this.substrates = substrates;
	}
	
	
	
}
