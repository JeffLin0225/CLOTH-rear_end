package com.cloth.Dao;

import java.util.List;

import org.json.JSONObject;

import com.cloth.model.Orders;

public interface OrdersDao {
	public abstract List<Orders> find(JSONObject obj);
	public abstract long count(JSONObject obj);
}
