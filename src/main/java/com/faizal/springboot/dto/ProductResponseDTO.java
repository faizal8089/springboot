package com.faizal.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    public ProductResponseDTO(){}
}
