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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(columnDefinition = "VARCHAR(MAX)")
	private String description;

	@ManyToOne
	@JoinColumn(name = "activity_id")
	private Activity activity;

	private Integer quantity;

	private String size;

	private Integer price;

	private Integer discount_price;

	@Lob
	private byte[] img;
	@Lob
	private byte[] img2;
	@Lob
	private byte[] img3;
	@Lob
	private byte[] img4;

	private String status;

	private String color;

	private String type;

	private String gender;

	private String style;

	private String created_by;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

	private String updated_by;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<Comment> comment = new ArrayList<>();
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<Cart> cart = new ArrayList<>();

	@PrePersist
	public void oncreate() {
		if (created_at == null) {
			created_at = new Date();
		}
	}
}
