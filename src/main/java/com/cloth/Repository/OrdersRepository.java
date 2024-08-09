package com.cloth.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloth.Dao.OrdersDao;
import com.cloth.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer>, OrdersDao {

    @Query(value = "select count(*) from Orders where status = :status")
    public long countByName(String status);

    @Query(value = "from Orders where status = :status order by created_at asc")
    public List<Orders> findUnDoneOrders(String status);

    @Query(value = "select count(*) from Orders where status = :status ")
    public long countUnDoneOrders(String status);

    @Query(value = "from Orders where status = :status order by shipping_at desc")
    public List<Orders> findDoneOrders(String status);

    @Query(value = "select count(*) from Orders where status = :status ")
    public long countDoneOrders(String status);

    @Query("from Orders where users = :usersId ")
    List<Orders> findByUsersId(Integer usersId);

}
