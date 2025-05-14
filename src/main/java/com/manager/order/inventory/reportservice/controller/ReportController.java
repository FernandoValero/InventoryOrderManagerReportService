package com.manager.order.inventory.reportservice.controller;

import com.manager.order.inventory.reportservice.dto.SaleDto;
import com.manager.order.inventory.reportservice.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/reports")
public class ReportController {

    ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales/by-user/{userId}")
    public ResponseEntity<Map<String, Object>> getSalesByUserId(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();
        try{
            List<SaleDto> sales = reportService.getSalesByUserId(userId);
            response.put("Reportes: ", sales);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch(Exception e){
            response.put("Message: ", "Error interno");
            response.put("Error: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
