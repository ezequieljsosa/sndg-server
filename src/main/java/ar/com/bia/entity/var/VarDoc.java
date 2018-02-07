package ar.com.bia.entity.var;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "variant")
public class VarDoc {

	private String id;
	private String organism;
	private String contig;
	private long pos;
	private String gene;

	private String ref;
	private String prot_ref;

	private List<Allele> sample_alleles;

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getContig() {
		return contig;
	}

	public void setContig(String contig) {
		this.contig = contig;
	}

	public long getPos() {
		return pos;
	}

	public void setPos(long pos) {
		this.pos = pos;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	

	public String getProt_ref() {
		return prot_ref;
	}

	public void setProt_ref(String prot_ref) {
		this.prot_ref = prot_ref;
	}

	public List<Allele> getSample_alleles() {
		return sample_alleles;
	}

	public void setSample_alleles(List<Allele> sample_alleles) {
		this.sample_alleles = sample_alleles;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String nuclFromStrain(String strain) {

		Optional<Allele> allele = this.sample_alleles.stream().filter(allele2 -> allele2.getSamples().stream()
				.anyMatch(sampleAllele -> sampleAllele.getSample().equals(strain))).findFirst();
		;
		if (allele.isPresent()) {
			return allele.get().getAlt();
		}
		return this.getRef();
	}

	public boolean strainVarType(List<String> strains, List<String> list) {

		Optional<Allele> allele = this.sample_alleles.stream().filter(allele2 -> {
			HashSet<String> hashSet = new HashSet<String>(allele2.getVariant_type());
			hashSet.retainAll(list);
			return !hashSet.isEmpty() && allele2.getSamples().stream()
					.anyMatch(sampleAllele -> strains.contains(sampleAllele.getSample()));
		}).findFirst();

		return allele.isPresent();
	}

}
