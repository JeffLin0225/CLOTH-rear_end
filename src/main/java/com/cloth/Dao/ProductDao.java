package com.cloth.Dao;

import java.util.List;

import org.json.JSONObject;

import com.cloth.model.Product;

public interface ProductDao {
	public abstract List<Product> find(JSONObject obj);
	public abstract long count(JSONObject obj);
}
