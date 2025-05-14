package com.manager.order.inventory.reportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Integer id;

    private String saleDate;
    private double totalPrice;
    private Integer userId;
    private Integer clientId;
    private List<SaleDetailDto> saleDetail;

}