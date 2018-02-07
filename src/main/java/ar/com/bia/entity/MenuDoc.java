package ar.com.bia.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class MenuDoc {

	@Field("id")
	private String selector;
	private String parent;
	private String name;
	private String link;
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSelector() {
		return selector;
	}
	public void setSelector(String selector) {
		this.selector = selector;
	}
	
	
	
	
	
}
