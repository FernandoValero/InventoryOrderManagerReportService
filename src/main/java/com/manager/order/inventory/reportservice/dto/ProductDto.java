package com.manager.order.inventory.reportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;

    private String number;
    private String name;
    private int stock;
    private String barCode;
    private Double price;
    private String description;
    private String category;
    private String image;
    private Integer userId;
    private Integer supplierId;
}