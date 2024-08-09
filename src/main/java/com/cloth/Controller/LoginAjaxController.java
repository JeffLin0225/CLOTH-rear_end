//package com.cloth.Controller;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cloth.Util.DatetimeConverter;
//import com.cloth.Util.JsonWebTokenUtility;
//import com.cloth.model.Admin;
//
//import jakarta.servlet.http.HttpSession;
//
//
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@RestController
//@RequestMapping("/backstage/secure")
//@CrossOrigin
//public class LoginAjaxController {
//    @Autowired
//    private AdminService adminService;
//    @Autowired
//    private JsonWebTokenUtility jsonWebTokenUtility;
//
//    @PostMapping("/login")
//    public String login(@RequestBody String body, HttpSession session) {
//        JSONObject responseBody = new JSONObject();
//        // 接收資料
//        JSONObject obj = new JSONObject(body);
//        String username = obj.isNull("name") ? null : obj.getString("name");
//        String password = obj.isNull("password") ? null : obj.getString("password");
//
//        // 驗證資料
//        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
//            responseBody.put("success", false);
//            responseBody.put("message", "請輸入帳號以及密碼");
//            return responseBody.toString();
//        }
//
//        // 呼叫model
//        Admin bean = adminService.login(username, password);
//
//        // 根據model執行結果，導向view
//        if (bean == null) {
//            responseBody.put("success", false);
//            responseBody.put("message", "登入失敗");
//        } else {
//            responseBody.put("success", true);
//            responseBody.put("message", "登入成功");
//            session.setAttribute("user", bean);
//
////          String birth = DatetimeConverter.toString(bean.getBirth(), "yyyy-MM-dd");
//            JSONObject user = new JSONObject()
//                      .put("id", bean.getId());
////                    .put("email", bean.getEmail())
////                    .put("birth", birth);
//            String token = jsonWebTokenUtility.createEncryptedToken(user.toString(), null);
//            responseBody.put("token", token);
//            responseBody.put("user", bean.getId());
//        }
//        return responseBody.toString();
//    }
//
//}
