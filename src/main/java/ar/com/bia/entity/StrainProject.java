package ar.com.bia.entity;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public class StrainProject {


    private String id;
    private String name;
    private String description;
    private String date;
    private List<String> strains;
    private List<Map<String, Object>> trees;
    private String user;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public List<Map<String, Object>> getTrees() {
        return trees;
    }

    public void setTrees(List<Map<String, Object>> trees) {
        this.trees = trees;
    }

    public List<String> getStrains() {
        return strains;
    }

    public void setStrains(List<String> strains) {
        this.strains = strains;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
