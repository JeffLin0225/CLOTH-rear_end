package com.cloth.Dao;

import java.util.List;

import org.json.JSONObject;

import com.cloth.model.Customerservice;

public interface CustomerserviceDao {
	public abstract List<Customerservice> find(JSONObject obj);
//	public abstract long count(JSONObject obj);
}
