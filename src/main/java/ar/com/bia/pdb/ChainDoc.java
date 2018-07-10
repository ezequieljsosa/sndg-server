package ar.com.bia.pdb;

import ar.com.bia.entity.aln.SimpleAligment;

import java.util.List;
import java.util.Map;

public class ChainDoc {
	
	private String name;
	private List<List<Integer>> segments;
    private SimpleAligment aln;
	private List<Map<String,Object>> residues;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<List<Integer>> getSegments() {
		return segments;
	}
	public void setSegments(List<List<Integer>> segments) {
		this.segments = segments;
	}
	public SimpleAligment getAln() {
		return aln;
	}
	public void setAln(SimpleAligment aln) {
		this.aln = aln;
	}
	public List<Map<String, Object>> getResidues() {
		return residues;
	}
	public void setResidues(List<Map<String, Object>> residues) {
		this.residues = residues;
	}
	
	
	
	
}
