package ar.com.bia.pdb;

import java.util.List;
import java.util.Map;

public class PDBSeqCluster {

	private String name;
	private String type;
	private List<Map<String,Object>> parts;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Map<String, Object>> getParts() {
		return parts;
	}
	public void setParts(List<Map<String, Object>> parts) {
		this.parts = parts;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
