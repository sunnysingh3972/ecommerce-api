package com.fresco.ecommerce.controller;

import com.fresco.ecommerce.models.Category;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.UserInfo;
import com.fresco.ecommerce.repository.CategoryRepo;
import com.fresco.ecommerce.repository.ProductRepo;
import com.fresco.ecommerce.repository.UserInfoRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/seller")
public class SellerController {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private UserInfoRepository userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@PostMapping("/product")
public ResponseEntity<Object> postProduct(HttpServletRequest request, Principal principal, @RequestBody Product product) {
    Optional<UserInfo> seller = userRepo.findByUsername(principal.getName());
    if (seller.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Attach seller to product
    product.setSeller(seller.get());

    // Validate and attach Category
    // if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
    //     Optional<Category> categoryOpt = categoryRepo.findById(product.getCategory().getCategoryId());
    //     if (categoryOpt.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Category ID");
    //     }
    //     product.setCategory(categoryOpt.get());
    // } else {
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category is required");
    // }

	Optional<Category> category=categoryRepo.findByCategoryName(product.getCategory().getCategoryName());
	if(!category.isPresent()){
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	product.setCategory(category.get());
    // Save Product
    Product savedProduct = productRepo.save(product);

    // Build Absolute URI
    String baseUrl = request.getScheme() + "://" + request.getServerName()
            + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
    URI location = URI.create(baseUrl + "/api/auth/seller/product/" + savedProduct.getProductId());

    return ResponseEntity.created(location).build();
}


	@GetMapping("/product")
	public ResponseEntity<Object> getAllProducts(Principal principal) {
		Optional<UserInfo> seller = userRepo.findByUsername(principal.getName());
		if (seller.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		List<Product> products = productRepo.findBySellerUserId(seller.get().getUserId());
		return ResponseEntity.ok(products);
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<Object> getProduct(Principal principal, @PathVariable Integer productId) {
		Optional<UserInfo> seller = userRepo.findByUsername(principal.getName());
		if (seller.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		Optional<Product> productOpt = productRepo.findById(productId);
		if (productOpt.isEmpty()) return ResponseEntity.notFound().build();

		Product product = productOpt.get();
		if (product.getSeller().getUserId() != seller.get().getUserId()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(product);
	}

	@PutMapping("/product")
	public ResponseEntity<Object> putProduct(Principal principal, @RequestBody Product updatedProduct) {
		Optional<UserInfo> seller = userRepo.findByUsername(principal.getName());
		if (seller.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		Optional<Product> productOpt = productRepo.findById(updatedProduct.getProductId());
		if (productOpt.isEmpty()) return ResponseEntity.notFound().build();

		Product existing = productOpt.get();
		if (existing.getSeller().getUserId() != seller.get().getUserId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		// Update fields
		existing.setProductName(updatedProduct.getProductName());
		existing.setPrice(updatedProduct.getPrice());
		if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getCategoryId() != null) {
			Optional<Category> category = categoryRepo.findById(updatedProduct.getCategory().getCategoryId());
			category.ifPresent(existing::setCategory);
		}

		productRepo.save(existing);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<Product> deleteProduct(Principal principal, @PathVariable Integer productId) {
		Optional<UserInfo> seller = userRepo.findByUsername(principal.getName());
		if (seller.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		Optional<Product> productOpt = productRepo.findById(productId);
		if (productOpt.isEmpty()) return ResponseEntity.notFound().build();

		Product existing = productOpt.get();
		if (existing.getSeller().getUserId() != seller.get().getUserId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		productRepo.delete(existing);
		return ResponseEntity.ok().build();
	}
}
