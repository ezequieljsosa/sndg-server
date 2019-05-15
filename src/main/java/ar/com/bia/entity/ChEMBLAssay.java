package ar.com.bia.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class ChEMBLAssay {

    private String assay;
    private String type;
    private String description;
    private ArrayList<HashMap<String,Object>> activities;

    public String getAssay() {
        return assay;
    }

    public void setAssay(String assay) {
        this.assay = assay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<HashMap<String, Object>> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<HashMap<String, Object>> activities) {
        this.activities = activities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
