

# 📦 Prices API — Hexagonal Architecture (Spring Boot 3, Java 17)

Este proyecto implementa un servicio REST que determina el **precio final aplicable** para un producto en un momento concreto, basado en reglas de prioridad y ventanas temporales.

El ejercicio replica el enunciado técnico de Inditex/Zara, siguiendo **arquitectura hexagonal**, principios **SOLID**, buenas prácticas REST y tests de integración end-to-end con H2 en memoria.

---

# 1. Arquitectura

El diseño sigue **Arquitectura Hexagonal (Ports & Adapters)** para desacoplar el dominio de la infraestructura.

```
┌──────────────────────────────────────────────────────────────┐
│                           APPLICATION                         │
│             (Casos de uso - Orquesta la lógica)               │
└──────────────────────────────────────────────────────────────┘
                     ▲                         ▲
                     │                         │
         Puerto de dominio             Puerto de persistencia
                     │                         │
                     ▼                         ▼
┌──────────────────────────────────────────────────────────────┐
│                             DOMAIN                           │
│       - Lógica de negocio                                     │
│       - Entidades de dominio (Price)                          │
│       - Puertos (PriceRepository)                             │
└──────────────────────────────────────────────────────────────┘
                     ▲                         ▲
                     │                         │
                     ▼                         ▼
┌──────────────────────────────────────────────────────────────┐
│                        INFRASTRUCTURE                        │
│  - REST Controller                     - Adaptador JPA        │
│  - DTOs / Mappers                      - H2 / Spring Data      │
└──────────────────────────────────────────────────────────────┘
```

Beneficios:

* Independencia del framework (Spring solo en infraestructura)
* Testabilidad elevada
* Dominio autocontenido y sin dependencias externas
* Adaptadores fácilmente reemplazables

---

# 2. Tecnologías utilizadas

* **Java 17**
* **Spring Boot 3**
* **Spring Web**
* **Spring Data JPA**
* **H2 Database (en memoria)**
* **Maven**
* **JUnit 5 + MockMvc**
* **Lombok**

---

#  3. Estructura del proyecto

```
src/main/java/com/prices
│
├── domain
│   ├── model
│   │   └── Price.java
│   └── repository
│       └── PriceRepository.java
│
├── application
│   └── PriceService.java
│
└── infraestructure
    ├── controller
    │   └── PriceController.java
    ├── entity
    │   └── PriceEntity.java
    └── repository
        ├── SpringDataPriceRepository.java
        └── JpaPriceRepository.java
```

Además:

```
src/main/resources
└── data.sql      # Datos iniciales para H2
```

---

#  4. Dominio

### Entidad de dominio

```java
public class Price {
    private Long brandId;
    private Long productId;
    private Long priceList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer priority;
    private Double price;
    private String curr;
}
```

### Puerto del dominio (persistencia independiente)

```java
public interface PriceRepository {
    Optional<Price> findApplicablePrice(Long brandId,
                                        Long productId,
                                        LocalDateTime applicationDate);
}
```

---

#  5. Caso de uso (Application Layer)

```java
@Service
public class PriceService {

    private final PriceRepository repository;

    public PriceService(PriceRepository repository) {
        this.repository = repository;
    }

    public Price getPrice(Long brandId, Long productId, LocalDateTime date) {
        return repository.findApplicablePrice(brandId, productId, date)
                .orElseThrow(() -> new RuntimeException("Price not found"));
    }
}
```

---

#  6. Adaptador JPA (Infrastructure)

```java
@Repository
public class JpaPriceRepository implements PriceRepository {

    private final SpringDataPriceRepository jpa;

    public JpaPriceRepository(SpringDataPriceRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long brandId,
                                               Long productId,
                                               LocalDateTime date) {
        return jpa.findFirstApplicablePrice(brandId, productId, date)
                .map(PriceEntity::toDomain);
    }
}
```

### Query JPA óptima

```java
@Query("""
    SELECT p FROM PriceEntity p
    WHERE p.brandId = :brandId
      AND p.productId = :productId
      AND :date BETWEEN p.startDate AND p.endDate
    ORDER BY p.priority DESC
    """)
Optional<PriceEntity> findFirstApplicablePrice(@Param("brandId") Long brandId,
                                               @Param("productId") Long productId,
                                               @Param("date") LocalDateTime date);
```

---

# 7. Endpoint REST

### **GET /price**

Parámetros:

| Parámetro | Tipo          | Ejemplo               |
| --------- | ------------- | --------------------- |
| brandId   | Long          | `1`                   |
| productId | Long          | `35455`               |
| date      | LocalDateTime | `2020-06-14-10.00.00` |

Ejemplo de request:

```
GET http://localhost:8080/price?brandId=1&productId=35455&date=2020-06-14-10.00.00
```

Ejemplo de response:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50
}
```

---

#  8. Datos iniciales (H2)

```
INSERT INTO PRICES (ID, BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR)
VALUES
  (1, 1, '2020-06-14 00:00:00', '2020-12-31 23:59:59', 1, 35455, 0, 35.50, 'EUR'),
  (2, 1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 2, 35455, 1, 25.45, 'EUR'),
  (3, 1, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 3, 35455, 1, 30.50, 'EUR'),
  (4, 1, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 4, 35455, 1, 38.95, 'EUR');
```

---

#  9. Tests de integración

Se validan los 5 casos del enunciado:

* 14/06 → 10:00
* 14/06 → 16:00
* 14/06 → 21:00
* 15/06 → 10:00
* 16/06 → 21:00

Ejemplo:

```java
mockMvc.perform(get("/price")
        .param("brandId", "1")
        .param("productId", "35455")
        .param("date", "2020-06-14-10.00.00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.priceList").value(1));
```

Todos los tests se ejecutan sobre H2 en memoria.

---

#  10. Cómo ejecutar

### Ejecución normal

```
mvn spring-boot:run
```

### Tests

```
mvn test
```

---

#  11. Decisiones técnicas destacadas

✔ Arquitectura Hexagonal para desacoplar dominio e infraestructura
✔ Dominio puro sin dependencias de Spring
✔ Endpoints REST siguiendo buenas prácticas
✔ Repositorio JPA optimizado con ordenación por prioridad
✔ H2 para testing consistente
✔ Tests de integración cubriendo todos los escenarios solicitados
✔ Código limpio, mantenible y orientado a SOLID

---

#  12. Mejoras futuras

* Manejo de errores con problem-details RFC-7807
* Caching para consultas recurrentes
* Versionado de API
* Validaciones con Bean Validation


