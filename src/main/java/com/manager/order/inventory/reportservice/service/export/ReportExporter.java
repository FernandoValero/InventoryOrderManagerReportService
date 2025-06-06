package com.manager.order.inventory.reportservice.service.export;

import java.util.List;
import com.manager.order.inventory.reportservice.dto.SaleDto;

import java.rmi.server.ExportException;

public interface ReportExporter<T> {
    byte[] export(List<SaleDto> sales, String title) throws ExportException;
}