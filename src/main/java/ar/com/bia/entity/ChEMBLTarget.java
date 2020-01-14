package ar.com.bia.entity;


import java.util.ArrayList;

public class ChEMBLTarget {

    private String target;
    private ArrayList<ChEMBLAssay> assays;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ArrayList<ChEMBLAssay> getAssays() {
        return assays;
    }

    public void setAssays(ArrayList<ChEMBLAssay> assays) {
        this.assays = assays;
    }
}
