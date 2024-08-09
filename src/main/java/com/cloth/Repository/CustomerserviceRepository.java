package com.cloth.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloth.Dao.CustomerserviceDao;
import com.cloth.model.Customerservice;
import com.cloth.model.Users;

public interface CustomerserviceRepository extends JpaRepository<Customerservice, Integer> ,CustomerserviceDao{
   
	@Query("SELECT c.users FROM Customerservice c GROUP BY c.users")	
	List<Users> findByAdmin();
	
	@Query("FROM Customerservice c where c.users.id = :userid")	
	List<Customerservice> findMessageByUserID(Integer userid);
	
    @Query(value = "select count(*) FROM Customerservice where users.id = :userid")
    public long countByUserid(Integer userid);
    
    @Query(value = "select count(*) FROM Customerservice where users.id = :userid AND isAdminRead = 0")
    public long countByUseridisAdminRead(Integer userid);
    
    
///user專用

    @Query(value="FROM Customerservice c where c.users.id = :userid")
    List<Customerservice> findALLmesssageByUserid(Integer userid);
    
    @Query(value="select count(*) FROM Customerservice where users.id = :userid AND isUserRead = 0")
    long countisUserUnread (Integer userid);


}
