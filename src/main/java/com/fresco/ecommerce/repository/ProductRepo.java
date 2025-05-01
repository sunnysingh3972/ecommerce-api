package com.fresco.ecommerce.repository;

import com.fresco.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Import required annotations to make use of the Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
	List<Product> findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(String productName,
			String categoryName);

	List<Product> findBySellerUserId(Integer sellerId);

	Optional<Product> findBySellerUserIdAndProductId(Integer sellerId, Integer productId);

    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}

