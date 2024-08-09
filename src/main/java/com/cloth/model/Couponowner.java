package com.cloth.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "couponowner")
public class Couponowner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private Users users;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id")
	private Coupon coupon;
	
	
	private Integer status;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	@Temporal(TemporalType.TIMESTAMP) 
	private Date start_at;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	@Temporal(TemporalType.TIMESTAMP) 
	private Date end_at;
	
	@OneToMany(mappedBy = "couponowner", fetch = FetchType.LAZY)
	private List<Orders> orders = new ArrayList<>();
	
	
}
