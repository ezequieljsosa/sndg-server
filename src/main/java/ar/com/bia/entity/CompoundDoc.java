package ar.com.bia.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "compounds")
public class CompoundDoc {

    @Id
    private String id;

    private String name;

    private String smiles;



    private String source;

    private List<String> zincs ;
    private List<String> ligand_list ;


    private String pfam;

    private String gene;
    private String organism;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }



    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }



    public List<String> getZincs() {
        return zincs;
    }

    public void setZincs(List<String> zincs) {
        this.zincs = zincs;
    }

    public String getPfam() {
        return pfam;
    }

    public void setPfam(String pfam) {
        this.pfam = pfam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLigand_list() {
        return ligand_list;
    }

    public void setLigand_list(List<String> ligand_list) {
        this.ligand_list = ligand_list;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }
}
