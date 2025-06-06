package com.manager.order.inventory.reportservice.feign;

import com.manager.order.inventory.reportservice.dto.SalesResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/between")
    SalesResponseDto getSalesBetweenDates(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    );

    @GetMapping("/sales/client/{clientId}")
    SalesResponseDto getSalesByClientId(@PathVariable("clientId") Integer id);

    @GetMapping("/sales/user/{userId}")
    SalesResponseDto getSalesByUserId(@PathVariable("userId") Integer id);

    @GetMapping("/sales/month")
    SalesResponseDto getSalesByMonth(@RequestParam("month") int month);

    @GetMapping("/sales/year")
    SalesResponseDto getSalesByYear(@RequestParam("year") int year);

    @GetMapping("/sales/product/{productId}")
    SalesResponseDto getSalesByProductId(@PathVariable("productId") Integer id);

    @GetMapping("/sales/{id}")
    SalesResponseDto getSalesById(@PathVariable("id") Integer id);
}
