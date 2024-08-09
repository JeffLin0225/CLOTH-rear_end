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

import com.cloth.Repository.ActivityRepository;
import com.cloth.config.DatetimeConverter;
import com.cloth.model.Activity;


@Service
@Transactional
public class ActivityService {

	
	@Autowired
	private ActivityRepository activityRepository;

	public boolean existsByName(String name) {
		if(name!=null && name.length()!=0) {
			return activityRepository.countByName(name)!=0;
		}
		return false;
	}

	public Activity modify(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			
			Integer id = obj.isNull("id") ? null : obj.getInt("id");
			String name = obj.isNull("name") ? null : obj.getString("name");			
			Integer discount_percent = obj.isNull("discount_percent") ? null : obj.getInt("discount_percent");			
			String start_at = obj.isNull("start_at") ? null : obj.getString("start_at");
			String end_at = obj.isNull("end_at") ? null : obj.getString("end_at");	
			String updated_by = obj.isNull("updated_by") ? null : obj.getString("updated_by");
		 
			Optional<Activity> optional = activityRepository.findById(id);
			if(optional.isPresent()) {
				Activity update = optional.get();
				
				update.setName(name);
				update.setDiscount_percent(discount_percent);
				update.setStart_at(DatetimeConverter.parse(start_at, "yyyy-MM-dd"));
				update.setEnd_at(DatetimeConverter.parse(end_at, "yyyy-MM-dd"));
				update.setUpdated_at(new Date());
				update.setUpdated_by(updated_by);

				return activityRepository.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Activity create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			
			
			String name = obj.isNull("name") ? null : obj.getString("name");			
			Integer discount_percent = obj.isNull("discount_percent") ? null : obj.getInt("discount_percent");			
			String start_at = obj.isNull("start_at") ? null : obj.getString("start_at");
			String end_at = obj.isNull("end_at") ? null : obj.getString("end_at");	
			String created_by = obj.isNull("created_by") ? null : obj.getString("created_by");
			
			
			
				Activity insert = new Activity();
				
				insert.setName(name);
				insert.setDiscount_percent(discount_percent);
				insert.setStart_at(DatetimeConverter.parse(start_at, "yyyy-MM-dd"));		
				insert.setEnd_at(DatetimeConverter.parse(end_at, "yyyy-MM-dd"));
				
				insert.setCreated_at(new Date());
				insert.setCreated_by(created_by);

				return activityRepository.save(insert);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean remove(Integer id) {
		if(id!=null) {
			Optional<Activity> optional = activityRepository.findById(id);
			if(optional.isPresent()) {
				activityRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}
	public boolean exists(Integer id) {
		if(id!=null) {
			return activityRepository.existsById(id);
		}
		return false;
	}
	public Activity findById(Integer id) {
		if(id!=null) {
			Optional<Activity> optional = activityRepository.findById(id);
			if(optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return activityRepository.count(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<Activity> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return activityRepository.find(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Object[]> findIdNameDis() {
		
			return activityRepository.findAllProjectedActivities();
		
		
	}

	public List<Activity> select(Activity bean) {
		List<Activity> result = null;
		if(bean!=null && bean.getId()!=null && !bean.getId().equals(0)) {
			Optional<Activity> optional = activityRepository.findById(bean.getId());
			if(optional.isPresent()) {
				result = new ArrayList<Activity>();
				result.add(optional.get());
			}
		} else {
			result = activityRepository.findAll();
		}
		return result;
	}
	public Activity insert(Activity bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<Activity> optional = activityRepository.findById(bean.getId());
			if(optional.isEmpty()) {
				return activityRepository.save(bean);
			}
		}
		return null;
	}
	public Activity update(Activity bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<Activity> optional = activityRepository.findById(bean.getId());
			if(optional.isPresent()) {
				return activityRepository.save(bean);
			}
		}
		return null;
	}
	public boolean delete(Activity bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<Activity> optional = activityRepository.findById(bean.getId());
			if(optional.isPresent()) {
				activityRepository.deleteById(bean.getId());
				return true;
			}
		}
		return false;
	}
}
