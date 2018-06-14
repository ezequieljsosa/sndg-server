package ar.com.bia.backend.dao;

import ar.com.bia.pdb.StructureDoc;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface StructureRepository extends PagingAndSortingRepository<StructureDoc, String>  {


	List<StructureDoc> findAll(Query query);
	StructureDoc findByName(String name);

}
