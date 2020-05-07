package com.cg.iter.shelftimereport.repository;

import java.util.List;

import javax.transaction.Transactional;


import org.springframework.data.repository.CrudRepository;


import com.cg.iter.shelftimereport.dto.RetailerInventoryDTO;

@Modifying
@Transactional
@Query("select ri from RetailerInventoryDTO ri where ri.retailerId=:retailerId")
public interface RetailerInventoryRepository extends CrudRepository<RetailerInventoryDTO, String> {
	
	List <RetailerInventoryDTO> findAllByretailerId(int retailerId);
	



}
