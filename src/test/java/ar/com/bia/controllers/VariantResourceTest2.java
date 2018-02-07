package ar.com.bia.controllers;




import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ar.com.bia.TestContextConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfig.class)
public class VariantResourceTest2 {

//	private @Autowired
//	GeneProductDocumentRepository proteinRepo;
//
//	private @Autowired
//	SeqCollectionRepository seqColRepo;
//
//	private @Autowired
//	VariantResource variantResource;
//
//	private final String organism = "tuberculosis";
//
//	private final String strain1 = "strain1";
//	private final String strain2 = "strain2";
//	private final String strain3 = "strain3";
//	
//	private final String prot1 = "prot1";
//	private final String prot2 = "prot2";
//	private final String prot3 = "prot3";
//	private final String prot22 = "prot22";

	// Dataset:
	// seq_collections: 4 colecciones // TODO ver que sean de peptidos
	// 1) tuberq - strain1
	// 2) tuberq - strain2
	// 3) tuberq - strain3
	// 4) otro - strainnoimporta
	//
	// proteinas: 4 proteinas
	//
	// proteina1
	//	coleccion 1
	//  variantes: strain2 y strain3
	// proteina2
	//  coleccion2
	//	variantes: strain3
	// proteina22
	//	coleccion2
	// proteina3
	//  coleccion3

//	/**
//	 * 
//	 * @throws Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//
//		this.proteinRepo.deleteAll();
//		this.seqColRepo.drop();
//
//		SeqCollectionDoc seqCollection1 = new SeqCollectionDoc();
//		seqCollection1.setOrganism(this.organism);
//		seqCollection1.setStrain(this.strain1);
//		this.seqColRepo.save(seqCollection1);
//
//		SeqCollectionDoc seqCollection2 = new SeqCollectionDoc();
//		seqCollection2.setOrganism(this.organism);
//		seqCollection2.setStrain(this.strain2);
//		this.seqColRepo.save(seqCollection2);
//
//		SeqCollectionDoc seqCollection3 = new SeqCollectionDoc();
//		seqCollection3.setOrganism(this.organism);
//		seqCollection3.setStrain(this.strain3);
//		this.seqColRepo.save(seqCollection3);
//
//		SeqCollectionDoc seqCollection4 = new SeqCollectionDoc();
//		seqCollection4.setOrganism("otro organismo");
//		seqCollection4.setStrain(this.strain3);
//		this.seqColRepo.save(seqCollection4);
//
//		
//		
//		GeneProductDoc protein1 = new GeneProductDoc();
//		protein1.setName(this.prot1);		
//		VariantEmbedDoc prot1var2 = new VariantEmbedDoc();
//		prot1var2.setStrain(this.strain2);
//		protein1.addVariant(prot1var2);
//		VariantEmbedDoc prot1var3 = new VariantEmbedDoc();
//		prot1var3.setStrain(this.strain3);
//		protein1.addVariant(prot1var3);
//		protein1.setSeqCollectionId( new ObjectId(seqCollection1.getId()));
//		this.proteinRepo.save(protein1);
//
//		GeneProductDoc protein2 = new GeneProductDoc();
//		protein2.setName(this.prot2);
//		VariantEmbedDoc prot2var3 = new VariantEmbedDoc();
//		prot2var3.setStrain(this.strain3);
//		protein2.addVariant(prot2var3);
//		protein2.setSeqCollectionId( new ObjectId(seqCollection2.getId()));
//		this.proteinRepo.save(protein2);
//
//		GeneProductDoc protein3 = new GeneProductDoc();
//		protein3.setName(this.prot3);
//		protein3.setSeqCollectionId( new ObjectId(seqCollection3.getId()));
//		
//		
//		this.proteinRepo.save(protein3);
//
//		GeneProductDoc protein22 = new GeneProductDoc();
//		protein22.setName(this.prot22);
//		protein22.setSeqCollectionId( new ObjectId(seqCollection2.getId()));
//		this.proteinRepo.save(protein22);
//
//	}
//
//	@Test
//	public void testStrainsFromOrganism() {
//
//		List<SeqCollectionDoc> strains = variantResource.strains(this.organism);
//		assertEquals(3, strains.size());
//	}
//
//	@Test(expected = InvalidParameterException.class)
//	public void testNoStrains() {
////		variantResource.variants("prot1\nprot2", null);
//	}
//
//	@Test(expected = InvalidParameterException.class)
//	public void testNoProteins() {
//		variantResource.variants("", "strain1\nstrain2");
//	}
//	/**
//	 * La proteina 2 y 22 no estan en la strain1
//	 */
//	@Test
//	public void testProteinsWithNoStrains() {
//		Map<String, List<VariantEmbedDoc>> variants = variantResource.variants("prot2\nprot22", "strain1");
//		assertEquals("las proteinas 2 y 22 no deberian estar en el strain1",0, variants.get(this.prot2).size());
//		assertEquals("las proteinas 2 y 22 no deberian estar en el strain1",0, variants.get(this.prot22).size());
//	}
//
//	/**
//	 * La proteina 22 solo esta en el strain2
//	 */
//	@Test
//	public void testProteinInOneStrains() {
//		Map<String, List<VariantEmbedDoc>> variants = variantResource.variants("prot22", "strain1\nstrain2\nstrain3");
//		assertEquals("Solo deberia haber una variante",1,variants.size());
//		assertEquals("Solo deberia estar la prot22 en el strain 2",this.strain2,variants.get(this.prot22).get(0).getStrain());
//	}
//
//	/**
//	 * La proteina 1 esta en todos los strains
//	 */
//	@Test
//	public void testProteinInManyStrains() {
//		Map<String, List<VariantEmbedDoc>> variants = variantResource.variants(this.prot1, "strain1\nstrain2\nstrain3");
//		assertEquals("Solo deberia haber 3 variantes",3,variants.get(this.prot1).size());
//		assertTrue("Deberia estar la prot1 en el strain 1",this.inStrain(this.strain1,variants.get(this.prot1)));
//		assertTrue("Deberia estar la prot1 en el strain 2",this.inStrain(this.strain2,variants.get(this.prot1)));
//		assertTrue("Deberia estar la prot1 en el strain 3",this.inStrain(this.strain3,variants.get(this.prot1)));
//		
//	}	
//
//	/**
//	 * La proteina 1 esta en todos los strains y la 2 en el strain2 y el 3
//	 */
//	@Test
//	public void testProteinsInManyStrains() {
//		
//		Map<String, List<VariantEmbedDoc>> variants = this.variantResource.variants("prot1\nprot2", "strain1\nstrain2");;
//		
//		assertEquals("Solo deberia haber 3 variantes",3,variants.get(this.prot1).size() + variants.get(this.prot2).size());
//		
//		assertTrue("Deberia estar la prot1 en el strain 1", this.inStrain(this.strain1,variants.get(this.prot1)));
//		assertTrue("Deberia estar la prot1 en el strain 2",this.inStrain(this.strain2,variants.get(this.prot1)));
//		assertFalse("La prot1 no deberia estar  en el strain 3 ya que no se consulto dicho strain",
//				this.inStrain(this.strain3,variants.get(this.prot1)));
//		
//		assertTrue("Deberia estar la prot2 en el strain 2", this.inStrain(this.strain2,variants.get(this.prot2)));
//		assertFalse("La prot2 no deberia estar  en el strain 3 ya que no se consulto dicho strain",this.inStrain(this.strain3,variants.get(this.prot2)));
//		assertFalse("La prot2 no esta en el strain 1",
//				this.inStrain(this.strain1,variants.get(this.prot2)));
//		
//	}
//
//	public boolean inStrain(String strain,List<VariantEmbedDoc> variants){
//		for (VariantEmbedDoc variantEmbedDoc : variants) {
//			if(variantEmbedDoc.getStrain().equals(strain))
//					return true;			
//		}
//		return false;
//	}
	
}
