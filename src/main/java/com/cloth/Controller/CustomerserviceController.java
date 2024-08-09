package com.cloth.Controller;


import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloth.Repository.CustomerserviceRepository;
import com.cloth.config.DatetimeConverter;
import com.cloth.model.Customerservice;
import com.cloth.model.Users;
import com.cloth.service.CustomerserviceService;

@RestController
@RequestMapping("/backstage/customerservice")
@CrossOrigin
public class CustomerserviceController {
	 	@Autowired
	    private CustomerserviceService customerserviceService;
	 	@Autowired
	    private CustomerserviceRepository customerserviceRepository;
	 		    
	    @PostMapping("/create")
	    public String create(
	    		@RequestParam(value = "chats") String chats,
	    		@RequestParam(value = "adminid", required = false) Integer adminid,
	    		@RequestParam(value = "usersid", required = false) Integer usersid,	
	    		@RequestParam(value = "files", required = false)List<MultipartFile> files
	    		) {
	        JSONObject responseBody = new JSONObject();	     	    
	        
	        if ((chats == null || chats.length() == 0) && files==null) {
	            responseBody.put("nogood", false);
	            responseBody.put("message", "沒有內容");
	            return responseBody.toString();
	        }
	        if(usersid == 0  ) {
	        	responseBody.put("nogood", false);
	            responseBody.put("message", "沒有選擇使用者");
	            return responseBody.toString();
	        }
	                     
	        Customerservice bean = customerserviceService.create(chats,adminid,usersid,files);
	        System.out.println("bean="+bean);
	        	        
	        return responseBody.toString();
	    }
	    
	    @PostMapping("/findAdmin")
	    public String findAdmin(@RequestBody String body) {
	    	JSONObject responseBody = new JSONObject();
	    	JSONArray array = new JSONArray();
	       List<Users> users = customerserviceRepository.findByAdmin();
	       for(Users user :users ) {
	    long unreadMessageCount = customerserviceRepository.countByUseridisAdminRead(user.getId());
	    	   JSONObject item = new JSONObject()
	    			   .put("userid", user.getId())	    				    				       
	       	     .put("unreadMessageCount", unreadMessageCount);
	      
	      array = array.put(item);
	       }	    	
	        
	        responseBody.put("list",array);
	        return responseBody.toString();
	    }
	    
	    @PostMapping("/findAdminMessage")
	    public String findAdminMessage(@RequestBody String body ) {
	    	JSONObject obj = new JSONObject(body);
	        JSONObject responseBody = new JSONObject();
	    	JSONArray array = new JSONArray();
	        List<Customerservice> messages = customerserviceRepository.find(obj);
	        for(Customerservice message :messages ) {
	        	message.setIsAdminRead(1);
	        	customerserviceRepository.save(message);
                String time = DatetimeConverter.toString(message.getSend_at(), "yyyy年MM月dd日 HH時mm分ss秒");
		    	boolean imgnull =false;
				if(message.getImg1()!=null){
					imgnull = true;
				}
				
				
				JSONObject item = new JSONObject()
				   		   .put("id", message.getId())
		    			   .put("chats", message.getChats())
		    			   .put("adminid", message.getAdmin())
		    			   .put("usersid", message.getUsers())
		    			   .put("imgnull", imgnull)
		    			   .put("time",time)
		    			   ;	    				    				       
		       	     array = array.put(item);
		       }	    		   
	        
	        Integer userid = obj.isNull("userid") ? null : obj.getInt("userid");

	         long count = customerserviceRepository.countByUserid(userid);
	         responseBody.put("count", count);
	         responseBody.put("list",array);
	        return responseBody.toString();
	    }
    
	private byte[] photos = null;
    @GetMapping(path = "/detail/{mesid}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public @ResponseBody byte[] findPhotoByPhotoId(@PathVariable(name = "mesid") Integer id) {
 		Optional<Customerservice>	op=	customerserviceRepository.findById(id);
        Customerservice detail = op.get();

      byte[] result = this.photos;
		
	  if (detail!= null) {
		result =  detail.getImg1();
		if(result==null){
			result=null;
		}
	  }       
	    return result;        
    }
	/////////////////////////////////////////////////////////////////////////////
    
    
    @PostMapping("/createUser")
    public String createUser(
    		@RequestParam(value = "chats") String chats,
    		@RequestParam(value = "usersid", required = false) Integer usersid,	
    		@RequestParam(value = "files", required = false)List<MultipartFile> files
    		) {
        JSONObject responseBody = new JSONObject();	     	    
        
        if ((chats == null || chats.length() == 0) && files==null) {
            responseBody.put("nogood", false);
            responseBody.put("message", "沒有內容");
            return responseBody.toString();
        }
        if(usersid == 0  ) {
        	responseBody.put("nogood", false);
            responseBody.put("message", "沒有選擇使用者");
            return responseBody.toString();
        }
                     
        Customerservice bean = customerserviceService.createbyUser(chats,usersid,files);
        System.out.println("bean="+bean);
        	        
        return responseBody.toString();
    }
    
    @PostMapping("/findHowManyUnread")
    public String findHowManyUnread(@RequestBody String body) {
    	JSONObject responseBody = new JSONObject();
 	    JSONObject obj = new JSONObject(body);
 		Integer userid = obj.getInt("userid");
    	
 		long unreadMessageCount = customerserviceRepository.countisUserUnread(userid);    	     			      				    				                             	
        responseBody.put("unreadMessageCount",unreadMessageCount);
        return responseBody.toString();
    }
    
    @PostMapping("/findUserMessage")
    public String findUserMessage(@RequestBody String body ) {
    	JSONObject obj = new JSONObject(body);
    	Integer userid = obj.getInt("userid");
        JSONObject responseBody = new JSONObject();
    	JSONArray array = new JSONArray();
        List<Customerservice> messages = customerserviceRepository.findALLmesssageByUserid(userid);
        for(Customerservice message :messages ) {
        	message.setIsUserRead(1);
        	customerserviceRepository.save(message);
            String time = DatetimeConverter.toString(message.getSend_at(), "yyyy年MM月dd日 HH時mm分ss秒");
	    	boolean imgnull =false;
			if(message.getImg1()!=null){
				imgnull = true;
			}
			
			JSONObject item = new JSONObject()
			   		   .put("id", message.getId())
	    			   .put("chats", message.getChats())
	    			   .put("usersid", message.getUsers())
	    			   .put("adminid",message.getAdmin())
	    			   .put("imgnull", imgnull)
	    			   .put("time",time)
	    			   ;	    				    				       
	       	     array = array.put(item);
	       }	    	
        
         responseBody.put("list",array);
        return responseBody.toString();
    }
//
//private byte[] photos = null;
//@GetMapping(path = "/detail/{mesid}", produces = { MediaType.IMAGE_JPEG_VALUE })
//public @ResponseBody byte[] findPhotoByPhotoId(@PathVariable(name = "mesid") Integer id) {
//		Optional<Customerservice>	op=	customerserviceRepository.findById(id);
//    Customerservice detail = op.get();
//
//  byte[] result = this.photos;
//	
//  if (detail!= null) {
//	result =  detail.getImg1();
//	if(result==null){
//		result=null;
//	}
//  }       
//    return result;        
//}

	    	     	   	   	  
}
	

