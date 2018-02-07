package ar.com.bia.pdb;

public class MoleculeDoc {

	public String chain;
	public String compound_type;
	public String compound;
	public Integer resid;
	
	
	public String getCompound_type() {
		return compound_type;
	}
	public void setCompound_type(String compound_type) {
		this.compound_type = compound_type;
	}
	public String getCompound() {
		return compound;
	}
	public void setCompound(String compound) {
		this.compound = compound;
	}
	public Integer getResid() {
		return resid;
	}
	public void setResid(Integer resid) {
		this.resid = resid;
	}
	public String getChain() {
		return chain;
	}
	public void setChain(String chain) {
		this.chain = chain;
	}
	
	
}
