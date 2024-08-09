package com.cloth.Dao;

import org.springframework.data.jpa.domain.Specification;

import com.cloth.model.Product;

public class ProductSpecifications {

	
	public static Specification<Product> hasGender(String gender) {
        return (root, query, builder) -> {
            if (gender == null || gender.isEmpty()) {
                return builder.conjunction(); // 无条件时返回 true
            }
            return builder.equal(root.get("gender"), gender);
        };
    }

    public static Specification<Product> hasStyle(String style) {
        return (root, query, builder) -> {
            if (style == null || style.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(root.get("style"), style);
        };
    }

    public static Specification<Product> hasType(String type) {
        return (root, query, builder) -> {
            if (type == null || type.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(root.get("type"), type);
        };
    }
}
