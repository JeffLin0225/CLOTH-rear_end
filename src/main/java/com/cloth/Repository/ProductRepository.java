package com.cloth.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloth.Dao.ProductDao;
import com.cloth.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>,JpaSpecificationExecutor<Product>, ProductDao {
	@Query(value="select count(*) from Product where name = :name")
    public long countByName(String name);
	
	 @Query(value = "select img, img2, img3, img4 from Product where id = :id", nativeQuery = true)
	    List<Product> findProductsByPhotoid(@Param("id") Integer id);
	
}
