package com.faizal.springboot.services;


import com.faizal.springboot.dto.PriceDTO;
import com.faizal.springboot.dto.ProductRequestDTO;
import com.faizal.springboot.dto.ProductResponseDTO;
import com.faizal.springboot.exception.ResourceNotFoundException;
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
        ProductItem productItem = productItemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No product found for id: "+ id));
        return toResponse(productItem);
    }
//    public ProductItem create(ProductItem productItem){
//        return productItemRepository.save(productItem);
//    }

    public ProductResponseDTO create(ProductRequestDTO request){
        ProductItem productItem = new ProductItem();
        productItem.setName(request.name());
        productItem.setPrice(request.price());
        productItem.setDescription(request.description());
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
        ProductItem exists = productItemRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Product not found with id: "+ id));

        exists.setName(request.name());
        exists.setPrice(request.price());
        exists.setDescription(request.description());
        return toResponse(productItemRepository.save(exists));
    }

    public boolean delete(Long id){
        if (!productItemRepository.existsById(id)) return false;
        productItemRepository.deleteById(id);
        return true;
    }


    public PriceDTO toPriceDTO(Long id){
        Double price = productItemRepository.findCustom(id);
        if (price == null){
            throw new ResourceNotFoundException("Product not found with id: "+ id);
        }
        return new PriceDTO(price);
    }

//    public ProductResponseDTO toResponse(ProductItem productItem){
//        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
//        productResponseDTO.setId(productItem.getId());
//        productResponseDTO.setName(productItem.getName());
//        productResponseDTO.setDescription(productItem.getDescription());
//        productResponseDTO.setPrice(productItem.getPrice());
//        return productResponseDTO;
//    }
    public ProductResponseDTO toResponse(ProductItem productItem){
        return new ProductResponseDTO(productItem.getId(), productItem.getName(), productItem.getDescription(), productItem.getPrice());
    }
}
