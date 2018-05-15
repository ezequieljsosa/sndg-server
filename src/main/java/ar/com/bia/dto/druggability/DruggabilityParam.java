package ar.com.bia.dto.druggability;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.data.mongodb.core.query.Criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import ar.com.bia.dto.PathwayDTO;
import ar.com.bia.entity.UserDoc;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DruggabilityParam {

	public static List<String> defaultSearchParams = Arrays.asList(new String[] { "keyword", "ontology", "pathway" });

	private double coefficient;
	private String category;
	private String type;
	private String name;
	private List<String> options;
	private String operation;
	private String value;
	private String description;
	private String score;
	private String target;
	private String groupoperation;
	private String uploader;

	private String defaultGroupOperation;
	private String defaultOperation;
	private String defaultValue;

	public DruggabilityParam() {
		super();
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getGroupoperation() {
		return groupoperation;
	}

	public void setGroupoperation(String groupoperation) {
		this.groupoperation = groupoperation;
	}

	public String getDefaultGroupOperation() {
		return defaultGroupOperation;
	}

	public void setDefaultGroupOperation(String defaultGroupOperation) {
		this.defaultGroupOperation = defaultGroupOperation;
	}

	public String getDefaultOperation() {
		return defaultOperation;
	}

	public void setDefaultOperation(String defaultOperation) {
		this.defaultOperation = defaultOperation;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	@Override
	public String toString() {
		return "DruggabilityParam [name=" + name + ", type=" + type + ", category=" + category + ", value=" + value
				+ "]";
	}

	public Criteria mongoFilter() {
		Criteria c = null;
		if (this.getUploader().equals("demo")) {
			c = Criteria.where("search." + this.getName());
		} else {
			c = Criteria.where("search." + this.getUploader() + "." + this.getName());
		}
		if (this.getType().equals("number")) {
			if (this.getOperation().equals(">")) {
				c.gte(Float.parseFloat(this.getValue()));
			} else {
				c.lte(Float.parseFloat(this.getValue()));
			}
		} else {
			Object value = this.getValue();
			if (this.getOptions().contains("true") && this.getOptions().contains("false")) {
				value = Boolean.parseBoolean(value.toString());
			}
			
			if (this.getOperation().equals("equal")) {
				c.is(value);
			} else {
				c.ne(value);
			}
		}
		return c;
	}
	
	

	public void mongoProject(DBObject projections) {
		String field = "search." + this.getName();
		if (!this.getUploader().equals("demo")) {
			field = "search." + this.getUploader() + "." + this.getName();
		}
		projections.put(field, true);
	}

	public Boolean matches(Map<String, Object> map) {
		// Map<String, Object> map = p.getSearch();

		if (map.containsKey(this.getName())) {

			Object rawValue = map.get(this.getName());
			return extractValue(rawValue);

		} else if (!this.uploader.equals(UserDoc.publicUserName) && map.containsKey(this.uploader)) {

			@SuppressWarnings("rawtypes")
			BasicDBObject userDict = new BasicDBObject((Map) map.get(this.uploader));
			String paramName = this.getName();
			// Rv2764c
			Optional<String> attrib = userDict.keySet().stream().filter(x -> x.toLowerCase().equals(paramName))
					.findFirst();
			if (attrib.isPresent()) {
				String rawValue = userDict.get(attrib.get()).toString().toLowerCase();
				return extractValue(rawValue);
			} else {
				return false;
			}

			// BasicDBObject userDict = (BasicDBObject) map.get(this.uploader);
			// if(userDict.containsField(this.getName())){
			// Object rawValue = userDict.get(this.getName());
			// return extractValue(rawValue);
			// } else {
			// return false;
			// }
		} else {
			return false;
		}

	}

	// FIXME deberia ser una herencia en lugar de preguntar de que tipo es y
	// hacer el metodo
	private Boolean extractValue(Object rawValue) {
		if (this.getType().equals("value")) {
			if (this.getValue() == null) {
				System.err.println("Esto nunca deberia pasar");
				return false;
			}
			boolean booleanValue = this.getValue().equals(rawValue.toString().toLowerCase());
			if (this.operation.equals("equal")) {
				return booleanValue;
			} else {
				return !booleanValue;
			}
		} else {
			double doubleValue = Double.parseDouble(rawValue.toString());
			if (this.operation.equals(">")) {
				return doubleValue() < doubleValue;
			} else {
				return doubleValue() > doubleValue;
			}
		}
	}

	private double doubleValue() {
		return Double.parseDouble(this.getValue());
	}

	public double score(Collection<String> keywords, Map<String, Object> map) {
		if (DruggabilityParam.defaultSearchParams.contains(this.getName())) {

			boolean hasKeyword = keywords.contains(this.getValue());

			// TODO el caso particular del EC no deberia manejarse aca
			if (this.getValue().startsWith("ec:")) {
				hasKeyword = keywords.stream().anyMatch(x -> x.startsWith(this.getValue()));
			}
			//

			if (this.operation.equals("equal")) {
				return hasKeyword ? this.getCoefficient() : 0;
			} else {
				return !hasKeyword ? this.getCoefficient() : 0;
			}
		}

		if (map.containsKey(this.getName())) {
			String searchValue = map.get(this.getName()).toString().toLowerCase();
			return extractCoeficient(searchValue);

		} else if (!this.uploader.equals(UserDoc.publicUserName) && map.containsKey(this.uploader)) {
			@SuppressWarnings("rawtypes")
			BasicDBObject userDict = new BasicDBObject((Map) map.get(this.uploader));
			String paramName = this.getName();

			Optional<String> attrib = userDict.keySet().stream().filter(x -> x.equals(paramName)).findFirst();
			if (attrib.isPresent()) {
				String rawValue = userDict.get(attrib.get()).toString().toLowerCase();
				return extractCoeficient(rawValue);
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	private double extractCoeficient(String searchValue) {

		if (this.getType().equals("value")) {
			Boolean isValue = this.getValue().toLowerCase().equals(searchValue);
			if (this.operation.equals("equal")) {
				return isValue ? this.getCoefficient() : 0;
			} else {
				return !isValue ? this.getCoefficient() : 0;
			}
		} else {
			if (!searchValue.toString().toLowerCase().equals("nan")) {
				try {
					return Double.parseDouble(searchValue) * this.getCoefficient();
				} catch (Exception ex){
					throw ex;
				}
			}
			return 0;

		}
	}

	public String getCompleteName() {

		return (this.name.equals("ontology")) ? this.getName() + "-" + this.getValue() : this.getName();
	}

	public Double applyGroupOperation(PathwayDTO pathway, List<Double> list) {
		Double val = 0.0;
		Supplier<Stream<Double>> filtered = () -> list.stream().filter(p -> p != null);
		switch (this.groupoperation) {
		case "avg":
			val = filtered.get().mapToDouble(x -> x).sum() / list.size();
			break;
		case "min":
			val = filtered.get().min(Double::compare).get();
			break;
		case "max":
			val = filtered.get().max(Double::compare).get();
			break;
		default:
			val = filtered.get().mapToDouble(x -> x).sum();

		}

		Map<String, Object> props = new HashMap<String, Object>();
		props.put("name", this.getCompleteName());
		props.put("total", list.size());
		props.put("filtered", filtered.get());
		props.put("operation", this.groupoperation);
		props.put("result", val);
		pathway.getProperties().add(props);
		return val;

	}

}
