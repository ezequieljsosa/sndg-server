package ar.com.bia.backend.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import ar.com.bia.entity.ContigDoc;
import ar.com.bia.entity.SeqFeature;

public interface ContigDocumentRepository  extends PagingAndSortingRepository<ContigDoc, String> {

	ContigDoc findTerm(String gene);

	long count(Query query);

	List<ContigDoc> findAll(Query query);

	

}
