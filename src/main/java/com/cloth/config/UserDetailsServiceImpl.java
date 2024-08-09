//package com.cloth.config;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Service;
//
//import com.cloth.Repository.AdminRepository;
//import com.cloth.model.Admin;
//
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String admin_name) throws UsernameNotFoundException {
//        Admin admin = adminRepository.findByAdmin_name(admin_name);
//        if (admin == null) {
//            throw new UsernameNotFoundException("User not found with username: " + admin_name);
//        }
//
//        // 單個角色的設置，不需要使用角色陣列
//        String role = admin.getAuthority();
//
//        return User.withUsername(admin.getAdmin_name())
//                   .password(admin.getAdmin_pwd())
//                   .roles(role)
//                   .build();
//    }
//}
