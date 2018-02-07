package ar.com.bia.pdb;

import java.util.HashMap;
import java.util.Map;

public class PocketEmbedDoc {

	/*
	 "descriptors" : [
				{
					"hetatm" : " ",
					"code" : "STP",
					"pos" : 1,
					"chain" : " ",
					"atoms" : [
						{
							"code" : " POL",
							"bfactor" : 0,
							"pos" : 1662,
							"element" : "P",
							"y" : 45.266998291015625,
							"x" : -78.6240005493164,
							"z" : 64.1989974975586
						},

	"properties" : {
				"Pocket Score" : "51.1070",
				"Polarity Score" : "29",
				"Real volume (approximation)" : "2085.9546",
				"Volume Score" : "4.2609",
				"Local hydrophobic density Score" : "32.3360",
				"Mean alpha-sphere SA" : "0.4794",
				"Proportion of apolar alpha sphere" : "0.3968",
				"Number of apolar alpha sphere" : "125",
				"Mean alpha-sphere radius" : "3.7485",
				"Hydrophobicity Score" : "7.8913",
				"Mean B-factor" : "0.4803",
				"Number of V Vertices" : "315",
				"Charge Score" : "10",
				"Drug Score" : "0.5832"
			},


	"residues" : [
				{
					"hetatm" : " ",
					"code" : "SER",
					"pos" : 76,
					"chain" : " ",
					"atoms" : [
						{
							"code" : "  OG",
							"bfactor" : 133.05,
							"pos" : 607,
							"element" : "O",
							"y" : 43.529998779296875,
							"x" : -78.49600219726562,
							"z" : 66.91000366210938
						},
						*/
	
	private Map<String,String> properties;
	
	public PocketEmbedDoc() {
		super();
		this.properties = new HashMap<String,String>();
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	
	
	
	
}
