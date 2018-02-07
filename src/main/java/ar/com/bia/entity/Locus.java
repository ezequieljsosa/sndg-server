package ar.com.bia.entity;

public class Locus {

	private String reference;
	private Integer start;
	private Integer end;
	private String strand;

	public Locus(String reference, int start, int end, String strand) {
		this.setReference(reference);
		this.setStart(start);
		this.setEnd(end);
		this.setStrand(strand);
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getStrand() {
		return strand;
	}

	public void setStrand(String strand) {
		assert (strand.length() == 1);
		this.strand = strand;
	}

	@Override
	public String toString() {
		if (this.reference == null) {
			if (strand != null) {
				return start + ":" + end + "(" + strand + ")";	
			} else {
				return start + ":" + end ;
			}
			
		}
		if (strand != null) {
			return this.getReference() + ":" + start + ":" + end + "(" + strand
					+ ")";
		} else {
			return this.getReference() + ":" + start + ":" + end;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((strand == null) ? 0 : strand.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Locus other = (Locus) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (strand == null) {
			if (other.strand != null)
				return false;
		} else if (!strand.equals(other.strand))
			return false;
		return true;
	}

	public boolean in(SeqFeatureEmbedDoc structFeature) {
		if (this.getReference() != null
				&& this.getReference().equals(structFeature.getId())) {
			return true;
		}
		if (structFeature.getLocation().getReference() == this.getReference()) {
			if (structFeature.getLocation().getStart() <= this.getStart()) {
				if (structFeature.getLocation().getEnd() >= this.getEnd())
					return true;
			}
		}
		return false;
	}

	public Locus relativeTo(Locus referenceLocus) {
		if(this.getReference() == null){
			//Relative to the begining of the sequence
			return new Locus(referenceLocus.getReference(),
					   this.getStart() - referenceLocus.getStart(),
					 this.getEnd() - referenceLocus.getStart(), this.getStrand());
		} else {
			if(this.getReference().equals(referenceLocus.getReference())){
				//Relative to the begining of the locus
				return new Locus(referenceLocus.getReference(),
						referenceLocus.getStart() +  this.getStart(),
						referenceLocus.getEnd() + this.getStart(), this.getStrand());
			} else {
				return this;
			}
			
		}
		
	}

}
