package com.manager.order.inventory.reportservice.service.export.impl;

import com.manager.order.inventory.reportservice.dto.ProductDto;
import com.manager.order.inventory.reportservice.dto.SaleDetailDto;
import com.manager.order.inventory.reportservice.dto.SaleDto;
import com.manager.order.inventory.reportservice.service.export.ReportExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.List;

@Service
public class ExcelExporter implements ReportExporter<List<SaleDto>> {

    @Override
    public byte[] export(List<SaleDto> sales, String title) throws ExportException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Sales report");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            createHeaderRow(sheet, headerStyle);

            fillDataRows(sheet, sales, currencyStyle, dateStyle);

            autoSizeColumns(sheet);

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new ExportException("Error generating Excel file", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("dd/mm/yyyy"));
        return style;
    }

    private void createHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Sale ID", "Date", "Client ID",
                "Product", "Code", "Amount",
                "Unit Price", "Tota Product", "Total Sale"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillDataRows(Sheet sheet, List<SaleDto> sales, CellStyle currencyStyle, CellStyle dateStyle) {
        int rowNum = 1;

        for (SaleDto sale : sales) {
            for (SaleDetailDto detail : sale.getSaleDetail()) {
                Row row = sheet.createRow(rowNum++);

                createCell(row, 0, sale.getId(), null);
                createCell(row, 1, sale.getSaleDate(), dateStyle);
                createCell(row, 2, sale.getClientId(), null);

                ProductDto product = detail.getProduct();
                createCell(row, 3, product.getName(), null);
                createCell(row, 4, product.getNumber(), null);
                createCell(row, 5, detail.getAmount(), null);
                createCell(row, 6, product.getPrice(), currencyStyle);

                double itemTotal = detail.getAmount() * product.getPrice();
                createCell(row, 7, itemTotal, currencyStyle);
                createCell(row, 8, sale.getTotalPrice(), currencyStyle);
            }
        }
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value != null) {
            cell.setCellValue(value.toString());
        }

        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}