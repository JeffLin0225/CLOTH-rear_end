package com.cloth.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloth.Dao.ActivityDao;
import com.cloth.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer>,ActivityDao {
	
			Activity findByName(String name);
	default Activity findOrCreateActivity(String activityName) {
        Activity activity = findByName(activityName);
      
        return activity;
    }
	
	@Query(value="select count(*) from Activity where name = :name")
    public long countByName(String name);
	
	 @Query("SELECT id, name, discount_percent FROM Activity ")
	 List<Object[]> findAllProjectedActivities();
}
