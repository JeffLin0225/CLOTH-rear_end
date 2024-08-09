package com.cloth.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.Repository.AdminRepository;
import com.cloth.Util.JsonWebTokenUtility;
import com.cloth.model.Admin;
import com.cloth.service.AdminService;

@RestController
@RequestMapping("/backstage/secure")
@CrossOrigin
public class AdminController {
	 	@Autowired
	    private AdminRepository adminRepository;
	 
		@Autowired
		private AdminService adminService;

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JsonWebTokenUtility jwtUtility;

	    @PostMapping("/login")
	    public String login(@RequestBody String body) {
	        JSONObject responseBody = new JSONObject();
	        JSONObject obj = new JSONObject(body);
	        String username = obj.optString("username");
	        String password = obj.optString("password");
	        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
	            responseBody.put("success", false);
	            responseBody.put("message", "請輸入帳號以及密碼");
	            return responseBody.toString();
	        }
	        
	        Admin bean = adminService.login(username, password);
	        System.out.println("bean="+bean);
		     // 根據model執行結果，導向view
	        if (bean == null) {
	            responseBody.put("success", false);
	            responseBody.put("message", "登入失敗");
	        } else {
	            responseBody.put("success", true);
	            responseBody.put("message", "登入成功");
	        }
                 	
	        Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(username, password)
	        );
	        String authority = bean.getAuthorities();  // 假设 `getAuthorities` 返回一个字符串，实际中可能是集合

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String token = jwtUtility.createToken(username, authority,null);
	        System.out.println("權限有沒有拿到:::"+bean.getAuthorities());
	        
	        responseBody.put("id", bean.getId());
	        responseBody.put("token", token);
	        responseBody.put("username", bean.getUsername());
	        responseBody.put("authority", bean.getAuthorities());

	        return responseBody.toString();
	    }
	    
	    @GetMapping("/findAdminById/{id}")
	    public String findAdminById(@PathVariable Integer id ) {
	        JSONObject responseBody = new JSONObject();
//	        JSONObject obj = new JSONObject(body);
	    	JSONArray array = new JSONArray();
//	        Integer userid = obj.getInt("id");
	        Optional<Admin> admm = adminRepository.findById(id);
	        Admin adm= admm.get();
	        JSONObject item = new JSONObject()
	    			.put("id", adm.getId())
	    			.put("name", adm.getUsername())
	    			.put("password", adm.getPassword())
	    			.put("authority", adm.getAuthorities())
	    			.put("email", adm.getEmail())
	    			.put("birth", adm.getBirth())
	    			.put("address", adm.getAddress())
	    			.put("gender", adm.getGender())
	    			.put("phone", adm.getPhone())
	    			.put("created_at", adm.getCreated_at())
	    			.put("updated_at", adm.getUpdated_at())
	    			;
	        array = array.put(item);
	        responseBody.put("admin",array);
	        return responseBody.toString();
	    }
	    @GetMapping("/findAdminByName")
	    public String findAdminByName(@RequestBody String body) {
	        JSONObject responseBody = new JSONObject();
	        JSONObject obj = new JSONObject(body);
	    	JSONArray array = new JSONArray();
	        String username = obj.getString("username");
	       Admin adm = adminRepository.findByUsername(username);
	       JSONObject item = new JSONObject()
	    			.put("id", adm.getId())
	    			.put("name", adm.getUsername())
	    			.put("password", adm.getPassword())
	    			.put("authority", adm.getAuthorities())
	    			.put("email", adm.getEmail())
	    			.put("birth", adm.getBirth())
	    			.put("address", adm.getAddress())
	    			.put("gender", adm.getGender())
	    			.put("phone", adm.getPhone())
	    			.put("created_at", adm.getCreated_at())
	    			.put("updated_at", adm.getUpdated_at())
	    			;
	       	array = array.put(item);
	        responseBody.put("admin",array);
	        return responseBody.toString();
	    }
	    
	    @PostMapping("/findAll")
	    public String findAll(@RequestBody String body) {
	    	JSONObject responseBody = new JSONObject();
	    	JSONArray array = new JSONArray();
	       List<Admin> adms = adminService.find(body);
	       for(Admin adm :adms ) {
	    	   JSONObject item = new JSONObject()
	    			.put("id", adm.getId())
	    			.put("name", adm.getUsername())
	    			.put("password", adm.getPassword())
	    			.put("authority", adm.getAuthorities())
	    			.put("email", adm.getEmail())
	    			.put("birth", adm.getBirth())
	    			.put("address", adm.getAddress())
	    			.put("gender", adm.getGender())
	    			.put("phone", adm.getPhone())
	    			.put("created_at", adm.getCreated_at())
	    			.put("updated_at", adm.getUpdated_at())
	    			;	       
	       	     array = array.put(item);
	       }
	    	
	       long count = adminService.count(body);
        	responseBody.put("count", count);
	        responseBody.put("list",array);
	        return responseBody.toString();
	    }
	    @PutMapping("/modify")
	    public String modifyadmin(@RequestBody String body) {
	        JSONObject responseBody = new JSONObject();
	        JSONObject obj = new JSONObject(body);
	        Integer userid = obj.getInt("id");
	        Optional<Admin> admm = adminRepository.findById(userid);
	        Admin adm= admm.get();
	    	adm.setEmail(obj.getString("email"));		
	    	adm.setBirth(obj.getString("birth"));
	    	adm.setAddress(obj.getString("address"));
	    	adm.setGender(obj.getString("gender"));
	    	adm.setPhone(obj.getString("phone"));
	    	adm.setUpdated_at(new Date());
	    	adminRepository.save(adm);	
	       
	        responseBody.put("success",true);
            responseBody.put("message", "更新資料成功");

	        return responseBody.toString();
	    }
	    
	    
	    @GetMapping("/home")
	    public String home() {
	        SecurityContext context = SecurityContextHolder.getContext();
	        Authentication auth = context.getAuthentication();
	        Object principal = auth.getPrincipal();
	        
	        System.out.println("Principal: " + principal);
	        System.out.println("Authentication: " + auth);
	        if ("anonymousUser".equals(principal)) {
	            return "你尚未經過身份認證";
	        }
	        
	        UserDetails userDetails = (UserDetails) principal;
	        return 
	                "你好，"+userDetails.getUsername().toString()+"你的權限是："+
	                
	                userDetails.getAuthorities().toString();
	        
	    }
	    
	    @GetMapping("/test")
	    public String test() {
	        Admin admin = adminRepository.findByUsername("aaa");
	        System.out.println(admin.getId()+admin.getUsername()+admin.getPassword()+admin.getAuthorities());
	        return "可以";
	    }
	    
	    

}
	

