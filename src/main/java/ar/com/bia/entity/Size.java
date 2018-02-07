package ar.com.bia.entity;

public class Size {

	private int len;
	private String unit;

	public Size(Integer len, String unit) {
		super();
		this.len = len;
		this.unit = unit;
	}

	public Integer getLen() {
		return len;
	}

	public void setLen(Integer len) {
		this.len = len;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	

	

	@Override
	public String toString() {
		return "" + len + " " + unit;
	}

}
