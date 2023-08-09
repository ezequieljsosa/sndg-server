package ar.com.bia.backend.dao;

import ar.com.bia.entity.CompoundDoc;
import ar.com.bia.pdb.StructureDoc;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompoundRepository extends PagingAndSortingRepository<CompoundDoc, String>  {


	List<CompoundDoc> findAll(Query query);
	long count(Query query);
	CompoundDoc findByName(String name);

}
