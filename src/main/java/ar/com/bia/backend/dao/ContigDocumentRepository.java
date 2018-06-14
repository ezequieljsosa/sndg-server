package ar.com.bia.backend.dao;

import ar.com.bia.entity.ContigDoc;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ContigDocumentRepository  extends PagingAndSortingRepository<ContigDoc, String> {

	ContigDoc findTerm(String gene);

	long count(Query query);

	List<ContigDoc> findAll(Query query);

	

}
