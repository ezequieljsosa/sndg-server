package ar.com.bia.dto.krona;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;


@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"}) 
public class KronaColor  {
	
	
	private String value; 
	
	@XStreamAsAttribute
	private String attribute;
	@XStreamAsAttribute
	private int valueStart;
	@XStreamAsAttribute
	private int valueEnd;
	@XStreamAsAttribute
	private int hueStart;
	@XStreamAsAttribute
	private int hueEnd;

	public KronaColor() {
		super();
		this.valueStart = 0;
		this.valueEnd = 55;
		this.hueStart = 120;
		this.hueEnd = 240;
		this.value = "";

	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public int getValueStart() {
		return valueStart;
	}

	public void setValueStart(int valueStart) {
		this.valueStart = valueStart;
	}

	public int getValueEnd() {
		return valueEnd;
	}

	public void setValueEnd(int valueEnd) {
		this.valueEnd = valueEnd;
	}

	public int getHueStart() {
		return hueStart;
	}

	public void setHueStart(int hueStart) {
		this.hueStart = hueStart;
	}

	public int getHueEnd() {
		return hueEnd;
	}

	public void setHueEnd(int hueEnd) {
		this.hueEnd = hueEnd;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
