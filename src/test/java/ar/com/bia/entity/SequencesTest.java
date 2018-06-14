package ar.com.bia.entity;

import ar.com.bia.backend.dao.ContigDocumentRepository;
import ar.com.bia.backend.dao.GeneDocumentRepository;
import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class) // classes = TestContextConfig.class
public class SequencesTest {

	

	private @Autowired
	ContigDocumentRepository contigRepository;
	
	private @Autowired
	GeneDocumentRepository geneRepository;
	
	private @Autowired
	GeneProductDocumentRepository proteinRepository;
	

	private ContigDoc contig;
	
	@Before
	public void setUp(){
		this.contigRepository.deleteAll();
		this.proteinRepository.deleteAll();
		
		this.contig = new ContigDoc();
		contig.setId("1");
		contig.setName("contigA");
		
		this.contigRepository.save(contig);
	}
	
	@Test
	public void testSaveGeneAndProtein(){
		
		GeneDoc gene = new GeneDoc();
		String geneSymbol = "aGene";
		gene.setName(geneSymbol);
		//String genePos = "667";
		Location locus = new Location("?",1,2,"+");
		
		gene.setLocation(locus);
		
		
		
		Size size = new Size(300,"bp");
		gene.setSize(size);
		ObjectId colID = new ObjectId();
		gene.setSeqCollectionId(colID);
		ObjectId geneId = new ObjectId();
		gene.setId(geneId.toStringMongod());
		
		GeneProductDoc protein = new GeneProductDoc();
		String symbol = "aprotein";
		protein.setName(symbol);
//		protein.setGene_id(geneId.toStringMongod());
		protein.setLocation(locus);
		String protDescription = "soy una proteina";
		protein.setDescription(protDescription);
		
		this.geneRepository.save(gene,contig);
		this.proteinRepository.save(protein);
		
		
		
		GeneProductDoc proteinDoc = this.proteinRepository.findByName(symbol);
//		assertEquals(geneId.toStringMongod(),proteinDoc.getGene_id());
		assertEquals(locus,proteinDoc.getLocus());
		assertEquals(protDescription,proteinDoc.getDescription());
		
		GeneDoc geneDoc = this.geneRepository.findOne(geneId.toStringMongod());
		assertEquals(geneSymbol,geneDoc.getName());
		assertEquals(locus,geneDoc.getLocation());
		assertEquals(size,geneDoc.getSize());
		
	}
	
	
	
}










