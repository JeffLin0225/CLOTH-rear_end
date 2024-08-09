package com.cloth.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloth.Dao.ProductSpecifications;
import com.cloth.Repository.ProductRepository;
import com.cloth.model.Activity;
import com.cloth.model.Product;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	
	public List<Product> findClothing(String gender, String style, String type) {
        return productRepository.findAll(
            Specification.where(ProductSpecifications.hasGender(gender))
                         .and(ProductSpecifications.hasStyle(style))
                         .and(ProductSpecifications.hasType(type))
        );
    }

	public boolean existsByName(String name) {
		if (name != null && name.length() != 0) {
			return productRepository.countByName(name) != 0;
		}
		return false;
	}

	public Product modify(Integer id, String name, String description, Activity activity, Integer quantity, String size,
			Integer price, Integer discount_price, String color, String type, String gender,
			String style, String created_by, String updated_by, List<MultipartFile> files) {
		try {
			String status;
			Optional<Product> optional = productRepository.findById(id);
			if (optional.isPresent()) {
				Product update = optional.get();
				update.setName(name);
				update.setDescription(description);
				update.setActivity(activity);
				update.setQuantity(quantity);
				update.setSize(size);
				update.setPrice(price);
				update.setDiscount_price(discount_price);
				if (quantity > 0) {
					status = "有貨";
				} else {
					status = "缺貨";
				}
				update.setStatus(status);
				update.setColor(color);
				update.setType(type);
				update.setGender(gender);
				update.setStyle(style);
				update.setCreated_by(created_by);
				update.setUpdated_by(updated_by);
				update.setUpdated_at(new Date());
				if (files != null) {
					for (int i = 0; i < files.size(); i++) {
						MultipartFile file = files.get(i);
						switch (i) {
							case 0:
								if (file == null) {
									file = null;
									break;
								}
								update.setImg(file.getBytes());
								break;
							case 1:
								if (file == null) {
									file = null;
									break;
								}
								update.setImg2(file.getBytes());
								break;
							case 2:
								if (file == null) {
									file = null;
									break;
								}
								update.setImg3(file.getBytes());
								break;
							case 3:
								if (file == null) {
									file = null;
									break;
								}
								update.setImg4(file.getBytes());
								break;
							default:
								break;
						}
					}

				}
				return productRepository.save(update);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Product create(String name, String description, Activity activity, Integer quantity, String size,
			Integer price, Integer discount_price, String color, String type, String gender,
			String style, String created_by, List<MultipartFile> files) {
		try {
			String status;
			Product insert = new Product();
			insert.setName(name);
			insert.setDescription(description);
			insert.setActivity(activity);
			insert.setQuantity(quantity);
			insert.setSize(size);
			insert.setPrice(price);
			insert.setDiscount_price(discount_price);

			if (quantity > 0) {
				status = "有貨";
			} else {
				status = "缺貨";
			}
			insert.setStatus(status);
			insert.setColor(color);
			insert.setType(type);
			insert.setGender(gender);
			insert.setStyle(style);
			insert.setCreated_by(created_by);

			for (int i = 0; i < files.size(); i++) {
				MultipartFile file = files.get(i);
				switch (i) {
					case 0:
						insert.setImg(file.getBytes());
						break;
					case 1:
						insert.setImg2(file.getBytes());
						break;
					case 2:
						insert.setImg3(file.getBytes());
						break;
					case 3:
						insert.setImg4(file.getBytes());
						break;
					default:
						// 如果有超過4個文件，根據需求決定如何處理
						break;
				}
			}
			return productRepository.save(insert);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean remove(Integer id) {
		if (id != null) {
			Optional<Product> optional = productRepository.findById(id);
			if (optional.isPresent()) {
				productRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

	public boolean exists(Integer id) {
		if (id != null) {
			return productRepository.existsById(id);
		}
		return false;
	}

	public Product findById(Integer id) {
		if (id != null) {
			Optional<Product> optional = productRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return productRepository.count(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Product> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return productRepository.find(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}





}
