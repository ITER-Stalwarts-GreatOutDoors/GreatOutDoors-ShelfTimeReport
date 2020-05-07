package com.cg.iter.shelftimereport.repository;

import org.springframework.data.repository.CrudRepository;

import com.cg.iter.shelftimereport.dto.UserDTO;

public interface UserRepository extends CrudRepository<UserDTO, String>{

}
