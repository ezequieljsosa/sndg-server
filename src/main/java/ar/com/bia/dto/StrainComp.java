package ar.com.bia.dto;

import ar.com.bia.entity.var.VarDoc;

public class StrainComp {
	private String strain1;
	private String strain2;
	private String operation;
	
	public String getStrain1() {
		return strain1;
	}
	public void setStrain1(String strain1) {
		this.strain1 = strain1;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getStrain2() {
		return strain2;
	}
	public void setStrain2(String strain2) {
		this.strain2 = strain2;
	}
	public boolean compare(VarDoc var) {
		String nuclStrain1 = var.nuclFromStrain(this.strain1);
		String nuclStrain2 = var.nuclFromStrain(this.strain2);
		if(this.operation.equals("equal")){
			return nuclStrain1.equals(nuclStrain2);	
		} else {
			return !nuclStrain1.equals(nuclStrain2);
		}
		
	}
	
}