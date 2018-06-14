package ar.com.bia.backend.dao;

import ar.com.bia.entity.ContigDoc;
import ar.com.bia.entity.GeneDoc;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface GeneDocumentRepository  extends PagingAndSortingRepository<GeneDoc, String> {

	GeneDoc findTerm(String gene);

	long count(Query query);

	List<GeneDoc> findAll(Query query);

	GeneDoc save(GeneDoc gene,  ContigDoc contig);

	GeneDoc findByName(String name);

	GeneDoc findBy(String string, Object locusTag);
	
	GeneDoc findOrganismGene(String organism, List<Object> locusTags);


	

}
