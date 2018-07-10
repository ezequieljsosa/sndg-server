package ar.com.bia.dto.krona;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

public class KronaAttributeCollection {

	@XStreamImplicit
	private List<KronaAttribute> attributes;

	@XStreamAsAttribute
	private String magnitude;

	public String getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(String magnitude) {
		this.magnitude = magnitude;
	}

	public KronaAttributeCollection() {
		super();
		this.attributes = new ArrayList<KronaAttribute>();
	}

	public void addAttribute(KronaAttribute kronaAttribute) {
		this.attributes.add(kronaAttribute);

	}

}
