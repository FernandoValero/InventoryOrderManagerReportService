package com.manager.order.inventory.reportservice.service.impl;

import com.manager.order.inventory.reportservice.dto.SaleDto;
import com.manager.order.inventory.reportservice.dto.SalesResponseDto;
import com.manager.order.inventory.reportservice.exception.NotFoundException;
import com.manager.order.inventory.reportservice.service.export.impl.ExcelExporter;
import com.manager.order.inventory.reportservice.service.export.impl.PdfExporter;
import com.manager.order.inventory.reportservice.service.export.util.ReportRequest;
import com.manager.order.inventory.reportservice.service.export.util.enums.ExportFormat;
import com.manager.order.inventory.reportservice.exception.ReportGenerationException;
import com.manager.order.inventory.reportservice.feign.InventoryClient;
import com.manager.order.inventory.reportservice.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.rmi.server.ExportException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final PdfExporter pdfExporter;
    private final ExcelExporter excelExporter;
    private final InventoryClient inventoryClient;

    public ReportServiceImpl(PdfExporter pdfExporter,
                             ExcelExporter excelExporter,
                             InventoryClient inventoryClient) {
        this.pdfExporter = pdfExporter;
        this.excelExporter = excelExporter;
        this.inventoryClient = inventoryClient;
    }

    @Override
    public byte[] generateReport(ReportRequest request) {
        Objects.requireNonNull(request.getFormat(), "The format cannot be null");
        SalesResponseDto response = fetchSalesData(request);
        validateSalesResponse(response);

        ExportFormat exportFormat = ExportFormat.fromString(request.getFormat());
        String reportTitle = generateReportTitle(request);

        return exportReport(response.getSales(), reportTitle, exportFormat);

    }

    private SalesResponseDto fetchSalesData(ReportRequest request) {
        return switch (request.getType()) {
            case USER -> inventoryClient.getSalesByUserId(request.getId());
            case CLIENT -> inventoryClient.getSalesByClientId(request.getId());
            case DATE_RANGE -> inventoryClient.getSalesBetweenDates(request.getStartDate(), request.getEndDate());
            case MONTH -> inventoryClient.getSalesByMonth(request.getMonth());
            case YEAR -> inventoryClient.getSalesByYear(request.getYear());
            case PRODUCT -> inventoryClient.getSalesByProductId(request.getProductId());
        };
    }

    private String generateReportTitle(ReportRequest request) {
        return switch (request.getType()) {
            case USER -> String.format("Sales Report - User %d", request.getId());
            case CLIENT -> String.format("Sales Report - Client %d", request.getId());
            case DATE_RANGE -> String.format("Sales Report - %s to %s", request.getStartDate(), request.getEndDate());
            case MONTH -> String.format("Sales Report - Month %d", request.getMonth());
            case YEAR -> String.format("Sales Report - Year %d", request.getYear());
            case PRODUCT -> String.format("Sales Report - Product %d", request.getProductId());
        };
    }

    private byte[] exportReport(List<SaleDto> sales, String title, ExportFormat format) {
        try{

            return switch (format) {
                case PDF -> pdfExporter.export(sales, title);
                case EXCEL -> excelExporter.export(sales, title);
            };
        } catch (ExportException e) {
            throw new ReportGenerationException("Error generating report.");
        } catch (IllegalArgumentException e) {
            throw new ReportGenerationException(e.getMessage());
        }
    }

    private void validateSalesResponse(SalesResponseDto response) {
        if (response == null) {
            throw new ReportGenerationException("No response received from the inventory service");
        }
        if (response.getSales() == null || response.getSales().isEmpty()) {
            throw new NotFoundException("No sales found.");
        }
    }
}

