package com.manager.order.inventory.reportservice.controller;

import com.manager.order.inventory.reportservice.exception.NotFoundException;
import com.manager.order.inventory.reportservice.exception.ReportGenerationException;
import com.manager.order.inventory.reportservice.service.export.util.ReportRequest;
import com.manager.order.inventory.reportservice.service.export.util.enums.ExportFormat;
import com.manager.order.inventory.reportservice.service.ReportService;
import com.manager.order.inventory.reportservice.service.export.util.enums.ReportType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

import static com.manager.order.inventory.reportservice.controller.util.MessageConstants.*;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Reports API")
public class ReportController {

    ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Generate sales report by user ID")
    @GetMapping("/sales/user/{format}/{userId}")
    public ResponseEntity<?> generateUserReport(
            @PathVariable String format,
            @PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();
        try{
            ReportRequest request = ReportRequest.builder()
                    .type(ReportType.USER)
                    .id(userId)
                    .format(format)
                    .build();

            return buildResponse(request);
        } catch (ReportGenerationException  e) {
            response.put(ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        } catch (NotFoundException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response);
        } catch (Exception e) {
            response.put(ERROR, "Error generating report.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    private ResponseEntity<byte[]> buildResponse(ReportRequest request) {
        byte[] reportContent = reportService.generateReport(request);
        ExportFormat exportFormat = ExportFormat.fromString(request.getFormat());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + generateFilename(request) + "\"")
                .contentType(MediaType.parseMediaType(exportFormat.getContentType()))
                .body(reportContent);
    }

    private String generateFilename(ReportRequest request) {
        String prefix = "sales_report_";
        return prefix + switch (request.getType()) {
            case USER -> "user_" + request.getId();
            case CLIENT -> "client_" + request.getId();
            case DATE_RANGE -> request.getStartDate() + "_to_" + request.getEndDate();
            case MONTH -> "month_" + request.getMonth();
            case YEAR -> "year_" + request.getYear();
            case PRODUCT -> "product_" + request.getProductId();
        } + "." + ExportFormat.fromString(request.getFormat()).getFileExtension();
    }
}
