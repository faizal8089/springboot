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


public record ProductRequestDTO(
        String name, String description, double price
) {}