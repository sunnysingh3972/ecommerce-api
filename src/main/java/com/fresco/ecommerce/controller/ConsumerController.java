package com.fresco.ecommerce.controller;

import com.fresco.ecommerce.models.Cart;
import com.fresco.ecommerce.models.CartProduct;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.UserInfo;
import com.fresco.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/consumer")
public class ConsumerController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartProductRepo cpRepo;

    @Autowired
    private UserInfoRepository userRepo;

    @GetMapping("/cart")
    public ResponseEntity<Object> getCart(Principal principal) {
        Optional<UserInfo> user = userRepo.findByUsername(principal.getName());
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Cart> cart = cartRepo.findByUserUsername(principal.getName());
        if (cart.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(cart.get());
    }

    @PostMapping("/cart")
    public ResponseEntity<Object> addProductToCart(Principal principal, @RequestBody Product productRequest) {
        Optional<UserInfo> user = userRepo.findByUsername(principal.getName());
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Cart> cartOpt = cartRepo.findByUserUsername(principal.getName());
        if (cartOpt.isEmpty()) return ResponseEntity.notFound().build();
        Cart cart = cartOpt.get();

        Optional<Product> productOpt = productRepo.findById(productRequest.getProductId());
        if (productOpt.isEmpty()) return ResponseEntity.notFound().build();
        Product product = productOpt.get();

        Optional<CartProduct> cpOpt = cpRepo.findByCartUserUserIdAndProductProductId(user.get().getUserId(), product.getProductId());
        if (cpOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        CartProduct cp = new CartProduct();
        cp.setCart(cart);
        cp.setProduct(product);
        cp.setQuantity(1);

        cpRepo.save(cp);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/cart")
    public ResponseEntity<Object> updateProductQuantity(Principal principal, @RequestBody CartProduct cpRequest) {
        Optional<UserInfo> user = userRepo.findByUsername(principal.getName());
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Cart> cartOpt = cartRepo.findByUserUsername(principal.getName());
        if (cartOpt.isEmpty()) return ResponseEntity.notFound().build();
        Cart cart = cartOpt.get();

        Optional<CartProduct> cpOpt = cpRepo.findByCartUserUserIdAndProductProductId(user.get().getUserId(), cpRequest.getProduct().getProductId());
        CartProduct cp;

        if (cpOpt.isPresent()) {
            cp = cpOpt.get();
        } else {
            Optional<Product> productOpt = productRepo.findById(cpRequest.getProduct().getProductId());
            if (productOpt.isEmpty()) return ResponseEntity.notFound().build();
            cp = new CartProduct();
            cp.setCart(cart);
            cp.setProduct(productOpt.get());
        }

        int quantity = cpRequest.getQuantity();
        if (quantity == 0) {
            cpRepo.delete(cp);
        } else {
            cp.setQuantity(quantity);
            cpRepo.save(cp);
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Object> removeProductFromCart(Principal principal, @RequestBody Product productRequest) {
        Optional<UserInfo> user = userRepo.findByUsername(principal.getName());
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<CartProduct> cpOpt = cpRepo.findByCartUserUserIdAndProductProductId(user.get().getUserId(), productRequest.getProductId());
        if (cpOpt.isPresent()) {
            cpRepo.delete(cpOpt.get());
        }

        return ResponseEntity.ok().build();
    }
}
