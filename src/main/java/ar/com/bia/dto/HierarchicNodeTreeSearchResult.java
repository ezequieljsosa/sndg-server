package ar.com.bia.dto;

import java.util.ArrayList;
import java.util.List;

import ar.com.bia.entity.GeneProductDoc;


public class HierarchicNodeTreeSearchResult {

	/**
	 * Query term
	 */
	private String query;
	/**
	 * Map
	 * 	Sets: 	direct Successors
	 *  Values:	Child Annotations 
	 */
	private List<GOTermTuple> dataTuples;
	/**
	 * Genes annotated with the query term
	 */
	private List<GeneProductDoc> annotatedGenes;

	public HierarchicNodeTreeSearchResult() {
		super();
		this.dataTuples = new ArrayList<GOTermTuple>();
		this.annotatedGenes = new ArrayList<GeneProductDoc>();
	}

	

	



	public List<GOTermTuple> getDataTuples() {
		return dataTuples;
	}







	public void setDataTuples(List<GOTermTuple> dataTuples) {
		this.dataTuples = dataTuples;
	}







	

	public List<GeneProductDoc> getAnnotatedGenes() {
		return annotatedGenes;
	}







	public void setAnnotatedGenes(List<GeneProductDoc> annotatedGenes) {
		this.annotatedGenes = annotatedGenes;
	}







	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	

}
