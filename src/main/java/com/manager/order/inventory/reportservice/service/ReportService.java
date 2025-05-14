package com.manager.order.inventory.reportservice.service;

import com.manager.order.inventory.reportservice.dto.SaleDto;

import java.util.List;

public interface ReportService {

    List<SaleDto> getSalesByUserId(Integer userId);
}
