package ar.com.bia.controllers.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import ar.com.bia.TestContextConfig;
import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.backend.dao.impl.GODocumentRepositoryImpl;
import ar.com.bia.dto.GOTermTuple;
import ar.com.bia.dto.HierarchicNodeTreeSearchResult;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.services.JSTreeService;

/**
 * Imita click sobre arbol de AmiGO Se selecciona un nodo y se piden los hijos,
 * datos importantes ** Terminos ** Anotaciones ** Cantidades
 * 
 * El arbol GO de prueba se crea en crearArbol
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfig.class)
public class GOServiceTest {


	private @Autowired
	GeneProductDocumentRepository proteinRepository;

	private @Autowired
	GODocumentRepositoryImpl repositoryGO;

	private @Autowired
	JSTreeService goService;

	// /**
	// * Se usa esto y no GODocumentRepositoryImpl, ya que en
	// * {@link GODocumentRepositoryImpl} no se quiere utilizar/mapear el
	// * atributto succesors
	// */
	// private DBCollection goCollection;

	// private GOColumnServiceBOImpl columnService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// Conection

		// SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(
		// new MongoURI("mongodb://localhost:27017/test"));
		//
		// SimpleMongoMappingContext mappingContext = new
		// SimpleMongoMappingContext();
		// MappingMongoConverter converter = new MappingMongoConverter(
		// mongoDbFactory, mappingContext);

		// converter.setTypeMapper(MongoTypeMapper);
		// converter.setCustomConversions(new CustomConversions(Arrays.asList(
		// new CustomPropertiesReadConverter(),
		// new CustomPropertiesWriteConverter())));
		// converter.afterPropertiesSet();
		//
		// this.template = new MongoTemplate(mongoDbFactory, converter);

		// this.repositoryHmm = new HmmBlastResultDocumentRepositoryImpl();
		// this.repositoryHmm.setMongoOperations(template);
		// Como el template NO se carga por Spring, no lee las anotations y no
		// levanta el nombre de la coleccion anotada con @Collection
		// this.repositoryGO = new
		// GODocumentRepositoryImpl(this.template.getCollectionName(GODocument.class));
		// this.repositoryGO.setMongoOperations(template);

		// this.goCollection = mongoDbFactory.getDb("test").getCollection(
		// this.template.getCollectionName(GODocument.class));

		// TODO: mover la prueba de GOColumnServiceBOImpl a su propia clase de
		// test
		// this.columnService = new GOColumnServiceBOImpl();
		// this.columnService.setRepositoryGO(this.repositoryGO);
		// this.columnService.setRepositoryHmm(this.repositoryHmm);

		// Limpio la base
		this.repositoryGO.deleteAll();
		this.proteinRepository.deleteAll();

		// this.repositoryHmm.deleteAll();

		// Cargo los datos de prueba
		this.crearArbol();
	}

	// - molecular_function (go:0003674) [Prot4]
	// -- binding (go:0005488)
	// --- heterocyclic compound binding (go:1901363) : [Prot2]
	// ---- biotin binding (go:0009374) : [Prot1]
	// --- hydroxyapatite binding (go:0046848): [Prot3, Prot2]
	// **** antioxidant activity (go:0016209) : [Prot1 ]
	// -- antioxidant activity (go:0016209) : [Prot1 ]
	/**
	 * Arbol comentado arriba para que no se desarme al formatear el codigo Los
	 * - indican herencia, los * regulacion
	 */
	public void crearArbol() {
		insertTerm("go:0003674", "molecular_function", new String[] {
				"go:0005488", "go:0016209", "go:1901363", "go:0009374",
				"go:0046848" }, new String[] { "go:0005488", "go:0016209" },
				new String[] { "go:0005488", "go:0016209", "go:1901363",
						"go:0009374", "go:0046848" });
		insertTerm("go:0005488", "binding", new String[] { "go:0009374",
				"go:1901363", "go:0046848", "go:0046848" }, new String[] {
				"go:1901363", "go:0046848" }, new String[] { "go:0009374",
				"go:1901363", "go:0046848", "go:0046848" });
		insertTerm("go:1901363", "heterocyclic compound binding",
				new String[] { "go:0009374" }, new String[] { "go:0009374" },
				new String[] { "go:0009374" });
		insertTerm("go:0009374", "biotin binding", new String[] {},
				new String[] {}, new String[] {});
		insertTerm("go:0046848", "hydroxyapatite binding",
				new String[] { "go:0016209" }, new String[] {}, new String[] {});
		insertTerm("go:0016209", "antioxidant activity", new String[] {},
				new String[] {}, new String[] {});

		insertAnnotation("Prot1", new String[] { "go:0009374", "go:0016209" });
		insertAnnotation("Prot2", new String[] { "go:1901363", "go:0046848" });
		insertAnnotation("Prot3", new String[] { "go:0046848" });
		insertAnnotation("Prot4", new String[] { "go:0003674" });

		insertCount(new String[] { "go:0003674-4", "go:0005488-3",
				"go:1901363-2", "go:0009374-1", "go:0046848-3", "go:0016209-1", });
	}

	private void insertCount(String[] strings) {
		for (String string : strings) {
			String[] termCount = string.split("-");
			BasicDBObject index_entry = new BasicDBObject("term", termCount[0])
					.append("count", Integer.parseInt(termCount[1])).append(
							"name", "noimporta");
			this.goIndexCollection().insert(index_entry);
		}

	}

	private void insertAnnotation(String prot, String[] goTerms) {
		GeneProductDoc doc = new GeneProductDoc();
		doc.setName(prot);
		doc.setSeqCollectionId(genomeId());
		// doc.setProperties(new ArrayList<CustomProperties>());
		// CustomProperties customProperties = new CustomProperties();
		for (String goterm : goTerms) {
			doc.addKeyWord(goterm.toLowerCase());
		}
		// doc.setGoTerms(Arrays.asList(goTerms));
		// doc.getProperties().add(customProperties);

		this.proteinRepository.save(doc);
	}

	public ObjectId genomeId() {
		return new ObjectId("530e445ebe737e1c726cf614");
	}

	public DBCollection goCollection() {
		return this.repositoryGO.getMongoOperations().getCollection("go");
	}

	public DBCollection goIndexCollection() {
		return this.repositoryGO.getMongoOperations().getCollection("go_index");
	}

	private void insertTerm(String term, String name, String[] successors,
			String[] children, String[] subclases) {
		BasicDBObject goTerm = new BasicDBObject("term", term)
				.append("name", name).append("successors", successors)
				.append("children", children).append("subclases", subclases);
		this.goCollection().insert(goTerm);
	}

	/**
	 * Saca los "padres"/antecesores de un determinado termino, creo que servira
	 * para hacer histogramas
	 */
	// @Ignore
	// @Test
	// public void test() {
	//
	// // Get parents from succesor
	// Criteria criteria = Criteria.where("successors").all("go:0003723");
	// List<GODocument> find = this.template.find(new Query(criteria),
	// GODocument.class, "go");
	// assertEquals(5, find.size());
	// assertTrue(find.contains("go:0003676"));
	//
	// // Get Parents from successor list
	// criteria = Criteria.where("successors").in(
	// new String[] { "go:0003723", "go:0003723" });
	// find = this.template.find(new Query(criteria), GODocument.class);
	// assertEquals(1, find.size());
	// assertEquals("go:0003676", find.get(0).getTerm());
	// }

	/**
	 * Cuando se consulta el termino go:1901363 se espera go:1901363
	 * heterocyclic compound binding -- go:0009374 (1) -- Prot2
	 */
	@Test
	public void testGOTerm1901363() {
		String openNode = "go:1901363"; // heterocyclic compound binding

		HierarchicNodeTreeSearchResult result = this.goService.openTreeNode(
				null, openNode);

		assertEquals("Se espera que haya una anotacion propia", 1, result
				.getAnnotatedGenes().size());
		assertTrue("Se espera que las anotaciones sea Prot2", result
				.getAnnotatedGenes().get(0).getName().equals("Prot2"));

		assertEquals("Bajo el termino go:0009374 deberia haber una anotacion",
				new Long(1),
				this.findTuple(result.getDataTuples(), "go:0009374").getCount());

	}

	/**
	 * Cuando se consulta el termino go:0005488 se espera go:1901363 binding --
	 * go:1901363 (2) -- go:0046848 (3)
	 */
	@Test
	public void testGOTerm0005488() {
		String openNode = "go:0005488"; // binding

		HierarchicNodeTreeSearchResult result = this.goService.openTreeNode(
				null, openNode);

		assertEquals("Se espera que no haya una anotacion propia", 0, result
				.getAnnotatedGenes().size());

		assertEquals("Bajo el termino go:1901363 deberia haber 2 anotaciones",
				new Long(2),
				this.findTuple(result.getDataTuples(), "go:1901363").getCount());
		assertEquals("Bajo el termino go:0046848 deberia haber 3 anotaciones",
				new Long(3),
				this.findTuple(result.getDataTuples(), "go:0046848").getCount());

	}

	/**
	 * Cuando se consulta el termino go:0003674 se espera go:0003674 molecular
	 * function -- go:0005488 (3) -- go:0016209 (1) -- Prot4
	 */
	@Test
	public void testGOTerm0003674() {
		String openNode = "go:0003674"; // molecular function

		HierarchicNodeTreeSearchResult result = this.goService.openTreeNode(
				null, openNode);

		assertEquals("Se espera que haya una anotacion propia", 1, result
				.getAnnotatedGenes().size());
		assertTrue("Se espera que las anotaciones sea Prot4", result
				.getAnnotatedGenes().get(0).getName().equals("Prot4"));

		assertEquals("Bajo el termino go:0005488 deberia haber 3 anotaciones",
				new Long(3),
				this.findTuple(result.getDataTuples(), "go:0005488").getCount());
		assertEquals("Bajo el termino go:0016209 deberia haber una anotacion",
				new Long(1),
				this.findTuple(result.getDataTuples(), "go:0016209").getCount());

	}

	/**
	 * Cuando se consulta el termino go:0046848 se espera go:0046848
	 * hydroxyapatite binding -- go:0009374 (0) -- Prot2 -- Prot3
	 */
	@Test
	public void testGOTerm0046848() {
		String openNode = "go:0046848"; // hydroxyapatite binding

		HierarchicNodeTreeSearchResult result = this.goService.openTreeNode(
				null, openNode);

		assertEquals("Se espera que haya una anotacion propia", 2, result
				.getAnnotatedGenes().size());
		assertTrue("Se espera que las anotaciones sea Prot2", result
				.getAnnotatedGenes().get(0).getName().equals("Prot2")
				|| result.getAnnotatedGenes().get(1).getName()
						.equals("Prot2"));
		assertTrue("Se espera que las anotaciones sea Prot3", result
				.getAnnotatedGenes().get(0).getName().equals("Prot3")
				|| result.getAnnotatedGenes().get(1).getName()
						.equals("Prot3"));

		assertTrue("Bajo el termino go:0009374 NO deberia haber una anotacion",
				result.getDataTuples().isEmpty());

	}

	/**
	 * Prueba la limpieza de los terminos inferidos, o sea los que estan porque
	 * son generalizaciones de otros. En nuestro arbol: [0003674] --> 0005488
	 * --> [1901363] --> [0009374] [0003674] --> [0016209] [0003674] --> 0005488
	 * --> [0046848] [0003674] --> 0005488 --> [0046848]
	 * 
	 * 0003674 se puede inferir de cualquiera y 1901363 se puede inferir de
	 * 0009374 0046848 no se puede inferir de 0016209 ya que no es una relacion
	 * de herencia
	 * 
	 */
	@Test
	public void testInferredTerms() {
		List<String> terms = Arrays.asList(new String[] { "go:1901363",
				"go:0016209", "go:0003674", "go:0009374", "go:0046848" });

		List<String> termsWithoutInferred = this.repositoryGO
				.removeInferedTerms(terms);

		assertFalse(termsWithoutInferred.contains("go:1901363"));
		assertTrue(termsWithoutInferred.contains("go:0016209"));
		assertFalse(termsWithoutInferred.contains("go:0003674"));
		assertTrue(termsWithoutInferred.contains("go:0009374"));
		assertTrue(termsWithoutInferred.contains("go:0046848"));

	}

	private GOTermTuple findTuple(Collection<GOTermTuple> tuplas, String id) {
		return  tuplas.stream().filter(x -> x.getId().equals(id)).findFirst().get();
	}

	

}
