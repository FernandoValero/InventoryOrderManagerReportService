package com.manager.order.inventory.reportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailDto {
    private Integer id;
    private int amount;
    private ProductDto product;

}
