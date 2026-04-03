package com.faizal.springboot.dto;

public record AuthRequestDTO(
   String name,
   String email,
   String password
) {}
