package com.cloth.Util;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cloth.Repository.AdminRepository;
import com.cloth.model.Admin;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	 private final AdminRepository adminRepository;

	    
	    public UserDetailsServiceImpl(AdminRepository adminRepository) {
	        this.adminRepository = adminRepository;
	    }

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Admin admin = adminRepository.findByUsername(username);
	        if (admin == null) {
	            System.out.println("沒有使用者");
	            throw new UsernameNotFoundException("User not found");
	        }
	        return new User(admin.getUsername(), admin.getPassword(),
	                Collections.singletonList(new SimpleGrantedAuthority(admin.getAuthorities())));
	    }
}
