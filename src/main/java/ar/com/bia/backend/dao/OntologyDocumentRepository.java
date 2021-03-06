package ar.com.bia.backend.dao;

import ar.com.bia.entity.OntologyTerm;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OntologyDocumentRepository  extends PagingAndSortingRepository<OntologyTerm, String> {

	List<OntologyTerm> findAll(Query query);

	

}
