package ar.com.bia.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class TrancriptionalRegulationData {

    private String tf_type ;
    private ArrayList<HashMap<String,Object>> regulates;
    private ArrayList<HashMap<String,Object>> regulated_by;


    public String getTf_type() {
        return tf_type;
    }

    public void setTf_type(String tf_type) {
        this.tf_type = tf_type;
    }

    public ArrayList<HashMap<String, Object>> getRegulates() {
        return regulates;
    }

    public void setRegulates(ArrayList<HashMap<String, Object>> regulates) {
        this.regulates = regulates;
    }

    public ArrayList<HashMap<String, Object>> getRegulated_by() {
        return regulated_by;
    }

    public void setRegulated_by(ArrayList<HashMap<String, Object>> regulated_by) {
        this.regulated_by = regulated_by;
    }
}
