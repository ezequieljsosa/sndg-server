package ar.com.bia.dto.druggability;

import java.util.List;

public interface Scoreable {

	public String gene();
	public Double getScore() ;
	public void setScore(Double score) ;
	public void updateScore(List<DruggabilityParam> scores) ;
	
}
