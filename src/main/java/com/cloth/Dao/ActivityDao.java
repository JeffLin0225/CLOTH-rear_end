package com.cloth.Dao;

import java.util.List;

import org.json.JSONObject;

import com.cloth.model.Activity;

public interface ActivityDao {
	public abstract List<Activity> find(JSONObject obj);
	public abstract long count(JSONObject obj);
}
