package ar.com.bia.backend.dao;

import ar.com.bia.entity.OntologyDoc;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface GODocumentRepository  extends PagingAndSortingRepository<OntologyDoc, String> {

	OntologyDoc findTerm(String ontology,String goTerm,Boolean  slim);

	long count(Query query);

	List<OntologyDoc> findAll(Query query);

	List<String> removeInferedTerms(List<String> list);

	List<String> succesors(String ontology,String child);

	Set<String> slimSet();

	

}
