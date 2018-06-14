package ar.com.bia.dto.krona;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.*;

/**
 * @author eze
 *
 */

/**
 * @author eze
 *
 */
@XStreamAlias("node")
public class KronaNode {

	//@XStreamAsAttribute
	private String name;
	//@XStreamImplicit
	private List<KronaNode> nodes;
	private String href = null;
	
	//@XStreamConverter(value = AttributesConverter.class)
	private Map<String, List<Long>> attributes;


	public KronaNode() {
		super();
		this.attributes = new HashMap<String, List<Long>>();
		
	}
	

	public KronaNode(String name) {
		super();
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<KronaNode> getNodes() {
		return nodes;
	}	

	
	
	public void addNode(KronaNode node){
		if(this.nodes == null){
			this.nodes = new ArrayList<KronaNode>();
		}
		this.nodes.add(node);
	}
	public void addAttribute(String attribute,List<Long> values){
		
		if(this.attributes == null){
			this.attributes = new HashMap<String, List<Long>>();
		}
		this.attributes.put(attribute,values);
	}


	public void addAttribute(String attribute, Long[] longs) {
		this.addAttribute(attribute, Arrays.asList(longs));		
	}


	public Map<String, List<Long>> getAttributes() {
		return attributes;
	}


	public String getHref() {
		return href;
	}


	public void setHref(String href) {
		this.href = href;
	}

	
	
	
}
