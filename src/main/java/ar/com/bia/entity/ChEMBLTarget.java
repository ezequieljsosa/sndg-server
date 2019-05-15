package ar.com.bia.entity;


import java.util.ArrayList;

public class ChEMBLTarget {

    private String chemblid;
    private ArrayList<ChEMBLAssay> assays;

    public String getChemblid() {
        return chemblid;
    }

    public void setChemblid(String chemblid) {
        this.chemblid = chemblid;
    }

    public ArrayList<ChEMBLAssay> getAssays() {
        return assays;
    }

    public void setAssays(ArrayList<ChEMBLAssay> assays) {
        this.assays = assays;
    }
}
