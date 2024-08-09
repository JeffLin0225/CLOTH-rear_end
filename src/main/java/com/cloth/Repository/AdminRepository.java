package com.cloth.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloth.Dao.AdminDao;
import com.cloth.model.Admin;


public interface AdminRepository extends JpaRepository<Admin, Integer>,AdminDao {
	
	@Query("from Admin where username = :username")
	Admin findByUsername (String username);


}
