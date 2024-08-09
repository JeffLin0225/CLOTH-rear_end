package com.cloth.Controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.Repository.CouponRepository;
import com.cloth.config.DatetimeConverter;
import com.cloth.model.Coupon;
import com.cloth.service.CouponService;


@RestController
@RequestMapping("/backstage/coupon")
@CrossOrigin
public class CouponController {
    @Autowired
    private CouponService couponService; 
    @Autowired
    CouponRepository couponRepository;  

    @GetMapping("/name/{name}")
    public String existsByName(@PathVariable(name="name") String name) {
        JSONObject responseBody = new JSONObject();
        
        if(name==null || name.length()==0) {
            responseBody.put("success", false);
            responseBody.put("message", "Name是必要欄位");
        } else {
            boolean exists = couponService.existsByName(name);
            if(exists) {
                responseBody.put("success", false);
                responseBody.put("message", "Name已存在");
            } else {
                responseBody.put("success", true);
                responseBody.put("message", "Name OK、可以使用");
            }
        }
        return responseBody.toString();
    }

    @DeleteMapping("/delete/{id}")
    public String remove(@PathVariable Integer id) {
        JSONObject responseBody = new JSONObject();
        if(id==null) {
            responseBody.put("success", false);
            responseBody.put("message", "Id是必要欄位");
        } else {
            if(!couponService.exists(id)) {
                responseBody.put("success", false);
                responseBody.put("message", "Id不存在");
            } else {
                if(!couponService.remove(id)) {
                    responseBody.put("success", false);
                    responseBody.put("message", "刪除失敗");
                } else {
                    responseBody.put("success", true);
                    responseBody.put("message", "刪除成功");
                }
            }
        }
        return responseBody.toString();
    }

    @PutMapping("/{id}")
    public String modify(@PathVariable Integer id, 
    		@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        if(id==null) {
            responseBody.put("success", false);
            responseBody.put("message", "Id是必要欄位");
        } else {
            if(!couponService.exists(id)) {
                responseBody.put("success", false);
                responseBody.put("message", "Id不存在");
            } else {
            	Coupon coupon = couponService.modify(body);
                if(coupon==null) {
                    responseBody.put("success", false);
                    responseBody.put("message", "修改失敗");
                } else {
                    responseBody.put("success", true);
                    responseBody.put("message", "修改成功");
                }
            }
        }
        return responseBody.toString();
    }

    @PostMapping("/create")
    public String create(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        Coupon coupon = couponService.create(body);
                if(coupon==null) {
                    responseBody.put("success", false);
                    responseBody.put("message", "新增失敗");
                } else {
                    responseBody.put("success", true);
                    responseBody.put("message", "新增成功");
                }
            
        
        return responseBody.toString();
    }

    @GetMapping("/{pk}")
    public String findById(@PathVariable(name = "pk") Integer id) {
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();

        Coupon coupon = couponService.findById(id);
        if(coupon!=null) {
            String created_at = DatetimeConverter.toString(coupon.getCreated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
            String updated_at = DatetimeConverter.toString(coupon.getUpdated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
            JSONObject item = new JSONObject()
                    .put("id", coupon.getId())
                    .put("name", coupon.getName())
                    .put("description", coupon.getDescription())
                    .put("discount", coupon.getDiscount())
                    .put("start_at", coupon.getStart_at())
                    .put("end_at", coupon.getEnd_at())
                    .put("created_by", coupon.getCreated_by())
                    .put("created_at", created_at)
                    .put("updated_by", coupon.getUpdated_by())
                    .put("updated_at", updated_at)
                    ;
            array = array.put(item);
        }

        responseBody.put("list", array);
        return responseBody.toString();
    }
    
    @GetMapping("/findall")
    public String findall() {
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();
        List<Object[]> coupons = couponService.findIdNameDis();
	        if (coupons != null && !coupons.isEmpty()) {
	            for (Object[] obj : coupons) {
	                JSONObject item = new JSONObject()
	                        .put("id", obj[0])
	                        .put("name", obj[1])
	                        .put("description", obj[2])
	                		.put("discount", obj[3]);
	                array.put(item);
	            }
	        }
        responseBody.put("coupons", array);

        return responseBody.toString();
    }

    @PostMapping("/find")
    public String find(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();

        JSONArray array = new JSONArray();
        List<Coupon> coupons = couponService.find(body);
        if(coupons!=null && !coupons.isEmpty()) {
            for(Coupon coupon : coupons) {
            	 String created_at = DatetimeConverter.toString(coupon.getCreated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                 String updated_at = DatetimeConverter.toString(coupon.getUpdated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                JSONObject item = new JSONObject()
                		.put("id", coupon.getId())
                        .put("name", coupon.getName())
                        .put("description", coupon.getDescription())
                        .put("discount", coupon.getDiscount())
                        .put("start_at", coupon.getStart_at())
                        .put("end_at", coupon.getEnd_at())
                        .put("created_by", coupon.getCreated_by())
                        .put("created_at", created_at)
                        .put("updated_by", coupon.getUpdated_by())
                        .put("updated_at", updated_at)
                         ;
                 array = array.put(item);
            }
        }

        long count = couponService.count(body);
        responseBody.put("count", count);
        responseBody.put("list", array);

        return responseBody.toString();
    }
}
