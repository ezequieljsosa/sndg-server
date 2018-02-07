package ar.com.bia.pdb;


public class HmmScanResultFeature {
	private String idFeature;
	private String name;
	private int start;
	private int end;
	private String description;
	private String note;
	private String status;
	private String chain;
	
	public HmmScanResultFeature(String name, int start, int end, String idFeature, String note, String status){
		this.name = name;
		this.start = start;
		this.end = end;
		this.idFeature = idFeature;
		this.note = note;
		this.status = status;
		this.chain = "";
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIdFeature() {
		return idFeature;
	}

	public void setIdFeature(String idFeature) {
		this.idFeature = idFeature;
	}

	public String getChain() {
		return chain;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}
	
	
}
