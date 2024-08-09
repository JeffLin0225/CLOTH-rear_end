package com.cloth.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloth.Repository.AdminRepository;
import com.cloth.model.Admin;


@Service
@Transactional
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;

	public Admin login(String username, String password) {
					
			Admin bean=  adminRepository.findByUsername(username);	
			System.out.println("Service的bean:"+bean);
        	if(password!=null && password.length()!=0) {
        		byte[] pass = bean.getPassword().getBytes();
        		byte[] temp = password.getBytes();
        		if(Arrays.equals(pass, temp)) {
        			return bean;
        		}            		
        	}
        	return bean;
	}
	
	public Admin findadm(Integer id) {
		Optional<Admin> adm = adminRepository.findById(id);
		if(adm.isPresent()) {
			return adm.get();
		}
		return null;
	}

	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return adminRepository.count(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Admin> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return adminRepository.find(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
