package ar.com.bia.services;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ar.com.bia.TestContextConfig;
import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.dto.druggability.DruggabilitySearch;
import ar.com.bia.entity.PropertyUpload;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfig.class)
public class DruggabilityServiceTest {

	@Autowired
	private DruggabilityService druggabilityService;

	@Ignore
	@Test
	public void testDrugabilitySort() {
		DruggabilitySearch ds = new DruggabilitySearch();
		DruggabilityParam druggability = new DruggabilityParam();
		druggability.setType("variable");
		druggability.setCategory("protein");
		druggability.setCoefficient(10.0);
		druggability.setName("druggability");
		ds.getFormula().add(druggability);
		List<Map<String, Object>> druggabilityList = druggabilityService.druggabilityList("H37Rv", ds, 10);
		for (Map<String, Object> geneProductDoc : druggabilityList) {

			System.out.println(geneProductDoc);
		}
	}

	@Test
	public void testLoadProperties() throws Exception {
		InputStream resourceAsStream = this.getClass().getResourceAsStream("test_prop_records2.tbl");
		PropertyUpload upload =  druggabilityService.loadPropFiles("H37Rv", resourceAsStream, "demo");
		assertTrue("errors were detected loading test_prop_records", upload.getErrors().isEmpty());
//		resourceAsStream = this.getClass().getResourceAsStream("test_prop_keyvalue.tbl");
//		 upload = druggabilityService.loadPropFiles("H37Rv", resourceAsStream, "pepe");
//		assertTrue("errors were detected loading test_prop_keyvalue", upload.getErrors().isEmpty());
	}

}
