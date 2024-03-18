package com.example.demo.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicesDto {
    private Long id;
    private String name;
    private String language;
}
