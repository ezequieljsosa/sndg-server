package ar.com.bia.dto.krona;

import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class KronaNodeConverter implements Converter {

	
	private String searchUrl;

	public KronaNodeConverter() {
		this.searchUrl = "";
	}

	
	public KronaNodeConverter(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {

		return type.equals(KronaNode.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		KronaNode node = (KronaNode) source;
		writer.addAttribute("name", node.getName());
		if (node.getHref() != null){
			writer.addAttribute("href", this.searchUrl + node.getHref().replace(".-", ""));	
		}
		
		if (node.getNodes() != null) {
			for (KronaNode child : node.getNodes()) {
				writer.startNode("node");
				context.convertAnother(child);
				writer.endNode();
			}
		}
		if (node.getAttributes() != null) {
			Map<String, List<Long>> map = node.getAttributes();
			for (String attribute : map.keySet()) {
				List<Long> values = map.get(attribute);
				writer.startNode(attribute);
				for (Long value : values) {
					writer.startNode("val");
					writer.setValue(value.toString());
					writer.endNode();
				}
				writer.endNode();
			}
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
