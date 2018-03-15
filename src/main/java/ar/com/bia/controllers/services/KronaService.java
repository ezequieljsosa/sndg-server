package ar.com.bia.controllers.services;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.aspectj.util.FileUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.thoughtworks.xstream.XStream;

import ar.com.bia.backend.dao.GODocumentRepository;
import ar.com.bia.backend.dao.SeqCollectionRepository;
import ar.com.bia.config.ContextConfig;
import ar.com.bia.dto.krona.KronaAttribute;
import ar.com.bia.dto.krona.KronaDocument;
import ar.com.bia.dto.krona.KronaNode;
import ar.com.bia.dto.krona.KronaNodeConverter;
import ar.com.bia.entity.OntologyDoc;
import ar.com.bia.entity.SeqCollectionDoc;

@Controller
@RequestMapping("/krona")
public class KronaService {

	private static final String SEARCH_URL = "../search/";
	private static final String TERM = "term";

	private static final String COUNT = "count";

	private static final String NO_DATA = "NO-DATA";

	private @Autowired MongoTemplate mongoTemplate;

	private @Autowired GODocumentRepository repositoryGO;

	private @Autowired SeqCollectionRepository repositorySeqCol;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public GODocumentRepository getRepositoryGO() {
		return repositoryGO;
	}

	public void setRepositoryGO(GODocumentRepository repositoryGO) {
		this.repositoryGO = repositoryGO;
	}

	public SeqCollectionRepository getRepositorySeqCol() {
		return repositorySeqCol;
	}

	public void setRepositorySeqCol(SeqCollectionRepository repositorySeqCol) {
		this.repositorySeqCol = repositorySeqCol;
	}

	
	@RequestMapping(value = "/stats/{filename:.+}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String stats(@PathVariable("filename") String filename ) throws IOException {

		 byte[] encoded = Files.readAllBytes(Paths.get("/data/xomeq/krona/" + filename ));
		 return new String(encoded, Charset.availableCharsets().get("UTF-8"));
		
	}
	
	
	@RequestMapping(value = "/{genome:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String go(@PathVariable("genome") String genome, @RequestParam(value = TERM) String term,
			@RequestParam(value = "level", defaultValue = "4") Long maxLevel,
			@RequestParam(value = "ontology") String ontology) throws IOException {

		SeqCollectionDoc seqCol = repositorySeqCol.findByName(genome);
		String seqCollection = seqCol.getId();
		if (seqCol.getProteomeId() != null)
			seqCollection = seqCol.getProteomeId();

		String filepath = "/data/organismos/" + seqCol.getName() + "/xomeq/krona_" + ontology + "_" + term + ".xml";
		if (new File(filepath).exists()) {
			return FileUtil.readAsString(new File(filepath));

		} else {
			System.out.println("Creating idx " + ontology + ":" +  genome);
			XStream xstream = new XStream();
			xstream.registerConverter(new KronaNodeConverter(this.keywordSearchURL(genome)));

			// + URLEncoder.encode(seqCol.getOrganism()) + "&keywords="));
			xstream.processAnnotations(KronaDocument.class);

			KronaDocument krona = new KronaDocument();
			KronaAttribute goAttr = new KronaAttribute(ontology.toUpperCase() + " Annotations", ontology);

			krona.getAttributes().addAttribute(goAttr);
			krona.getAttributes().setMagnitude(ontology);
			krona.getColor().setAttribute(ontology);

			OntologyDoc rootTerm = this.repositoryGO.findTerm(ontology, term, true);
			krona.addDataset(rootTerm.getName());

			KronaNode rootNone = krona.getNode();

			long count = termCount(seqCollection, ontology, rootTerm);
			rootNone.setName(ontology);
			// rootNone.setHref("");
			rootNone.addAttribute(ontology, Arrays.asList(new Long[] { count }));

			boolean existsData = this.mongoTemplate.getCollection(indexName(ontology))
					.findOne(new BasicDBObject("seq_collection_id", new ObjectId(seqCollection))) != null;
			if (existsData) {
				openNode(rootTerm, rootNone, 0, seqCollection, term, 1, maxLevel, ontology);
			} else {
				buildNoDataNode(rootNone, ontology);
			}

			byte[] ptext = xstream.toXML(krona).getBytes("UTF-8");
			String string = new String(ptext);
			try {
				FileUtil.writeAsString(new File(filepath), string);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return string;

		}

	}

	private String keywordSearchURL(String genomeName) {
		return SEARCH_URL + genomeName + "/product/";
	}

	public long openNode(OntologyDoc termDoc, KronaNode node, long count, String genome, String term, long level,
			long maxLevel, String ontology) {

		// OntologyDoc termDoc = this.repositoryGO.findTerm(ontology,
		// term,true);
		if (termDoc == null) {
			// If the term is not found
			return buildNoDataNode(node, ontology);
		} else {

			if (level > maxLevel)
				// Max Level reached
				return count;

			Collection<String> children = new HashSet<>( termDoc.getChildren());

			long bpRemainingAnnotations = count;
//			Set<String> slimSet = this.repositoryGO.slimSet();

			for (String child : children) {

				OntologyDoc childTermDoc = this.repositoryGO.findTerm(ontology, child, false);
				long childRemainingAnnotations = 0;
				
				KronaNode childNode = new KronaNode("?");
				childNode.setName(childTermDoc.getName());
				childNode.setHref(childTermDoc.getTerm().replace(".-", ""));
				long childNodeCount = termCount(genome, ontology, childTermDoc);
				childNode.addAttribute(ontology, Arrays.asList(new Long[] { childNodeCount }));

				if (childNodeCount == 0)
					// If the parent has not associated genes, neither do
					// the
					// children
					continue;

				childRemainingAnnotations = openNode(childTermDoc, childNode, childNodeCount, genome, child, level + 1,
						maxLevel, ontology);
				if (childRemainingAnnotations != 0) {
					bpRemainingAnnotations -= childRemainingAnnotations;
					node.addNode(childNode);
				}
				// }
			}

			if (bpRemainingAnnotations < 0) {
				count = count - bpRemainingAnnotations;
				if (termDoc.getOntology().equals("go")){
					node.addAttribute(ontology, Arrays.asList(new Long[] { count }));	
				}
				
			}

			if (bpRemainingAnnotations > 0) {
				KronaNode remainingAnnotationsNode = new KronaNode(termDoc.getName() + "_");
				remainingAnnotationsNode.setHref(termDoc.getTerm().replace(".-", ""));
				remainingAnnotationsNode.addAttribute(ontology, new Long[] { bpRemainingAnnotations });
				node.addNode(remainingAnnotationsNode);
			}
			return count;
		}
	}

	private long buildNoDataNode(KronaNode node, String ontology) {
		String childName = NO_DATA;
		node.setName(NO_DATA);
		KronaNode childNode = new KronaNode(childName);
		childNode.addAttribute(ontology, new Long[] { 1L });
		childNode.addAttribute(ontology, Arrays.asList(new Long[] { 1L }));

		node.addAttribute(ontology, Arrays.asList(new Long[] { 01L }));
		node.addNode(childNode);
		return 0;
	}

	private Long termCount(String seqCollection, String ontology, OntologyDoc termDoc) {
		BasicDBObject query = new BasicDBObject(TERM, termDoc.getTerm()).append("seq_collection_id", new ObjectId(seqCollection));
		DBObject indexTerm = this.mongoTemplate.getCollection(indexName(ontology)).findOne(
				query);
		if (indexTerm == null) {
			return 0L;
		}
		Object objectCount = indexTerm.get(COUNT);
		Long count = ((Double) (Double.parseDouble(objectCount.toString()))).longValue();
		return count;
	}

	private String indexName(String ontology) {
		return "col_ont_idx"; // ontology + "_index";
	}

	// // @RequestMapping(value = "/{genome}", method = RequestMethod.GET,
	// produces
	// // = MediaType.APPLICATION_XML_VALUE)
	// // @ResponseBody
	// public String test(@PathVariable("genome") String genome)
	// throws UnsupportedEncodingException {
	// XStream xstream = new XStream();
	// xstream.registerConverter(new KronaNodeConverter());
	// xstream.processAnnotations(KronaDocument.class);
	// KronaDocument krona = new KronaDocument();
	// KronaAttribute grams = new KronaAttribute("Grams", "grams");
	// KronaAttribute dailyValue = new KronaAttribute("% Daily Value", "dva");
	// krona.getAttributes().addAttribute(grams);
	// krona.getAttributes().addAttribute(dailyValue);
	// krona.getAttributes().setMagnitude("grams");
	// krona.getColor().setAttribute("grams");
	// krona.addDataset("Brand X");
	// krona.addDataset("Brand Y");
	//
	// krona.getNode().setName("Drugs serving");
	// krona.getNode().addAttribute("grams",
	// Arrays.asList(new Long[] { 55L, 55L }));
	//
	// KronaNode protein = new KronaNode("Marihuana");
	// protein.addAttribute("grams", new Long[] { 5L, 6L });
	// KronaNode cocain = new KronaNode("Cocaina");
	// cocain.addAttribute("grams", new Long[] { 39L, 41L });
	// cocain.addAttribute("dva", new Long[] { 13L, 14L });
	//
	// KronaNode sugar = new KronaNode("Sugars");
	// sugar.addAttribute("grams", new Long[] { 3L, 5L });
	//
	// KronaNode fiver = new KronaNode("Dietary fiber");
	// fiver.addAttribute("grams", new Long[] { 4L, 8L });
	// fiver.addAttribute("dva", new Long[] { 16L, 32L });
	// cocain.addNode(sugar);
	// cocain.addNode(fiver);
	//
	// KronaNode fat = new KronaNode("Fats");
	// fat.addAttribute("grams", new Long[] { 8L, 7L });
	// fat.addAttribute("dva", new Long[] { 12L, 9L });
	//
	// krona.getNode().addNode(protein);
	// krona.getNode().addNode(cocain);
	// krona.getNode().addNode(fat);
	// byte[] ptext = xstream.toXML(krona).getBytes("UTF-8");
	// return new String(ptext);
	// }

	// @RequestMapping(value = "/{genome}/prueba2", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_XML_VALUE)
	// public String xmlprueba2(@PathVariable("genome") String genome) {
	// String str = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>"
	// + " <krona>"
	// + " <attributes magnitude=\"grams\">"
	// + " <attribute display=\"Grams\">grams</attribute>"
	// + " <attribute display=\"% Daily Value\">dva</attribute>"
	// + " </attributes>"
	// + " <color attribute=\"grams\" valueStart=\"0\" valueEnd=\"55\"
	// hueStart=\"120\" hueEnd=\"240\" ></color>"
	// + " <datasets>" + " <dataset>Brand XXty</dataset>"
	// + " </datasets>" + " <node name=\"Drugs serving\">"
	// + " <grams><val>55</val></grams>"
	// + " <node name=\"Protein\">" + " <grams><val>5</val>/grams>"
	// + " </node>" + " <node name=\"Cocaina\">"
	// + " <grams><val>39</val></grams>"
	// + " <dva><val>13</val></dva>" + " <node name=\"Sugars\">"
	// + " <grams><val>3</val></grams>" + " </node>"
	// + " <node name=\"Dietary fiber\">"
	// + " <grams><val>4</val></grams>"
	// + " <dva><val>16</val></dva>" + " </node>" + " </node>"
	// + " <node name=\"Fats\">" + " <grams><val>8</val></grams>"
	// + " <dva><val>12</val></dva>"
	// + " <node name=\"Saturated fat\">"
	// + " <grams><val>2</val></grams>"
	// + " <dva><val>10</val></dva>" + " </node>"
	// + " <node name=\"Unsaturated fat\">"
	// + " <grams><val>6</val></grams>"
	// + " <node name=\"Polyunsaturated fat\">"
	// + " <grams><val>3</val>/grams>" + " </node>"
	// + " <node name=\"Monounsaturated fat\">"
	// + " <grams><val>3</val></grams>" + " </node>"
	// + " </node>" + " </node>" + " </node>" + "</krona>";
	//
	// return str;
	// }
	//
	// @RequestMapping(value = "/{genome}/prueba", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_XML_VALUE)
	// public String xmlprueba1(@PathVariable("genome") String genome) {
	// String str = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>"
	// + " <krona>"
	// + " <attributes magnitude=\"grams\">"
	// + " <attribute display=\"Grams\">grams</attribute>"
	// + " <attribute display=\"% Daily Value\">dva</attribute>"
	// + " </attributes>"
	// + " <color attribute=\"grams\" valueStart=\"0\" valueEnd=\"55\"
	// hueStart=\"120\" hueEnd=\"240\"></color>"
	// + " <datasets>" + " <dataset>Brand X</dataset>"
	// + " <dataset>Brand Y</dataset>" + " </datasets>"
	// + " <node name=\"Drugs serving\">"
	// + " <grams><val>55</val><val>55</val></grams>"
	// + " <node name=\"Protein\">"
	// + " <grams><val>5</val><val>6</val></grams>" + " </node>"
	// + " <node name=\"Cocaina\">"
	// + " <grams><val>39</val><val>41</val></grams>"
	// + " <dva><val>13</val><val>14</val></dva>"
	// + " <node name=\"Sugars\">"
	// + " <grams><val>3</val><val>5</val></grams>" + " </node>"
	// + " <node name=\"Dietary fiber\">"
	// + " <grams><val>4</val><val>8</val></grams>"
	// + " <dva><val>16</val><val>32</val></dva>" + " </node>"
	// + " </node>" + " <node name=\"Fats\">"
	// + " <grams><val>8</val><val>7</val></grams>"
	// + " <dva><val>12</val><val>9</val></dva>"
	// + " <node name=\"Saturated fat\">"
	// + " <grams><val>2</val><val>1</val></grams>"
	// + " <dva><val>10</val><val>5</val></dva>" + " </node>"
	// + " <node name=\"Unsaturated fat\">"
	// + " <grams><val>6</val><val>6</val></grams>"
	// + " <node name=\"Polyunsaturated fat\">"
	// + " <grams><val>3</val><val>4</val></grams>"
	// + " </node>" + " <node name=\"Monounsaturated fat\">"
	// + " <grams><val>3</val><val>2</val></grams>"
	// + " </node>" + " </node>" + " </node>" + " </node>"
	// + "</krona>";
	//
	// return str;
	// }

	public static void main(String[] args) throws IOException {

		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ContextConfig.class);
		
		KronaService krona = new KronaService();
		krona.setRepositoryGO(ctx.getBean(GODocumentRepository.class));

		SeqCollectionRepository repositorySeqCol = ctx.getBean(SeqCollectionRepository.class);
		krona.setRepositorySeqCol(repositorySeqCol);

		krona.setMongoTemplate(((MongoTemplate)ctx.getBean("mongoTemplate")));

		File file = new File("/data/organismos");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		String[] indexFiles = new String[] { "krona_ec_root.xml", "krona_go_go:0003674.xml", "krona_go_go:0005575.xml",
				"krona_go_go:0008150.xml" };
		for (String dir : directories) {
			System.out.println(dir);
			for (String idxfile : indexFiles) {
				if (!new File("/data/organismos/" + dir + "/xomeq/" + idxfile).exists()) {
					System.out.println(idxfile);
					String term = idxfile.split("_")[2].replace(".xml","");
					String ontology = idxfile.split("_")[1];
						SeqCollectionDoc seqCol = repositorySeqCol.findByName(dir);
						if (seqCol != null){
							krona.go(dir, term, 4L, ontology);	
						}
						
					
				}
			}

		}


	}

}
