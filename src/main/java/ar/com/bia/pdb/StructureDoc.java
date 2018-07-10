package ar.com.bia.pdb;

import ar.com.bia.entity.aln.SimpleAligment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

/**
 * @author eze
 * 
 */
@Document(collection = "structures")
public class StructureDoc {

	@Id
	private String id;
	
	
	private List<PDBSeqCluster> clusters;
	private List<Map<String, String>> bindingdb;
	private List<Map<String, String>> pka;
	private Map<String, List<Map<String, String>>> csa;

	// PDB
	private String resolution;
	private String quaternary;

	private String name;
	private String description;
	private String organism;

	private ObjectId seq_collection_id;
	private List<ChainDoc> chains;

	// private String residue_sets =
	// ListField(EmbeddedDocumentField(ResidueSet));
	private List<ResidueSetDoc> pockets;
	private List<MoleculeDoc> ligands;

	private List<StructureQualityDoc> qualities;

	private String pipeline;
	private List<SimpleAligment> templates;

	@Field("residue_sets")
	private List<ResidueSetDoc> residueSets;
	
	public StructureDoc() {
		super();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Map<String, String>> getBindingdb() {
		return bindingdb;
	}

	public void setBindingdb(List<Map<String, String>> bindingdb) {
		this.bindingdb = bindingdb;
	}

	public List<Map<String, String>> getPka() {
		return pka;
	}

	public void setPka(List<Map<String, String>> pka) {
		this.pka = pka;
	}

	public Map<String, List<Map<String, String>>> getCsa() {
		return csa;
	}

	public void setCsa(Map<String, List<Map<String, String>>> csa) {
		this.csa = csa;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getQuaternary() {
		return quaternary;
	}

	public void setQuaternary(String quaternary) {
		this.quaternary = quaternary;
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

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public ObjectId getSeq_collection_id() {
		return seq_collection_id;
	}

	public void setSeq_collection_id(ObjectId seq_collection_id) {
		this.seq_collection_id = seq_collection_id;
	}

	public List<ChainDoc> getChains() {
		return chains;
	}

	public void setChains(List<ChainDoc> chains) {
		this.chains = chains;
	}

	public List<ResidueSetDoc> getPockets() {
		return pockets;
	}

	public void setPockets(List<ResidueSetDoc> pockets) {
		this.pockets = pockets;
	}

	public List<MoleculeDoc> getLigands() {
		return ligands;
	}

	public void setLigands(List<MoleculeDoc> ligands) {
		this.ligands = ligands;
	}

	public List<StructureQualityDoc> getQualities() {
		return qualities;
	}

	public void setQualities(List<StructureQualityDoc> qualities) {
		this.qualities = qualities;
	}

	public String getPipeline() {
		return pipeline;
	}

	public void setPipeline(String pipeline) {
		this.pipeline = pipeline;
	}

	public List<SimpleAligment> getTemplates() {
		return templates;
	}

	public void setTemplates(List<SimpleAligment> templates) {
		this.templates = templates;
	}

	public String getFilePath() {
		if(this.name.length() == 4){
			return "/data/databases/pdb/divided/" +this.getName().substring(1, 3) + "/pdb" + this.getName() + ".ent";
		} 
		return "/data/organismos/" + this.getOrganism() + "/estructura/" + this.getPipeline() + "/modelos/"
				+ this.getName() + ".pdb";
	}

	public Object getSeqId() {
		if ((this.getTemplates() == null) || (this.getTemplates().isEmpty())) {
			return this.getName();
		} else {
			return this.getTemplates().get(0).getAln_query().getName();
		}
	}
	
	

	public String getFilePocketPath() {
		if(this.name.length() == 4){
			return "/data/databases/pdb/pockets/" +this.getName().substring(1, 3) + "/" + this.getName() + ".json";
		} 
		return "/data/organismos/" + this.getOrganism() + "/estructura/" + this.getPipeline() + "/pockets/"
				+ this.getName() + ".json";
	}
	
	public boolean isModel(){
		return (this.getTemplates() != null) && this.getTemplates().size() > 0;
	}

	public List<ResidueSetDoc> getResidueSets() {
		return residueSets;
	}

	public void setResidueSets(List<ResidueSetDoc> residueSets) {
		this.residueSets = residueSets;
	}

	public List<PDBSeqCluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<PDBSeqCluster> clusters) {
		this.clusters = clusters;
	}

	
	

}
