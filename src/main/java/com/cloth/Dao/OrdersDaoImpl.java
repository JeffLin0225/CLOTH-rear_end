package com.cloth.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.cloth.config.DatetimeConverter;
import com.cloth.model.Orders;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class OrdersDaoImpl implements OrdersDao {
    @PersistenceContext
    private Session session;
    
    public Session getSession() {
        return this.session;
    }
    
    @Override
    public long count(JSONObject obj) {
        String name = obj.isNull("name") ? null : obj.getString("name");
        String status = obj.isNull("status") ? null : obj.getString("status");
        Date startDate = obj.isNull("startDate") ? null : DatetimeConverter.parse(obj.getString("startDate"), "yyyy-MM-dd");
        Date endDate = obj.isNull("endDate") ? null : DatetimeConverter.parse(obj.getString("endDate"), "yyyy-MM-dd");
        String createdBy = obj.isNull("createdBy") ? null : obj.getString("createdBy");
        String updatedBy = obj.isNull("updatedBy") ? null : obj.getString("updatedBy");

        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        // from
        Root<Orders> root = criteriaQuery.from(Orders.class);

        // select count(*)
        criteriaQuery.select(criteriaBuilder.count(root));

        // where
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("name"), name));

//            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (status != null) {
//            predicates.add(criteriaBuilder.equal(root.get("discount_percent"), discountPercent));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + status.toLowerCase() + "%"));

        }
        if (startDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("start_at"), startDate));
        }
        if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("end_at"), endDate));
        }
        if (createdBy != null && !createdBy.isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("created_by")), createdBy.toLowerCase()));
        }
        if (updatedBy != null && !updatedBy.isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("updated_by")), updatedBy.toLowerCase()));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Long> typedQuery = this.getSession().createQuery(criteriaQuery);
        Long result = typedQuery.getSingleResult();
        
        return result != null ? result : 0;
    }

    @Override
    public List<Orders> find(JSONObject obj) {
        Integer userid = obj.isNull("userid") ? null : obj.getInt("userid");
        String status = obj.isNull("status") ? null : obj.getString("status");
        Date startDate = obj.isNull("startDate") ? null : DatetimeConverter.parse(obj.getString("startDate"), "yyyy-MM-dd");
        Date endDate = obj.isNull("endDate") ? null : DatetimeConverter.parse(obj.getString("endDate"), "yyyy-MM-dd");
        String createdBy = obj.isNull("createdBy") ? null : obj.getString("createdBy");
        String updatedBy = obj.isNull("updatedBy") ? null : obj.getString("updatedBy");
        
		int start = obj.isNull("start") ? 0 : obj.getInt("start");
		int max = obj.isNull("max") ? 5 : obj.getInt("max");
		boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
		String order = obj.isNull("order") ? "id" : obj.getString("order");

        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Orders> criteriaQuery = criteriaBuilder.createQuery(Orders.class);

        // from
        Root<Orders> root = criteriaQuery.from(Orders.class);

        // where
        List<Predicate> predicates = new ArrayList<>();
        if (userid != null) {
//            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
          predicates.add(criteriaBuilder.equal(root.get("users").get("id"), userid));

        }
        if (status != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
//            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + status.toLowerCase() + "%"));

        }
        if (startDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("start_at"), startDate));
        }
        if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("end_at"), endDate));
        }
        if (createdBy != null && !createdBy.isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("created_by")), createdBy.toLowerCase()));
        }
        if (updatedBy != null && !updatedBy.isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("updated_by")), updatedBy.toLowerCase()));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        // order by (you can customize the order by criteria here)
//        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(order)));
        
        if(dir) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(order)));
		} else {
	        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(order)));
		}

        TypedQuery<Orders> typedQuery = this.getSession().createQuery(criteriaQuery)      
				.setFirstResult(start)
				.setMaxResults(max);
        
        List<Orders> activities = typedQuery.getResultList();
        return activities;
    }
}
