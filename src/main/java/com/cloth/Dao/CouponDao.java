package com.cloth.Dao;

import java.util.List;

import org.json.JSONObject;

import com.cloth.model.Coupon;

public interface CouponDao {
	public abstract List<Coupon> find(JSONObject obj);
	public abstract long count(JSONObject obj);
}
