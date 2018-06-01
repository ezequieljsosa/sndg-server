package ar.com.bia.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import ar.com.bia.dto.PathwayDTO;
import ar.com.bia.entity.druggability.SeqColDruggabilityParam;


/**
 * @author eze
 */
@Document(collection = "sequence_collection")
public class SeqCollectionDoc {

    public static final String STRAIN = "strain";

    @Id
    private String id;
    private String organism;
    private String name;
    private String description;
    private String status;
    private String ncbi_assembly;


    private ObjectId auth;

    private String _cls = "SeqCollection.Genome";

    private String size;
    private String type;

    private Boolean has_expression;
    @Field("go_index")
    private Boolean goIndex;
    @Field("ec_index")
    private Boolean ecIndex;
    private Map<String, List<String>> expression_samples;

    private List<Metric> statistics;
    private Map<String, Object> assembly;
    private Map<String, Object> properties;

    private List<String> publications;

    private List<Strain> strains;
    private List<Map<String, String>> strainsProps;
    private List<StrainProject> strainProjects;

    private List<PathwayDTO> pathways;
    private List<PathwayDTO> kegg;

    private List<SeqColDruggabilityParam> druggabilityParams;
    private List<PropertyUpload> uploads;


    @Field("ref_genome_id")
    private String refGenomeId;

    @Field("proteome_id")
    private String proteomeId;

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Map<String, Object> getAssembly() {
        return assembly;
    }

    public void setAssembly(Map<String, Object> assembly) {
        this.assembly = assembly;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPublications() {
        return publications;
    }

    public void setPublications(List<String> publications) {
        this.publications = publications;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public List<Metric> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Metric> statistics) {
        this.statistics = statistics;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getProteomeId() {
        return proteomeId;
    }

    public void setProteomeId(String proteomeId) {
        this.proteomeId = proteomeId;
    }

    public Boolean getHas_expression() {
        return has_expression;
    }

    public void setHas_expression(Boolean has_expression) {
        this.has_expression = has_expression;
    }

    public Map<String, List<String>> getExpression_samples() {
        return expression_samples;
    }

    public void setExpression_samples(
            Map<String, List<String>> expression_samples) {
        this.expression_samples = expression_samples;
    }


    public String getRefGenomeId() {
        return refGenomeId;
    }

    public void setRefGenomeId(String refGenomeId) {
        this.refGenomeId = refGenomeId;
    }

    public Boolean getGoIndex() {
        return goIndex;
    }

    public void setGoIndex(Boolean goIndex) {
        this.goIndex = goIndex;
    }

    public Boolean getEcIndex() {
        return ecIndex;
    }

    public void setEcIndex(Boolean ecIndex) {
        this.ecIndex = ecIndex;
    }

    public List<PathwayDTO> getPathways() {
        return pathways;
    }

    public void setPathways(List<PathwayDTO> pathways) {
        this.pathways = pathways;
    }

    public List<SeqColDruggabilityParam> getDruggabilityParams() {
        return this.druggabilityParams;
    }

    public ObjectId getAuth() {
        return auth;
    }

    public void setAuth(ObjectId auth) {
        this.auth = auth;
    }

    public List<Float> getMetricValues(String metricName) {
        for (Metric metric : this.getStatistics()) {
            if (metric.getName().equals(metricName)) {
                return metric.getValues();
            }
        }
        return new ArrayList<Float>();
    }

    public List<PropertyUpload> getUploads() {
        return uploads;
    }

    public void setUploads(List<PropertyUpload> uploads) {
        this.uploads = uploads;
    }

    public void setDruggabilityParams(List<SeqColDruggabilityParam> druggabilityParams) {
        this.druggabilityParams = druggabilityParams;
    }

    public String get_cls() {
        return _cls;
    }

    public void set_cls(String _cls) {
        this._cls = _cls;
    }


    public List<StrainProject> getStrainProjects() {
        return strainProjects;
    }

    public void setStrainProjects(List<StrainProject> strainProjects) {
        this.strainProjects = strainProjects;
    }

    public List<Strain> projectStrains(StrainProject project) {
        return this.getStrains().stream().filter(x -> project.getStrains().contains(x.getName())).collect(Collectors.toList());
    }

    public String getNcbi_assembly() {
        return ncbi_assembly;
    }

    public void setNcbi_assembly(String ncbi_assembly) {
        this.ncbi_assembly = ncbi_assembly;
    }


    public List<Strain> getStrains() {
        return strains;
    }

    public void setStrains(List<Strain> strains) {
        this.strains = strains;
    }

    public List<Map<String, String>> getStrainsProps() {
        return strainsProps;
    }

    public void setStrainsProps(List<Map<String, String>> strainsProps) {
        this.strainsProps = strainsProps;
    }

    public List<PathwayDTO> getKegg() {
        return kegg;
    }

    public void setKegg(List<PathwayDTO> kegg) {
        this.kegg = kegg;
    }
}
