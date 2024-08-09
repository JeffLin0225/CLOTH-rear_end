package com.cloth.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.cloth.config.DatetimeConverter;
import com.cloth.model.Activity;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class ActivityDaoImpl implements ActivityDao {
    @PersistenceContext
    private Session session;
    
    public Session getSession() {
        return this.session;
    }
    
    @Override
    public long count(JSONObject obj) {
        String name = obj.isNull("name") ? null : obj.getString("name");
        Double discountPercent = obj.isNull("discountPercent") ? null : obj.getDouble("discountPercent");
        Date startDate = obj.isNull("startDate") ? null : DatetimeConverter.parse(obj.getString("startDate"), "yyyy-MM-dd");
        Date endDate = obj.isNull("endDate") ? null : DatetimeConverter.parse(obj.getString("endDate"), "yyyy-MM-dd");
        String createdBy = obj.isNull("createdBy") ? null : obj.getString("createdBy");
        String updatedBy = obj.isNull("updatedBy") ? null : obj.getString("updatedBy");

        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        // from
        Root<Activity> root = criteriaQuery.from(Activity.class);

        // select count(*)
        criteriaQuery.select(criteriaBuilder.count(root));

        // where
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (discountPercent != null) {
            predicates.add(criteriaBuilder.equal(root.get("discount_percent"), discountPercent));
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
    public List<Activity> find(JSONObject obj) {
        String name = obj.isNull("name") ? null : obj.getString("name");
        Double discountPercent = obj.isNull("discountPercent") ? null : obj.getDouble("discountPercent");
        Date startDate = obj.isNull("startDate") ? null : DatetimeConverter.parse(obj.getString("startDate"), "yyyy-MM-dd");
        Date endDate = obj.isNull("endDate") ? null : DatetimeConverter.parse(obj.getString("endDate"), "yyyy-MM-dd");
        String createdBy = obj.isNull("createdBy") ? null : obj.getString("createdBy");
        String updatedBy = obj.isNull("updatedBy") ? null : obj.getString("updatedBy");

        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Activity> criteriaQuery = criteriaBuilder.createQuery(Activity.class);

        // from
        Root<Activity> root = criteriaQuery.from(Activity.class);

        // where
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (discountPercent != null) {
            predicates.add(criteriaBuilder.equal(root.get("discount_percent"), discountPercent));
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

        TypedQuery<Activity> typedQuery = this.getSession().createQuery(criteriaQuery);

        List<Activity> activities = typedQuery.getResultList();
        return activities;
    }
}
