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

import com.cloth.config.DatetimeConverter;
import com.cloth.model.Activity;
import com.cloth.service.ActivityService;


@RestController
@RequestMapping("/backstage/activity")
@CrossOrigin
public class ActivityController {
    @Autowired
    private ActivityService activityService; 

    @GetMapping("/name/{name}")
    public String existsByName(@PathVariable(name="name") String name) {
        JSONObject responseBody = new JSONObject();
        
        if(name==null || name.length()==0) {
            responseBody.put("success", false);
            responseBody.put("message", "Name是必要欄位");
        } else {
            boolean exists = activityService.existsByName(name);
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
            if(!activityService.exists(id)) {
                responseBody.put("success", false);
                responseBody.put("message", "Id不存在");
            } else {
                if(!activityService.remove(id)) {
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
            if(!activityService.exists(id)) {
                responseBody.put("success", false);
                responseBody.put("message", "Id不存在");
            } else {
            	Activity activity = activityService.modify(body);
                if(activity==null) {
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
            	Activity activity = activityService.create(body);
                if(activity==null) {
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

        Activity activity = activityService.findById(id);
        if(activity!=null) {
            String created_at = DatetimeConverter.toString(activity.getCreated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
            String updated_at = DatetimeConverter.toString(activity.getUpdated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
            JSONObject item = new JSONObject()
                    .put("id", activity.getId())
                    .put("name", activity.getName())
                    .put("discount_percent", activity.getDiscount_percent())
                    .put("start_at", activity.getStart_at())
                    .put("end_at", activity.getEnd_at())
  
                    .put("created_by", activity.getCreated_by())
                    .put("created_at", created_at)
                    .put("updated_by", activity.getUpdated_by())
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
        List<Object[]> activities = activityService.findIdNameDis();

        if (activities != null && !activities.isEmpty()) {
            for (Object[] obj : activities) {
                JSONObject item = new JSONObject()
                        .put("id", obj[0])
                        .put("name", obj[1])
                        .put("discount_percent", obj[2]);

                array.put(item);
            }
        }
        responseBody.put("activities", array);

        return responseBody.toString();
    }

    @PostMapping("/find")
    public String find(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();

        JSONArray array = new JSONArray();
        List<Activity> activitys = activityService.find(body);
        if(activitys!=null && !activitys.isEmpty()) {
            for(Activity activity : activitys) {
            	 String created_at = DatetimeConverter.toString(activity.getCreated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                 String updated_at = DatetimeConverter.toString(activity.getUpdated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                JSONObject item = new JSONObject()
                		.put("id", activity.getId())
                        .put("name", activity.getName())
                        .put("discount_percent", activity.getDiscount_percent())
                        .put("start_at", activity.getStart_at())
                        .put("end_at", activity.getEnd_at())
                        .put("created_by", activity.getCreated_by())
                        .put("created_at", created_at)
                        .put("updated_by", activity.getUpdated_by())
                        .put("updated_at", updated_at)
                         ;
                 array = array.put(item);
            }
        }

        long count = activityService.count(body);
        responseBody.put("count", count);
        responseBody.put("list", array);

        return responseBody.toString();
    }
}
