package com.cloth.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.cloth.config.DatetimeConverter;
import com.cloth.model.Admin;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class AdminDaoImpl implements AdminDao {
    @PersistenceContext
    private Session session;
    
    public Session getSession() {
        return this.session;
    }
    
    @Override
    public long count(JSONObject obj) {
        String username = obj.isNull("username") ? null : obj.getString("username");
        String authorities = obj.isNull("authority") ? null : obj.getString("authority");
        Date startDate = obj.isNull("startDate") ? null : DatetimeConverter.parse(obj.getString("startDate"), "yyyy-MM-dd");
        Date endDate = obj.isNull("endDate") ? null : DatetimeConverter.parse(obj.getString("endDate"), "yyyy-MM-dd");
        String createdBy = obj.isNull("createdBy") ? null : obj.getString("createdBy");
        String updatedBy = obj.isNull("updatedBy") ? null : obj.getString("updatedBy");

        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        // from
        Root<Admin> root = criteriaQuery.from(Admin.class);

        // select count(*)
        criteriaQuery.select(criteriaBuilder.count(root));

        // where
        List<Predicate> predicates = new ArrayList<>();
        if (username != null && !username.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
        }
        if (authorities != null) {
            predicates.add(criteriaBuilder.equal(root.get("authorities"), authorities));
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
    public List<Admin> find(JSONObject obj) {
        String username = obj.isNull("username") ? null : obj.getString("username");
        String authorities = obj.isNull("authority") ? null : obj.getString("authority");
        Date startDate = obj.isNull("startDate") ? null : DatetimeConverter.parse(obj.getString("startDate"), "yyyy-MM-dd");
        Date endDate = obj.isNull("endDate") ? null : DatetimeConverter.parse(obj.getString("endDate"), "yyyy-MM-dd");
        String createdBy = obj.isNull("createdBy") ? null : obj.getString("createdBy");
        String updatedBy = obj.isNull("updatedBy") ? null : obj.getString("updatedBy");

        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Admin> criteriaQuery = criteriaBuilder.createQuery(Admin.class);

        // from
        Root<Admin> root = criteriaQuery.from(Admin.class);

        // where
        List<Predicate> predicates = new ArrayList<>();
//        if (authorities != null) {
//            // predicates.add(criteriaBuilder.equal(root.get("authority"), authority));
//
//            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("authorities")), "%" + authorities.toLowerCase() + "%"));
//        }
        if (authorities != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + authorities.toLowerCase() + "%"));
        }

        if (username != null && !username.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("username"), username));

            // predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
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
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));

        TypedQuery<Admin> typedQuery = this.getSession().createQuery(criteriaQuery);

        List<Admin> activities = typedQuery.getResultList();
        return activities;
    }
}
