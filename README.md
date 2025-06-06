# 📊 Report Service

Este microservicio genera reportes de ventas (formato PDF o Excel ) por fecha, producto, cliente o usuario. Se comunica con el Inventory Service mediante Feign Client para obtener los datos necesarios.

## Tecnologías utilizadas
- Java 17
- Spring Boot
- Spring Web
- Spring Cloud (Eureka Client, Config Client)
- Feign
- Swagger/OpenAPI

## Funcionalidades
- Generación de reportes personalizados de ventas.
- Filtro por fechas, productos, clientes o usuarios.
- Exposición de endpoints RESTful.
- Comunicación con el Inventory Service mediante Feign Client.
- Registro en el servidor de descubrimiento Eureka.

## 📚 Documentación Swagger

Disponible al iniciar el servicio: `http://localhost:8080/swagger-ui/index.html`

---

## Endpoints principales
## 📈 Reportes

| Método | Endpoint                                                          | Descripción                                       |
| ------ | ----------------------------------------------------------------- | ------------------------------------------------- |
| GET    | `/reports/sales/year/{format}/{year}`                             | Generar reporte de ventas por **año**             |
| GET    | `/reports/sales/user/{format}/{userId}`                           | Generar reporte de ventas por **usuario**         |
| GET    | `/reports/sales/product/{format}/{productId}`                     | Generar reporte de ventas por **producto**        |
| GET    | `/reports/sales/month/{format}/{month}`                           | Generar reporte de ventas por **mes**             |
| GET    | `/reports/sales/client/{format}/{clientId}`                       | Generar reporte de ventas por **cliente**         |
| GET    | `/reports/sales/between/{format}?start=yyyy-MM-dd&end=yyyy-MM-dd` | Generar reporte de ventas por **rango de fechas** |


---

## Configuración

Este servicio obtiene su configuración desde el **Spring Cloud Config Server**.


## 🏁 Ejecución local

Asegúrate de que los servicios de Eureka, Config Server y Inventory Service estén corriendo.


## 🖼️ Arquitectura

                     +--------------------+
                     |  API Gateway       |
                     +--------------------+
                               |
                 +-------------+--------------+
                 |                            |
       +------------------+         +------------------+
       |  InventoryService| <-----> |  ReportService   |
       +------------------+         +------------------+
