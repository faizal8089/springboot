package com.faizal.springboot.controllers;


import com.faizal.springboot.dto.PriceDTO;
import com.faizal.springboot.dto.ProductRequestDTO;
import com.faizal.springboot.dto.ProductResponseDTO;
import com.faizal.springboot.models.ProductItem;
import com.faizal.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping({"/inventory", "/index", "/home"})
public class ProductController {

    private final ProductService productService;


    ProductController(ProductService productService){
        this.productService = productService;
    }

//    @GetMapping
//    public ResponseEntity<List<ProductItem>> getAll(){
//        return ResponseEntity.ok(productService.getAll());
//    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductItem> findById(@PathVariable Long id){
//        ProductItem product = productService.getById(id);
//        if(product == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(product);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id){
        ProductResponseDTO productItem = productService.getById(id);
        if (productItem == null) return null;
        return ResponseEntity.ok(productItem);
    }

//    @PostMapping
//    public ResponseEntity<ProductItem> create(@RequestBody ProductItem productItem){
//        return ResponseEntity.status(201).body(productService.create(productItem));
//    }
//
//    @PostMapping
//    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO request, BindingResult result){
//        System.out.println(Objects.requireNonNull(result.getFieldError("name")).getDefaultMessage());
//        return ResponseEntity.ok(productService.create(request));
//    }
@PostMapping
public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO request) {
    // 1. Check if ANY errors occurred
//    if (result.hasErrors()) {
//        // 2. Log the specific error safely
//        if (result.hasFieldErrors("name")) {
//            System.out.println("Validation Error: " + result.getFieldError("name").getDefaultMessage());
//        }
//        // 3. Return the errors to the user (Postman) so they know what happened
//        return ResponseEntity.badRequest().body(result.getAllErrors());
//    }
//
//    return ResponseEntity.ok(productService.create(request));
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.create(request));
}

//    @PutMapping("/{id}")
//    public ResponseEntity<ProductItem> update(@PathVariable Long id, @RequestBody ProductItem productItem){
//        ProductItem updated = productService.update(id, productItem);
//        if(updated == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(updated);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id,@Valid @RequestBody ProductRequestDTO request){
        ProductResponseDTO updated = productService.update(id, request);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id){
//        boolean deleted = productService.delete(id);
//        if (!deleted) return ResponseEntity.notFound().build();
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> delete(@PathVariable Long id){
        boolean deleted = productService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/custom/{id}")
    public ResponseEntity<PriceDTO> custom(@PathVariable Long id){
        return ResponseEntity.ok(productService.toPriceDTO(id));
    }

}
