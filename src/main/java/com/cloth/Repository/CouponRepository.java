package com.cloth.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloth.Dao.CouponDao;
import com.cloth.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer>,CouponDao {
	
	
	@Query(value="from Coupon where name = :name")
	Coupon findByName(String name);
	
//	default Coupon findOrCreateActivity(String activityName) {
//		Coupon activity = findByName(activityName);
//      
//        return activity;
//    }
	
	@Query(value="select count(*) from Coupon where name = :name")
    public long countByName(String name);
	
	 @Query("SELECT id, name,description ,discount FROM Coupon ")
	 List<Object[]> findAllProjectedCoupons();
	 

	
		
}
