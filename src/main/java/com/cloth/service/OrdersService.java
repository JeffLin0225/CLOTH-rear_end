package com.cloth.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloth.Repository.OrdersRepository;

import com.cloth.model.Orders;

@Service
@Transactional
public class OrdersService {

	@Autowired
	private OrdersRepository ordersRepository;

	public Orders modify(Integer id) {
		try {

			Optional<Orders> optional = ordersRepository.findById(id);
			if (optional.isPresent()) {
				Orders update = optional.get();

				update.setStatus("已出貨");

				update.setShipping_at(new Date());

				return ordersRepository.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean remove(Integer id) {
		if (id != null) {
			Optional<Orders> optional = ordersRepository.findById(id);
			if (optional.isPresent()) {
				ordersRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

	public Orders findById(Integer id) {
		if (id != null) {
			Optional<Orders> optional = ordersRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return ordersRepository.count(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Orders> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return ordersRepository.find(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Orders> getAllOrders() {
		return ordersRepository.findAll();
	}

	public Orders getOrderById(Integer id) {
		return ordersRepository.findById(id).orElse(null);
	}

	public List<Orders> getOrdersByUserId(Integer userId) {
		return ordersRepository.findByUsersId(userId);
	}
}
