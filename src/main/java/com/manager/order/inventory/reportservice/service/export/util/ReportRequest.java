package com.manager.order.inventory.reportservice.service.export.util;

import com.manager.order.inventory.reportservice.service.export.util.enums.ReportType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportRequest {
    private ReportType type;
    private String format;
    private Integer id;
    private String startDate;
    private String endDate;
    private Integer month;
    private Integer year;
    private Integer productId;
}