package ar.com.bia.dto.krona;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 
 * @author eze
 * @see http://sourceforge.net/p/krona/wiki/Krona%202.0%20XML%20Specification/
 * @see http://krona.sourceforge.net/examples/xml.krona.html
 * @see http://krona.sourceforge.net/examples/xml.txt
 */
@XStreamAlias("krona")
public class KronaDocument {
	private KronaAttributeCollection attributes;
	

	private KronaDatasetCollection datasets;
	
	private KronaColor color;
	
	
	private KronaNode node;
	
	
	
	public KronaDocument() {
		super();
		this.datasets = new KronaDatasetCollection();
		this.setAttributes(new KronaAttributeCollection());		
		this.setColor(new KronaColor());
		this.setNode(new KronaNode());
	}
	
	
	public KronaAttributeCollection getAttributes() {
		return attributes;
	}
	public void setAttributes(KronaAttributeCollection attributes) {
		this.attributes = attributes;
	}
	
	public KronaColor getColor() {
		return color;
	}
	public void setColor(KronaColor color) {
		this.color = color;
	}
	public KronaNode getNode() {
		return node;
	}
	public void setNode(KronaNode node) {
		this.node = node;
	}
	
	public void addDataset(String dataset){
		this.datasets.addDataset(dataset);
	}
	
	
	
}
