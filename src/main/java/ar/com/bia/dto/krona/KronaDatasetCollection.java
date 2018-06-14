package ar.com.bia.dto.krona;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

public class KronaDatasetCollection {

	@XStreamImplicit(itemFieldName="dataset")
	private List<String> datasets;

	public KronaDatasetCollection() {
		super();
		this.setDatasets(new ArrayList<String>());
	}

	public List<String> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<String> datasets) {
		this.datasets = datasets;
	}

	public void addDataset(String dataset) {
		this.datasets.add(dataset);		
	}	
}