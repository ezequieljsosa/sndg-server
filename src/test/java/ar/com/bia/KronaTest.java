package ar.com.bia;

import ar.com.bia.dto.krona.KronaAttribute;
import ar.com.bia.dto.krona.KronaDocument;
import ar.com.bia.dto.krona.KronaNode;
import ar.com.bia.dto.krona.KronaNodeConverter;
import com.thoughtworks.xstream.XStream;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.XMLTestCase;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;

public class KronaTest extends XMLTestCase {

	public void testSerialization() throws SAXException, IOException {
		XStream xstream = new XStream();
		xstream.registerConverter(new KronaNodeConverter());
		xstream.processAnnotations(KronaDocument.class);
		KronaDocument krona = new KronaDocument();
		KronaAttribute grams = new KronaAttribute("Grams", "grams");
		KronaAttribute dailyValue = new KronaAttribute("% Daily Value", "dva");
		krona.getAttributes().addAttribute(grams);
		krona.getAttributes().addAttribute(dailyValue);
		krona.getAttributes().setMagnitude("grams");
		krona.getColor().setAttribute("grams");
		krona.addDataset("Brand X");
		krona.addDataset("Brand Y");

		krona.getNode().setName("Drugs serving");
		krona.getNode().addAttribute("grams",
				Arrays.asList(new Long[] { 55L, 55L }));

		KronaNode protein = new KronaNode("Protein");
		protein.addAttribute("grams", new Long[] { 5L, 6L });
		KronaNode cocain = new KronaNode("Cocaina");
		cocain.addAttribute("grams", new Long[] { 39L, 41L });
		cocain.addAttribute("dva", new Long[] { 13L, 14L });

		 KronaNode sugar = new KronaNode("Sugars");
		 sugar.addAttribute("grams", new Long[]{3L,5L});
		
		 KronaNode fiver=new KronaNode("Dietary fiber");
		 fiver.addAttribute("grams", new Long[]{4L,8L});
		 fiver.addAttribute("dva", new Long[]{16L,32L});
		 cocain.addNode(sugar);
		 cocain.addNode(fiver);

		KronaNode fat = new KronaNode("Fats");
		fat.addAttribute("grams", new Long[] { 8L, 7L });
		fat.addAttribute("dva", new Long[] { 12L, 9L });

		krona.getNode().addNode(protein);
		krona.getNode().addNode(cocain);
		krona.getNode().addNode(fat);

		String str = " <krona>"
				+ "	<attributes magnitude=\"grams\">"
				+ "		<attribute display=\"Grams\">grams</attribute>"
				+ "		<attribute display=\"% Daily Value\">dva</attribute>"
				+ "	</attributes>"
				+ "	<color attribute=\"grams\" valueStart=\"0\" valueEnd=\"55\" hueStart=\"120\" hueEnd=\"240\"> </color>"
				+ "	<datasets>"
				+ "		<dataset>Brand X</dataset>"
				+ "		<dataset>Brand Y</dataset>"
				+ "	</datasets>"
				+ "	<node name=\"Drugs serving\">"
				+ "		<grams><val>55</val><val>55</val></grams>"
				+ "		<node name=\"Protein\">"
				+ "			<grams><val>5</val><val>6</val></grams>"
				+ "		</node>"
				+ "		<node name=\"Cocaina\">"
				+ "			<grams><val>39</val><val>41</val></grams>"
				+ "			<dva><val>13</val><val>14</val></dva>"
				+ "			<node name=\"Sugars\">"
				+ "				<grams><val>3</val><val>5</val></grams>" 
				+ "			</node>"
				+ "			<node name=\"Dietary fiber\">"
				+ "				<grams><val>4</val><val>8</val></grams>"
				+ "				<dva><val>16</val><val>32</val></dva>" 
				+ "			</node>"
				+ "		</node>" 
				+ "		<node name=\"Fats\">"
				+ "			<grams><val>8</val><val>7</val></grams>"
				+ "			<dva><val>12</val><val>9</val></dva>" 
				+ "		</node>"
				+ "	</node>" 
				+ "  </krona>";

		String test = xstream.toXML(krona).replaceAll("\\s+", " ")
				.replaceAll("><", "> <").trim();

		String control = str.replaceAll("\\s+", " ").replaceAll("><", "> <")
				.trim();

		
		// assertXMLEqual( control, test );

		Diff myDiff = new Diff(control, test);
		myDiff.overrideElementQualifier(new ElementNameAndAttributeQualifier());
		
		//assertTrue(myDiff.similar());
		assertXMLEqual(myDiff, true);

	}

}
