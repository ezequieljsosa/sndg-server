package ar.com.bia.pdb;

import java.util.List;

public class ResidueSetDoc {

	private String name;
	private String type;
	private List<String> residues;
	
	private float score;
	private float druggability_score;
	private float number_of_alpha_spheres;
	private float total_sasa;
	private float polar_sasa;
	private float apolar_sasa;
	private float volume;
	private float mean_local_hydrophobic_density;
	private float mean_alpha_sphere_radius;
	private float mean_alp_sph_solvent_access;
	private float apolar_alpha_sphere_proportion;
	private float hydrophobicity_score;
	private float volume_score;
	private float polarity_score;
	private float charge_score;
	private float proportion_of_polar_atoms;
	private float alpha_sphere_density;
	private float cent_of_mass___alpha_sphere_max_dist;
	private float flexibility;
	

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

	public List<String> getResidues() {
		return residues;
	}

	public void setResidues(List<String> residues) {
		this.residues = residues;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getDruggability_score() {
		return druggability_score;
	}

	public void setDruggability_score(float druggability_score) {
		this.druggability_score = druggability_score;
	}

	public float getNumber_of_alpha_spheres() {
		return number_of_alpha_spheres;
	}

	public void setNumber_of_alpha_spheres(float number_of_alpha_spheres) {
		this.number_of_alpha_spheres = number_of_alpha_spheres;
	}

	public float getTotal_sasa() {
		return total_sasa;
	}

	public void setTotal_sasa(float total_sasa) {
		this.total_sasa = total_sasa;
	}

	public float getPolar_sasa() {
		return polar_sasa;
	}

	public void setPolar_sasa(float polar_sasa) {
		this.polar_sasa = polar_sasa;
	}

	public float getApolar_sasa() {
		return apolar_sasa;
	}

	public void setApolar_sasa(float apolar_sasa) {
		this.apolar_sasa = apolar_sasa;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getMean_local_hydrophobic_density() {
		return mean_local_hydrophobic_density;
	}

	public void setMean_local_hydrophobic_density(float mean_local_hydrophobic_density) {
		this.mean_local_hydrophobic_density = mean_local_hydrophobic_density;
	}

	public float getMean_alpha_sphere_radius() {
		return mean_alpha_sphere_radius;
	}

	public void setMean_alpha_sphere_radius(float mean_alpha_sphere_radius) {
		this.mean_alpha_sphere_radius = mean_alpha_sphere_radius;
	}

	public float getMean_alp_sph_solvent_access() {
		return mean_alp_sph_solvent_access;
	}

	public void setMean_alp_sph_solvent_access(float mean_alp_sph_solvent_access) {
		this.mean_alp_sph_solvent_access = mean_alp_sph_solvent_access;
	}

	public float getApolar_alpha_sphere_proportion() {
		return apolar_alpha_sphere_proportion;
	}

	public void setApolar_alpha_sphere_proportion(float apolar_alpha_sphere_proportion) {
		this.apolar_alpha_sphere_proportion = apolar_alpha_sphere_proportion;
	}

	public float getHydrophobicity_score() {
		return hydrophobicity_score;
	}

	public void setHydrophobicity_score(float hydrophobicity_score) {
		this.hydrophobicity_score = hydrophobicity_score;
	}

	public float getVolume_score() {
		return volume_score;
	}

	public void setVolume_score(float volume_score) {
		this.volume_score = volume_score;
	}

	public float getPolarity_score() {
		return polarity_score;
	}

	public void setPolarity_score(float polarity_score) {
		this.polarity_score = polarity_score;
	}

	public float getCharge_score() {
		return charge_score;
	}

	public void setCharge_score(float charge_score) {
		this.charge_score = charge_score;
	}

	public float getProportion_of_polar_atoms() {
		return proportion_of_polar_atoms;
	}

	public void setProportion_of_polar_atoms(float proportion_of_polar_atoms) {
		this.proportion_of_polar_atoms = proportion_of_polar_atoms;
	}

	public float getAlpha_sphere_density() {
		return alpha_sphere_density;
	}

	public void setAlpha_sphere_density(float alpha_sphere_density) {
		this.alpha_sphere_density = alpha_sphere_density;
	}

	

	public float getCent_of_mass___alpha_sphere_max_dist() {
		return cent_of_mass___alpha_sphere_max_dist;
	}

	public void setCent_of_mass___alpha_sphere_max_dist(float cent_of_mass___alpha_sphere_max_dist) {
		this.cent_of_mass___alpha_sphere_max_dist = cent_of_mass___alpha_sphere_max_dist;
	}

	public float getFlexibility() {
		return flexibility;
	}

	public void setFlexibility(float flexibility) {
		this.flexibility = flexibility;
	}
	
	public boolean inChain(String chain){
		for (String residue : residues) {
			if (residue.startsWith(chain)){
				return true;
			}
		}
		return false;
	}
	
	

}
