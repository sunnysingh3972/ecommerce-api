package com.fresco.ecommerce.controller;

import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/product/search")
    public List<Product> getProducts(@RequestParam String keyword) {
        return productRepo.findByProductNameContainingIgnoreCase(keyword);
    }
}
