package com.manager.order.inventory.reportservice.service.impl;

import com.manager.order.inventory.reportservice.dto.SaleDto;
import com.manager.order.inventory.reportservice.dto.SalesResponseDto;
import com.manager.order.inventory.reportservice.feign.InventoryClient;
import com.manager.order.inventory.reportservice.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final InventoryClient inventoryClient;

    public ReportServiceImpl(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }
    @Override
    public List<SaleDto> getSalesByUserId(Integer userId) {
        SalesResponseDto response = inventoryClient.getSalesByUserId(userId);
        return response.getSales();

    }
}
