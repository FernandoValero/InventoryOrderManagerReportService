package com.manager.order.inventory.reportservice.feign;

import com.manager.order.inventory.reportservice.dto.SalesResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/sales/between")
    SalesResponseDto getSalesBetweenDates(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    );

    @GetMapping("/api/v1/sales/client/{clientId}")
    SalesResponseDto getSalesByClientById(@PathVariable("clientId") Integer id);

    @GetMapping("api/v1/sales/user/{userId}")
    SalesResponseDto getSalesByUserId(@PathVariable("userId") Integer id);

    @GetMapping("/api/v1/sales/month")
    SalesResponseDto getSalesByMonth(int month);

    @GetMapping("/api/v1/sales/year")
    SalesResponseDto getSalesByYear(int year);

    @GetMapping("/api/v1/sales/product/{productId}")
    SalesResponseDto getProductById(@PathVariable("productId") Integer id);

    @GetMapping("/api/v1/sales/{id}")
    SalesResponseDto getSalesById(@PathVariable("id") Integer id);
}
