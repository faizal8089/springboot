package com.faizal.springboot.controllers;


import com.faizal.springboot.models.ProductItem;
import com.faizal.springboot.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductItem>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductItem> findById(@PathVariable Long id){
        ProductItem product = productService.getById(id);
        if(product == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(product);
    }
    @PostMapping
    public ResponseEntity<ProductItem> create(@RequestBody ProductItem productItem){
        return ResponseEntity.status(201).body(productService.create(productItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductItem> update(@PathVariable Long id, @RequestBody ProductItem productItem){
        ProductItem updated = productService.update(id, productItem);
        if(updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted = productService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

}
