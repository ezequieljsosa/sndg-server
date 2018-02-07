package ar.com.bia.dto.druggability;

import java.util.ArrayList;
import java.util.List;

public class ScoreDTO {

	public List<DruggabilityParam> filters;
	public List<DruggabilityParam> scores;
	
	
	
	
	public ScoreDTO() {
		super();
		this.filters = new ArrayList<>();
		this.scores = new ArrayList<>();
	}
	public List<DruggabilityParam> getFilters() {
		return filters;
	}
	public void setFilters(List<DruggabilityParam> filters) {
		this.filters = filters;
	}
	public List<DruggabilityParam> getScores() {
		return scores;
	}
	public void setScores(List<DruggabilityParam> scores) {
		this.scores = scores;
	}
	@Override
	public String toString() {
		return "ScoreDTO [filters=" + filters + ", scores=" + scores + "]";
	}
	
	
	
}
