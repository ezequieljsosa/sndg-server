package ar.com.bia.entity;

import java.util.List;

import org.bson.types.ObjectId;


public class GeneOrthologDoc {

	private ObjectId feature_id;
	private List<Float> identities;
	
	public ObjectId getFeatureId() {
		return this.feature_id;
	}
	
	public void setFeatureId(ObjectId feature_id) {
		this.feature_id = feature_id;
	}
	
	public List<Float> getIdentities() {
		return this.identities;
	}

	public void setIdentities(List<Float> identities) {
		this.identities = identities;
	}
	
}
