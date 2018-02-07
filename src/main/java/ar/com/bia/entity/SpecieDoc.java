package ar.com.bia.entity;

import java.util.List;

public class SpecieDoc {

	private String name;
	private List<String> producers;
	private List<String> consumers;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getProducers() {
		return producers;
	}
	public void setProducers(List<String> producers) {
		this.producers = producers;
	}
	public List<String> getConsumers() {
		return consumers;
	}
	public void setConsumers(List<String> consumers) {
		this.consumers = consumers;
	}
	
	
	
}
