package com.faizal.springboot.services;


import com.faizal.springboot.dto.ProductRequestDTO;
import com.faizal.springboot.dto.ProductResponseDTO;
import com.faizal.springboot.models.ProductItem;
import com.faizal.springboot.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService{

    private final ProductItemRepository productItemRepository;

    ProductService(ProductItemRepository productItemRepository){
        this.productItemRepository = productItemRepository;
    }

//    public List<ProductItem> getAll(){
//        return productItemRepository.findAll();
//    }
    public List<ProductResponseDTO> getAll(){
        return productItemRepository.findAll().stream().map(val -> toResponse(val)).toList();
    }

//    public ProductItem getById(Long id){
//        return productItemRepository.findById(id).orElse(null);
//    }
    public ProductResponseDTO getById(Long id){
        ProductItem productItem = productItemRepository.findById(id).orElse(null);
        if(productItem == null) return null;
        return toResponse(productItem);
    }
//    public ProductItem create(ProductItem productItem){
//        return productItemRepository.save(productItem);
//    }

    public ProductResponseDTO create(ProductRequestDTO request){
        ProductItem productItem = new ProductItem();
        productItem.setName(request.getName());
        productItem.setPrice(request.getPrice());
        productItem.setDescription(request.getDescription());
        return toResponse(productItemRepository.save(productItem));
    }

//    public ProductItem update(Long id, ProductItem productItem){
//        ProductItem exists = productItemRepository.findById(id).orElse(null);
//        if(exists == null){
//            return null;
//        }else {
//            exists.setName(productItem.getName());
//            exists.setDescription(productItem.getDescription());
//            exists.setPrice(productItem.getPrice());
//            return productItemRepository.save(exists);
//        }
//    }

    public ProductResponseDTO update(Long id, ProductRequestDTO request){
        ProductItem exists = productItemRepository.findById(id).orElse(null);
        if (exists == null) return null;
        exists.setName(request.getName());
        exists.setPrice(request.getPrice());
        exists.setDescription(request.getDescription());
        return toResponse(productItemRepository.save(exists));
    }

    public boolean delete(Long id){
        if (!productItemRepository.existsById(id)) return false;
        productItemRepository.deleteById(id);
        return true;
    }

    public ProductResponseDTO toResponse(ProductItem productItem){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(productItem.getId());
        productResponseDTO.setName(productItem.getName());
        productResponseDTO.setDescription(productItem.getDescription());
        productResponseDTO.setPrice(productItem.getPrice());
        return productResponseDTO;
    }
}
