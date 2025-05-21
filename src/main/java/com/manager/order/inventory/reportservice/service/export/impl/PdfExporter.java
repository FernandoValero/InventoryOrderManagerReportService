package com.manager.order.inventory.reportservice.service.export.impl;

import java.io.IOException;
import java.util.List;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.exceptions.PdfException;
import com.manager.order.inventory.reportservice.dto.SaleDto;
import com.manager.order.inventory.reportservice.service.export.ReportExporter;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.rmi.server.ExportException;
import java.util.Objects;

@Service
public class PdfExporter implements ReportExporter<List<SaleDto>> {
    private final TemplateEngine templateEngine;

    public PdfExporter(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "TemplateEngine is null");
    }

    @Override
    public byte[] export(List<SaleDto> sales, String title) throws ExportException {
        Objects.requireNonNull(sales, "The sales list cannot be null");
        Objects.requireNonNull(title, "The title cannot be void");

        if (sales.isEmpty()) {
            throw new ExportException("The sales list cannot be empty");
        }
        if (title.trim().isEmpty()) {
            throw new ExportException("The title cannot be empty");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            double totalSales = calculateTotalSales(sales);
            String html = generateHtml(sales, title, totalSales);
            return generatePdf(html, outputStream);
        } catch (PdfException | IOException e) {
            throw new ExportException("Error generating PDF document ", e);
        }
    }

    /**
     * Calculate the total selling price
     * @param sales Sales list to calculate the total selling price. It must not be null or empty.
     * @return Total selling price.
     */
    private double calculateTotalSales(List<SaleDto> sales) {
        return sales.stream()
                .mapToDouble(SaleDto::getTotalPrice)
                .sum();
    }

    private String generateHtml(List<SaleDto> sales, String title, double totalSales) {
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("sales", sales);
        context.setVariable("totalSales", totalSales);
        return templateEngine.process("reports/pdf_sales_report", context);
    }

    private byte[] generatePdf(String html, ByteArrayOutputStream outputStream) throws IOException {

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = null;

        try {
            document = new Document(pdf);
            ConverterProperties properties = new ConverterProperties();
            HtmlConverter.convertToPdf(html, pdf, properties);
            return outputStream.toByteArray();
        } finally {
            if (document != null) {
                document.close();
            }
            pdf.close();
            writer.close();
        }
    }
}