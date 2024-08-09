package com.cloth.Controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.Repository.OrdersRepository;
import com.cloth.config.DatetimeConverter;
import com.cloth.model.Orderdetail;
import com.cloth.model.Orders;
import com.cloth.service.OrdersService;

@RestController
@RequestMapping("/backstage/orders")
@CrossOrigin
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersRepository ordersRepository;

    @PutMapping("/modify/{id}")
    public String modify(@PathVariable Integer id) {
        JSONObject responseBody = new JSONObject();
        Orders order = ordersService.modify(id);
        if (order == null) {
            responseBody.put("success", false);
            responseBody.put("message", "出貨失敗");
        } else {
            responseBody.put("success", true);
            responseBody.put("message", "出貨成功");
        }
        return responseBody.toString();
    }

    @GetMapping("/{pk}")
    public String findById(@PathVariable(name = "pk") Integer id) {
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();

        Orders order = ordersService.findById(id);
        if (order != null) {
            JSONObject item = new JSONObject()
                    .put("id", order.getId())
                    .put("status", order.getStatus());
            array = array.put(item);
        }

        responseBody.put("list", array);// 記的.list[0]
        return responseBody.toString();
    }

    @PostMapping("/undonefind")
    public String newundonefind(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject(body);

        // 获取状态为“已出貨”的订单，并包含其订单明细
        List<Orders> ordersList = ordersRepository.find(obj);

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                String created_at = DatetimeConverter.toString(order.getCreated_at(), "yyyy年MM月dd日 HH時mm分ss秒");
                JSONArray detailsArray = new JSONArray();

                for (Orderdetail orderdetail : order.getOrderdetail()) {
                    JSONObject detailItem = new JSONObject()
                            .put("id", orderdetail.getId())
                            .put("name", orderdetail.getCart().getProduct().getName())
                            .put("quantity", orderdetail.getCart().getQuantity())
                            .put("color", orderdetail.getCart().getProduct().getColor())
                            .put("size", orderdetail.getCart().getProduct().getSize());
                    detailsArray.put(detailItem);
                }

                JSONObject orderItem = new JSONObject()
                        .put("orderid", order.getId())
                        .put("status", order.getStatus())
                        .put("created_at", created_at)
                        .put("userid", order.getUsers().getId())
                        .put("payment", order.getPayment())
                        .put("shipaddress", order.getShipaddress())
                        .put("shipment", order.getShipment())
                        .put("shipname", order.getShipname())
                        .put("shipphone", order.getShipphone())
                        .put("details", detailsArray);
                array.put(orderItem);
            }
        }
        long count = ordersService.count(body);
        responseBody.put("count", count);
        responseBody.put("list", array);

        return responseBody.toString();
    }

    @PostMapping("/donefind")
    public String newdonefind(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject(body);

        List<Orders> ordersList = ordersRepository.find(obj);

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                String shipping_at = DatetimeConverter.toString(order.getShipping_at(), "yyyy年MM月dd日 HH時mm分ss秒");
                JSONArray detailsArray = new JSONArray();

                for (Orderdetail orderdetail : order.getOrderdetail()) {
                    JSONObject detailItem = new JSONObject()
                            .put("id", orderdetail.getId())
                            .put("name", orderdetail.getCart().getProduct().getName())
                            .put("quantity", orderdetail.getCart().getQuantity())
                            .put("color", orderdetail.getCart().getProduct().getColor())
                            .put("size", orderdetail.getCart().getProduct().getSize());
                    detailsArray.put(detailItem);
                }
                JSONObject orderItem = new JSONObject()
                        .put("orderid", order.getId())
                        .put("status", order.getStatus())
                        .put("shipping_at", shipping_at)
                        .put("userid", order.getUsers().getId())
                        .put("payment", order.getPayment())
                        .put("shipaddress", order.getShipaddress())
                        .put("shipment", order.getShipment())
                        .put("shipname", order.getShipname())
                        .put("shipphone", order.getShipphone())
                        .put("details", detailsArray);
                array.put(orderItem);
            }
        }
        long count = ordersService.count(body);
        responseBody.put("count", count);
        responseBody.put("list", array);

        return responseBody.toString();
    }

}
