package com.manager.order.inventory.reportservice.controller;

import com.manager.order.inventory.reportservice.exception.NotFoundException;
import com.manager.order.inventory.reportservice.exception.ReportGenerationException;
import com.manager.order.inventory.reportservice.service.export.util.ReportRequest;
import com.manager.order.inventory.reportservice.service.export.util.enums.ExportFormat;
import com.manager.order.inventory.reportservice.service.ReportService;
import com.manager.order.inventory.reportservice.service.export.util.enums.ReportType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Reportes", description = "Creación de reportes detallados de ventas en múltiples formatos (PDF, Excel) con capacidades de filtrado avanzado")
public class ReportController {

    ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales/user/{format}/{userId}")
    @Operation(
            summary = "Generar reporte de ventas por usuario",
            description = "Genera un reporte de todas las ventas realizadas por un usuario específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado exitosamente",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "file", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en generación de reporte",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error al generar el reporte",
                                                    value = """
                                                    {
                                                      "error": "Error generating report."
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "El servicio de ventas no responde",
                                                    value = """
                                                    {
                                                      "error": "No response received from the inventory service"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato no soportado",
                                                    value = """
                                                    {
                                                      "error": "Report format not supported: ****"
                                                    }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Ventas asociadas al usuario no encontradas",
                                            value = """
                                            {
                                              "message": "No sales found."
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> generateReportByUser(
            @Parameter(
                    name = "format",
                    description = "Formato del reporte (PDF, EXCEL)",
                    example = "PDF", required = true)
            @PathVariable String format,

            @Parameter(
                    name = "userId",
                    description = "ID del usuario",
                    example = "1", required = true)
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
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (NotFoundException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            response.put(ERROR, "Error generating report.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @GetMapping("/sales/client/{format}/{clientId}")
    @Operation(
            summary = "Generar reporte de ventas por cliente",
            description = "Genera un reporte de todas las ventas realizadas por un cliente específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado exitosamente",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "file", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en generación de reporte",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error al generar el reporte",
                                                    value = """
                                                    {
                                                      "error": "Error generating report."
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "El servicio de ventas no responde",
                                                    value = """
                                                    {
                                                      "error": "No response received from the inventory service"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato no soportado",
                                                    value = """
                                                    {
                                                      "error": "Report format not supported: ****"
                                                    }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Ventas asociadas al cliente no encontradas",
                                            value = """
                                            {
                                              "message": "No sales found."
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> generateReportByClient(
            @Parameter(
                    name = "format",
                    description = "Formato del reporte (PDF, EXCEL)",
                    example = "PDF", required = true)
            @PathVariable String format,

            @Parameter(
                    name = "clientId",
                    description = "ID del cliente",
                    example = "1", required = true)
            @PathVariable Integer clientId) {
        Map<String, Object> response = new HashMap<>();
        try{
            ReportRequest request = ReportRequest.builder()
                    .type(ReportType.CLIENT)
                    .id(clientId)
                    .format(format)
                    .build();

            return buildResponse(request);
        } catch (ReportGenerationException  e) {
            response.put(ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (NotFoundException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            response.put(ERROR, "Error generating report.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }


    @GetMapping("/sales/between/{format}")
    @Operation(
            summary = "Generar reporte de ventas por rango de fechas",
            description = "Genera un reporte de todas las ventas realizadas por un rango de fechas específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado exitosamente",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "file", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en generación de reporte",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error al generar el reporte",
                                                    value = """
                                                    {
                                                      "error": "Error generating report."
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "El servicio de ventas no responde",
                                                    value = """
                                                    {
                                                      "error": "No response received from the inventory service"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato no soportado",
                                                    value = """
                                                    {
                                                      "error": "Report format not supported: ****"
                                                    }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No hay ventas en el rango de fechas",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Ventas en el rango de fechas especificado no encontradas",
                                            value = """
                                            {
                                              "message": "No sales found."
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> generateReportByDateRange(
            @Parameter(
                    name = "format",
                    description = "Formato del reporte (PDF, EXCEL)",
                    example = "PDF", required = true)
            @PathVariable String format,

            @Parameter(
                    name = "startDate",
                    description = "Fecha de inicio (formato dd/MM/yyyy)",
                    example = "01/05/2025"
            )
            @RequestParam("startDate") String startDate,

            @Parameter(
                    name = "endDate",
                    description = "Fecha de fin (formato dd/MM/yyyy)",
                    example = "31/05/2025"
            )
            @RequestParam("endDate") String endDate) {
        Map<String, Object> response = new HashMap<>();
        try{
            ReportRequest request = ReportRequest.builder()
                    .type(ReportType.DATE_RANGE)
                    .startDate(startDate)
                    .endDate(endDate)
                    .format(format)
                    .build();

            return buildResponse(request);
        } catch (ReportGenerationException  e) {
            response.put(ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (NotFoundException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            response.put(ERROR, "Error generating report.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @GetMapping("/sales/month/{format}/{month}")
    @Operation(
            summary = "Generar reporte de ventas por mes",
            description = "Genera un reporte de todas las ventas realizadas en un mes específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado exitosamente",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "file", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en generación de reporte",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error al generar el reporte",
                                                    value = """
                                                    {
                                                      "error": "Error generating report."
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "El servicio de ventas no responde",
                                                    value = """
                                                    {
                                                      "error": "No response received from the inventory service"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato no soportado",
                                                    value = """
                                                    {
                                                      "error": "Report format not supported: ****"
                                                    }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No hay ventas en el mes",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Ventas en el mes especificado no encontradas",
                                            value = """
                                            {
                                              "message": "No sales found."
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> generateReportByMonth(
            @Parameter(
                    name = "format",
                    description = "Formato del reporte (PDF, EXCEL)",
                    example = "PDF", required = true)
            @PathVariable String format,

            @Parameter(
                    name = "month",
                    description = "Numero del mes",
                    example = "5", required = true)
            @PathVariable Integer month) {
        Map<String, Object> response = new HashMap<>();
        try{
            ReportRequest request = ReportRequest.builder()
                    .type(ReportType.MONTH)
                    .month(month)
                    .format(format)
                    .build();

            return buildResponse(request);
        } catch (ReportGenerationException  e) {
            response.put(ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (NotFoundException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            response.put(ERROR, "Error generating report.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @GetMapping("/sales/year/{format}/{year}")
    @Operation(
            summary = "Generar reporte de ventas por año",
            description = "Genera un reporte de todas las ventas realizadas en un año específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado exitosamente",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "file", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en generación de reporte",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error al generar el reporte",
                                                    value = """
                                                    {
                                                      "error": "Error generating report."
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "El servicio de ventas no responde",
                                                    value = """
                                                    {
                                                      "error": "No response received from the inventory service"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato no soportado",
                                                    value = """
                                                    {
                                                      "error": "Report format not supported: ****"
                                                    }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No hay ventas en el año",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Ventas en el año especificado no encontradas",
                                            value = """
                                            {
                                              "message": "No sales found."
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> generateReportByYear(
            @Parameter(
                    name = "format",
                    description = "Formato del reporte (PDF, EXCEL)",
                    example = "PDF", required = true)
            @PathVariable String format,

            @Parameter(
                    name = "year",
                    description = "Numero del año",
                    example = "2025", required = true)
            @PathVariable Integer year) {
        Map<String, Object> response = new HashMap<>();
        try{
            ReportRequest request = ReportRequest.builder()
                    .type(ReportType.YEAR)
                    .year(year)
                    .format(format)
                    .build();

            return buildResponse(request);
        } catch (ReportGenerationException  e) {
            response.put(ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (NotFoundException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            response.put(ERROR, "Error generating report.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @GetMapping("/sales/product/{format}/{productId}")
    @Operation(
            summary = "Generar reporte de ventas por producto",
            description = "Genera un reporte de todas las ventas realizadas por un producto específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado exitosamente",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "file", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en generación de reporte",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error al generar el reporte",
                                                    value = """
                                                    {
                                                      "error": "Error generating report."
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "El servicio de ventas no responde",
                                                    value = """
                                                    {
                                                      "error": "No response received from the inventory service"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato no soportado",
                                                    value = """
                                                    {
                                                      "error": "Report format not supported: ****"
                                                    }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Producto no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Ventas asociadas al producto no encontradas",
                                            value = """
                                            {
                                              "message": "No sales found."
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> generateReportByProduct(
            @Parameter(
                    name = "format",
                    description = "Formato del reporte (PDF, EXCEL)",
                    example = "PDF", required = true)
            @PathVariable String format,

            @Parameter(
                    name = "productId",
                    description = "ID del producto",
                    example = "1", required = true)
            @PathVariable Integer productId) {
        Map<String, Object> response = new HashMap<>();
        try{
            ReportRequest request = ReportRequest.builder()
                    .type(ReportType.PRODUCT)
                    .productId(productId)
                    .format(format)
                    .build();
            return buildResponse(request);
        } catch (ReportGenerationException  e) {
            response.put(ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (NotFoundException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            response.put(ERROR, "Error generating report.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
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
