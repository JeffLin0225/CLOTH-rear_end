package com.cloth.Dao;

import java.util.List;

import org.json.JSONObject;

import com.cloth.model.Admin;

public interface AdminDao {
	public abstract List<Admin> find(JSONObject obj);
	public abstract long count(JSONObject obj);
}
