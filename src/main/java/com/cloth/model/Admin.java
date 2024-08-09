package com.cloth.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
@Table(name = "admin")
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="username" ,nullable=false , unique = true)
	private String username;
	
	@Column(name="password" ,nullable=false)
	private String password;
	
	@Column(name="authorities" ,nullable=false)
	private String authorities ; 
	
	private String gender;
	
	private String birth;
	
	private String email;
	
	private String phone;
	
	private String address;
		
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.DATE)
	private Date  created_at;
		
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.DATE)
	private Date updated_at;

	@OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
	private List<Reply> reply = new ArrayList<>();
	@OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
	private List<Customerservice> customerservice = new ArrayList<>();
	@PrePersist
	public void oncreate() {
		if (created_at == null) {
			created_at = new Date();
		}
	}
}
