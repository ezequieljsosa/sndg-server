package ar.com.bia.config;

import org.springframework.data.mongodb.core.MongoOperations;

public class CollectionConfig {
	private String name;
	private String collection;
	private Class<? extends Object> clazz;
	private MongoOperations mongoTemplate;

	public CollectionConfig(String name, String collection, Class<? extends Object> clazz,
			MongoOperations mongoTemplate) {
		super();
		this.name = name;
		this.collection = collection;
		this.clazz = clazz;
		this.mongoTemplate = mongoTemplate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public Class<? extends Object> getClazz() {
		return clazz;
	}

	public void setClazz(Class<? extends Object> clazz) {
		this.clazz = clazz;
	}

	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}