package com.cg.iter.shelftimereport.repository;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cg.iter.shelftimereport.dto.RetailerInventoryDTO;


public interface RetailerInventoryRepository extends CrudRepository<RetailerInventoryDTO, String> {
	@Modifying
	@Transactional
	@Query("select ri from RetailerInventoryDTO ri where ri.retailerId=:retailerId")
	List <RetailerInventoryDTO> findAllByretailerId(int retailerId);
	



}
