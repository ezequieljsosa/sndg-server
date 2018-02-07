package ar.com.bia.backend.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import ar.com.bia.pdb.StructureDoc;


public interface StructureRepository extends PagingAndSortingRepository<StructureDoc, String>  {


	List<StructureDoc> findAll(Query query);
	StructureDoc findByName(String name);

}
