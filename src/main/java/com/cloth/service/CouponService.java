package com.cloth.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloth.Repository.CouponRepository;
import com.cloth.config.DatetimeConverter;
import com.cloth.model.Coupon;


@Service
@Transactional
public class CouponService {

	
	@Autowired
	private CouponRepository couponRepository;

	public boolean existsByName(String name) {
		if(name!=null && name.length()!=0) {
			return couponRepository.countByName(name)!=0;
		}
		return false;
	}

	public Coupon modify(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			
			Integer id = obj.isNull("id") ? null : obj.getInt("id");
			String name = obj.isNull("name") ? null : obj.getString("name");			
			String description = obj.isNull("description") ? null : obj.getString("description");			
			Integer discount = obj.isNull("discount") ? null : obj.getInt("discount");
			String start_at = obj.isNull("start_at") ? null : obj.getString("start_at");	
			String end_at = obj.isNull("end_at") ? null : obj.getString("end_at");	
			String updated_by = obj.isNull("updated_by") ? null : obj.getString("updated_by");
		 
			Optional<Coupon> optional = couponRepository.findById(id);
			if(optional.isPresent()) {
				Coupon update = optional.get();
				
				update.setName(name);
				update.setDiscount(discount);
				update.setDescription(description);
				update.setStart_at(DatetimeConverter.parse(start_at, "yyyy-MM-dd"));
				update.setEnd_at(DatetimeConverter.parse(end_at, "yyyy-MM-dd"));
				update.setUpdated_at(new Date());
				update.setUpdated_by(updated_by);

				return couponRepository.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Coupon create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			
			
			String name = obj.isNull("name") ? null : obj.getString("name");			
			String description = obj.isNull("description") ? null : obj.getString("description");			
			Integer discount = obj.isNull("discount") ? null : obj.getInt("discount");
			String start_at = obj.isNull("start_at") ? null : obj.getString("start_at");	
			String end_at = obj.isNull("end_at") ? null : obj.getString("end_at");	
			String created_by = obj.isNull("created_by") ? null : obj.getString("created_by");
			
			
			
			Coupon insert = new Coupon();
				
				insert.setName(name);
				insert.setDescription(description);
				insert.setDiscount(discount);
				insert.setStart_at(DatetimeConverter.parse(start_at, "yyyy-MM-dd"));
				insert.setEnd_at(DatetimeConverter.parse(end_at, "yyyy-MM-dd"));
				insert.setCreated_at(new Date());
				insert.setCreated_by(created_by);

				return couponRepository.save(insert);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean remove(Integer id) {
		if(id!=null) {
			Optional<Coupon> optional = couponRepository.findById(id);
			if(optional.isPresent()) {
				couponRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}
	public boolean exists(Integer id) {
		if(id!=null) {
			return couponRepository.existsById(id);
		}
		return false;
	}
	public Coupon findById(Integer id) {
		if(id!=null) {
			Optional<Coupon> optional = couponRepository.findById(id);
			if(optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return couponRepository.count(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<Coupon> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return couponRepository.find(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
//	
	public List<Object[]> findIdNameDis() {
		
			return couponRepository.findAllProjectedCoupons();
		
		
	}

	public List<Coupon> select(Coupon bean) {
		List<Coupon> result = null;
		if(bean!=null && bean.getId()!=null && !bean.getId().equals(0)) {
			Optional<Coupon> optional = couponRepository.findById(bean.getId());
			if(optional.isPresent()) {
				result = new ArrayList<Coupon>();
				result.add(optional.get());
			}
		} else {
			result = couponRepository.findAll();
		}
		return result;
	}
	public Coupon insert(Coupon bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<Coupon> optional = couponRepository.findById(bean.getId());
			if(optional.isEmpty()) {
				return couponRepository.save(bean);
			}
		}
		return null;
	}
	public Coupon update(Coupon bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<Coupon> optional = couponRepository.findById(bean.getId());
			if(optional.isPresent()) {
				return couponRepository.save(bean);
			}
		}
		return null;
	}
	public boolean delete(Coupon bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<Coupon> optional = couponRepository.findById(bean.getId());
			if(optional.isPresent()) {
				couponRepository.deleteById(bean.getId());
				return true;
			}
		}
		return false;
	}
}
