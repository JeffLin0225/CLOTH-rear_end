package com.cloth.Controller;


import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloth.config.DatetimeConverter;
import com.cloth.model.Activity;
import com.cloth.model.Product;
import com.cloth.service.ProductService;

@RestController
@RequestMapping("/backstage/product")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @GetMapping("/conditionalSearch")
    public String conditionalSearch(@RequestBody String body) {
    	JSONObject responseBody = new JSONObject();	
    	JSONObject obj = new JSONObject(body);
    	String gender =obj.isNull("gender") ? null : obj.getString("gender");
    	String style =obj.isNull("style") ? null : obj.getString("style");
    	String type =obj.isNull("type") ? null : obj.getString("type");	    
        
        JSONArray array = new JSONArray();
        List<Product> products = productService.findClothing(gender, style, type);
        if(products!=null && !products.isEmpty()) {
            for(Product product : products) {
            	 String created_at = DatetimeConverter.toString(product.getCreated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                 String updated_at = DatetimeConverter.toString(product.getUpdated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                JSONObject item = new JSONObject()
                		 .put("id", product.getId())
                         .put("name", product.getName())
                         .put("description", product.getDescription())
                         .put("activity", product.getActivity().getId())
                         .put("quantity", product.getQuantity())
                         .put("size", product.getSize())
                         .put("price", product.getPrice())
                         .put("discount_price", product.getDiscount_price())
                         .put("status", product.getStatus())
                         .put("color", product.getColor())
                         .put("type", product.getType())
                         .put("gender", product.getGender())
                         .put("style", product.getStyle())
                         .put("created_by", product.getCreated_by())
                         .put("created_at", created_at)
                         .put("updated_by", product.getUpdated_by())
                         .put("updated_at", updated_at)
                         ;
                 array = array.put(item);
            }
        }

        responseBody.put("list", array);

        return responseBody.toString();
    }
    
    
    @GetMapping("/name/{name}")
    public String existsByName(@PathVariable(name="name") String name) {
        JSONObject responseBody = new JSONObject();
        
        if(name==null || name.length()==0) {
            responseBody.put("success", false);
            responseBody.put("message", "Name是必要欄位");
        } else {
            boolean exists = productService.existsByName(name);
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
            if(!productService.exists(id)) {
                responseBody.put("success", false);
                responseBody.put("message", "Id不存在");
            } else {
                if(!productService.remove(id)) {
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

    @PutMapping("/modify/{id}")
    public String modify(@PathVariable Integer id, 
    		@RequestParam("name") String name,
    		@RequestParam("description") String description,
    		@RequestParam("activity") Activity activity,
    		@RequestParam("quantity") Integer quantity,
    		@RequestParam("size") String size,
    		@RequestParam("price") Integer price,
    		@RequestParam("discount_price") Integer discount_price,
    		@RequestParam("color") String color,
    		@RequestParam("type") String type,
    		@RequestParam("gender") String gender,
    		@RequestParam("style") String style,
    		@RequestParam("created_by") String created_by,
    		@RequestParam("updated_by") String updated_by,
            @RequestParam(value = "files", required = false)List<MultipartFile> files) {
        		JSONObject responseBody = new JSONObject();
        		if(activity==null) {
        			activity=null;
        		}
            	Product product = productService.modify(id,name,description,activity,quantity,size,price,
                		discount_price,color,type,gender,style,created_by,updated_by,files);
            	    
                if(product==null) {
                    responseBody.put("success", false);
                    responseBody.put("message", "修改失敗");
                } else {
                    responseBody.put("success", true);
                    responseBody.put("message", "修改成功");
                } 
        return responseBody.toString();
    }

    @PostMapping("/create")
    public String create(
    		@RequestParam("name") String name,
    		@RequestParam("description") String description,
    		@RequestParam(value = "activity", required = false) Activity activity,
    		@RequestParam("quantity") Integer quantity,
    		@RequestParam("size") String size,
    		@RequestParam("price") Integer price,
    		@RequestParam(value = "discount_price", required = false) Integer discount_price,
    		@RequestParam("color") String color,
    		@RequestParam("type") String type,
    		@RequestParam("gender") String gender,
    		@RequestParam("style") String style,
    		@RequestParam("created_by") String created_by,
            @RequestParam(value = "filess", required = false)List<MultipartFile> files) {
		JSONObject responseBody = new JSONObject();
		
        Product product = productService.create(name,description,activity,quantity,size,price,
        		discount_price,color,type,gender,style,created_by,files);
        if(product==null) {
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

        Product product = productService.findById(id);
        if(product!=null) {
            String created_at = DatetimeConverter.toString(product.getCreated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
            String updated_at = DatetimeConverter.toString(product.getUpdated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
            JSONObject item = new JSONObject()
                    .put("id", product.getId())
                    .put("name", product.getName())
                    .put("description", product.getDescription())
                    .put("activity", product.getActivity())
                    .put("quantity", product.getQuantity())
                    .put("size", product.getSize())
                    .put("price", product.getPrice())
                    .put("discount_price", product.getDiscount_price())
                    .put("img", product.getImg())
                    .put("img2", product.getImg2())
                    .put("img3", product.getImg3())
                    .put("img4", product.getImg4())
                    .put("status", product.getStatus())
                    .put("color", product.getColor())
                    .put("type", product.getType())
                    .put("gender", product.getGender())
                    .put("style", product.getStyle())
                    .put("created_by", product.getCreated_by())
                    .put("created_at", created_at)
                    .put("updated_by", product.getUpdated_by())
                    .put("updated_at", updated_at)
                    ;
            array = array.put(item);
        }

        responseBody.put("list", array);
        return responseBody.toString();
    }

    @PostMapping("/find")
    public String find(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();

        JSONArray array = new JSONArray();
        List<Product> products = productService.find(body);
        if(products!=null && !products.isEmpty()) {
            for(Product product : products) {
            	 String created_at = DatetimeConverter.toString(product.getCreated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                 String updated_at = DatetimeConverter.toString(product.getUpdated_at(), "yyyy-MM-dd HH:mm:ss EEEE");
                JSONObject item = new JSONObject()
                		 .put("id", product.getId())
                         .put("name", product.getName())
                         .put("description", product.getDescription())
                         .put("activity", product.getActivity().getId())
                         .put("quantity", product.getQuantity())
                         .put("size", product.getSize())
                         .put("price", product.getPrice())
                         .put("discount_price", product.getDiscount_price())
                         .put("status", product.getStatus())
                         .put("color", product.getColor())
                         .put("type", product.getType())
                         .put("gender", product.getGender())
                         .put("style", product.getStyle())
                         .put("created_by", product.getCreated_by())
                         .put("created_at", created_at)
                         .put("updated_by", product.getUpdated_by())
                         .put("updated_at", updated_at)
                         ;
                 array = array.put(item);
            }
        }

        long count = productService.count(body);
        responseBody.put("count", count);
        responseBody.put("list", array);

        return responseBody.toString();
    }
    
	private byte[] photo1 = null;
    private byte[] photo2 = null;
    private byte[] photo3 = null;
    private byte[] photo4 = null;   
    @GetMapping(path = "/detail1/{photoid}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public @ResponseBody byte[] findPhotoByPhotoId(@PathVariable(name = "photoid") Integer id) {
        Product detail = productService.findById(id);
        byte[] result = this.photo1;
        if (detail != null) {
            result = detail.getImg();
        }
        return result;
    }
    
    @GetMapping(path = "/detail2/{photoid}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public @ResponseBody byte[] findPhotoByPhotoId2(@PathVariable(name = "photoid") Integer id) {
        Product detail = productService.findById(id);
        byte[] result = this.photo2;
        if (detail != null) {
            result = detail.getImg2();
        }
        return result;
    }
    @GetMapping(path = "/detail3/{photoid}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public @ResponseBody byte[] findPhotoByPhotoId3(@PathVariable(name = "photoid") Integer id) {
        Product detail = productService.findById(id);
        byte[] result = this.photo3;
        if (detail != null) {
            result = detail.getImg3();
        }
        return result;
    }
    @GetMapping(path = "/detail4/{photoid}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public @ResponseBody byte[] findPhotoByPhotoId4(@PathVariable(name = "photoid") Integer id) {
        Product detail = productService.findById(id);
        byte[] result = this.photo4;
        if (detail != null) {
            result = detail.getImg4();
        }
        return result;
    }
    
}
