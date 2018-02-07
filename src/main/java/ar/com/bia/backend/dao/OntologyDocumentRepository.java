package ar.com.bia.backend.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import ar.com.bia.entity.OntologyTerm;

public interface OntologyDocumentRepository  extends PagingAndSortingRepository<OntologyTerm, String> {

	List<OntologyTerm> findAll(Query query);

	

}
