package com.faizal.springboot.dto;

//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class ProductRequestDTO{
//
//    private String name;
//    private String description;
//    private double price;
//    public ProductRequestDTO(){}
//}


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductRequestDTO(
        @NotBlank(message = "Name is mandatory") String name,@NotBlank(message = "Explain briefly") String description, @Min(value = 10, message = "minimum 10") double price
) {}