package com.manager.order.inventory.reportservice.service;

import com.manager.order.inventory.reportservice.service.export.util.ReportRequest;

public interface ReportService {

    byte[] generateReport(ReportRequest request);
}
