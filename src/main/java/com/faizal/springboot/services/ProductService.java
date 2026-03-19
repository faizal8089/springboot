package com.faizal.springboot.services;


import com.faizal.springboot.models.ProductItem;
import com.faizal.springboot.repository.ProductItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService{

    private final ProductItemRepository productItemRepository;

    ProductService(ProductItemRepository productItemRepository){
        this.productItemRepository = productItemRepository;
    }

    public List<ProductItem> getAll(){
        return productItemRepository.findAll();
    }

    public ProductItem getById(Long Id){
        return productItemRepository.findById(Id).orElse(null);
    }

    public ProductItem create(ProductItem productItem){
        return productItemRepository.save(productItem);
    }

    public ProductItem update(Long id, ProductItem productItem){
        ProductItem exists = productItemRepository.findById(id).orElse(null);
        if(exists == null){
            return null;
        }else {
            exists.setName(productItem.getName());
            exists.setDescription(productItem.getDescription());
            exists.setPrice(productItem.getPrice());
            return productItemRepository.save(exists);
        }
    }

    public boolean delete(Long id){
        if (!productItemRepository.existsById(id)) return false;
        productItemRepository.deleteById(id);
        return true;
    }




}
