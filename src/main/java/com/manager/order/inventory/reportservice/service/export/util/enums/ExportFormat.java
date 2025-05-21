package com.manager.order.inventory.reportservice.service.export.util.enums;

import com.manager.order.inventory.reportservice.exception.ReportGenerationException;

public enum ExportFormat {
    PDF("pdf", "application/pdf"),
    EXCEL("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final String fileExtension;
    private final String contentType;

    ExportFormat(String fileExtension, String contentType) {
        this.fileExtension = fileExtension;
        this.contentType = contentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getContentType() {
        return contentType;
    }

    public static ExportFormat fromString(String format) {
        for (ExportFormat f : values()) {
            if (f.name().equalsIgnoreCase(format) || f.getFileExtension().equalsIgnoreCase(format)) {
                return f;
            }
        }
        throw new ReportGenerationException("Report format not supported: " + format);
    }
}