package com.cloth.Controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/send/byAdmin/3")//收前端
    @SendTo("/topic/toUser/3")//寄給客人，客人收 // 預設先給2號
    public String sendFromAdminMessage( @Payload String message) throws Exception {
    	 // 处理逻辑，例如打印消息
//        System.out.println("Admin to User ID: " + userid);
        System.out.println("Message: " + message);
        // 返回的消息将自动发送到 "/topic/user/{userid}"
        return message;
    }
	
    @MessageMapping("/send/byUser") 	
    @SendTo("/topic/toAdmin")//寄給後臺，後台收 //表明自己是誰
    public String sendFormUserMessage(@Payload String message) throws Exception {
    	String[] parts = message.split(":", 2);
        String userid = parts[0];
        String userMessage = parts[1];
        System.out.println("User ID: " + userid);
        System.out.println("Message: " + userMessage);
        // 返回的消息将自动发送到 "/topic/admin"
//        message
        return message; 
    }
    
    
    @MessageMapping("/send/Coupon")
    @SendTo("/topic/toUser/Coupon")
    public Map<String, Object>  sendCoupon(@Payload Map<String, Object> message) throws Exception {	
    	   System.out.println("Message: " + message);  
    	    return message;
       }
    
    
}
