package com.cloth.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloth.model.Orderdetail;

public interface OrderdetailRepository extends JpaRepository<Orderdetail, Integer> {
    
    @Query("SELECT od FROM Orderdetail od JOIN od.orders o WHERE o.status = :status")
    List<Orderdetail> findByOrdersStatus( String status);
    
    @Query("SELECT od FROM Orderdetail od JOIN od.orders o WHERE o.id = :ordersId")
    List<Orderdetail> findByOrdersId(Integer ordersId);
    
    Orderdetail findByCartId(Integer cartId);

	
	 List<Orderdetail> findByCartUsersId(Integer userId);

    List<Orderdetail> findByCartUsersIdAndCartProductId(Integer userId, Integer productId);
}
