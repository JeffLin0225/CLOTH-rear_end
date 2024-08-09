package com.cloth.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.cloth.config.DatetimeConverter;
import com.cloth.model.Customerservice;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CustomerserviceDaoImpl implements CustomerserviceDao {
    @PersistenceContext
    private Session session;
    
    public Session getSession() {
        return this.session;
    }

    @Override
    public List<Customerservice> find(JSONObject obj) {
        Integer userid = obj.isNull("userid") ? null : obj.getInt("userid");
        Date startDate = obj.isNull("startDate") ? null : DatetimeConverter.parse(obj.getString("startDate"), "yyyy-MM-dd");
        Date endDate = obj.isNull("endDate") ? null : DatetimeConverter.parse(obj.getString("endDate"), "yyyy-MM-dd");
        String createdBy = obj.isNull("createdBy") ? null : obj.getString("createdBy");
        String updatedBy = obj.isNull("updatedBy") ? null : obj.getString("updatedBy");
        
		int start = obj.isNull("start") ? 0 : obj.getInt("start");
		int max = obj.isNull("max") ? 5 : obj.getInt("max");
		// boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
		// String order = obj.isNull("order") ? "id" : obj.getString("order");

        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Customerservice> criteriaQuery = criteriaBuilder.createQuery(Customerservice.class);

        // from
        Root<Customerservice> root = criteriaQuery.from(Customerservice.class);

        // where
        List<Predicate> predicates = new ArrayList<>();
        if (userid != null) {
//            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
          predicates.add(criteriaBuilder.equal(root.get("users").get("id"), userid));

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
        
        
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("send_at")));
//           if(dir) { 
//		} else {
//	        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(order)));
//		}

        TypedQuery<Customerservice> typedQuery = this.getSession().createQuery(criteriaQuery)      
				.setFirstResult(start)
				.setMaxResults(max);
        
        List<Customerservice> activities = typedQuery.getResultList();
        return activities;
    }
}
