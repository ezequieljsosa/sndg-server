package ar.com.bia.dto.krona;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("attribute")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class KronaAttribute {
	
	private String display;
	
	
	private String value;
	
	
	
	public KronaAttribute(String display, String value) {
		super();
		this.display = display;
		this.value = value;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
