package com.cloth.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloth.model.Users;


public interface UsersRepository extends JpaRepository<Users, Integer> {
	
	
}
