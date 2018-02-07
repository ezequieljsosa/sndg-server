package ar.com.bia.entity;

import java.util.List;

/**
 * @author gburguener
 *
 */
/**
 * @author gburguener
 *
 */
public class VariantEmbedDoc {

	private String protein_id;
	private String strain_id;
	private List<ChangeEmbedDoc> changes;
	
	
	public String getProtein_id() {
		return protein_id;
	}
	public void setProtein_id(String protein_id) {
		this.protein_id = protein_id;
	}
	public String getStrain_id() {
		return strain_id;
	}
	public void setStrain_id(String strain_id) {
		this.strain_id = strain_id;
	}
	public List<ChangeEmbedDoc> getChanges() {
		return changes;
	}
	public void setChanges(List<ChangeEmbedDoc> changes) {
		this.changes = changes;
	}
	
	
	
}
