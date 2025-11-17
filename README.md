# Global de la catedra de Desarrollo de Software UTN FRM 2025 
### Alumno: Juan Ignacio Algozino
### Comicion: 3k9


# 🧬 API Detector de Mutantes (Examen MercadoLibre)

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot 3.x](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)
[![Docker](https://img.shields.io/badge/Docker-build-blue.svg)](https://www.docker.com/)
[![Tests](https://img.shields.io/badge/Tests-Passing-success.svg)]()
[![Coverage](https://img.shields.io/badge/Coverage-90%25+-brightgreen.svg)]()

Proyecto final que implementa una API REST para detectar si un humano es mutante basándose en su secuencia de ADN. Este proyecto cumple con los requisitos técnicos de la prueba de backend de MercadoLibre.

## 📋 Tabla de Contenidos
1. [Descripción del Proyecto](#-descripción-del-proyecto)
2. [Arquitectura](#-arquitectura)
3. [Tecnologías Utilizadas](#-tecnologías-utilizadas)
4. [Prerrequisitos](#-prerrequisitos)
5. [Instalación y Ejecución Local](#-instalación-y-ejecución-local)
6. [Cómo Probar la API](#-cómo-probar-la-api)
7. [Construir y Ejecutar con Docker](#-construir-y-ejecutar-con-docker)
8. [Detalles de la Implementación](#-detalles-de-la-implementación)
9. [Cobertura de Pruebas (Testing)](#-cobertura-de-pruebas-testing)

---

## 🎯 Descripción del Proyecto

El objetivo es crear una API que expone dos endpoints:
* `POST /mutant`: Recibe una secuencia de ADN (`String[]`) y determina si es mutante o humana.
* `GET /stats`: Devuelve estadísticas de las verificaciones de ADN realizadas.

### Reglas del Negocio
* **ADN Mutante**: Se considera mutante si se encuentran **más de una** secuencia de cuatro letras iguales (AAAA, TTTT, CCCC, GGGG).
* **Direcciones**: Las secuencias se buscan en forma horizontal, vertical y oblicua.
* **Persistencia**: Cada ADN verificado se almacena en una base de datos.
* **Optimización**: La API debe ser eficiente y evitar el re-procesamiento de ADNs idénticos (deduplicación).

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura hexagonal (en capas) para asegurar una clara separación de responsabilidades, alta cohesión y bajo acoplamiento.

[Image of a 6-layer architecture diagram]

1.  **Controller**: Expone la API REST (`MutantController`).
2.  **DTO (Data Transfer Objects)**: Define los contratos de la API (`DnaRequest`, `StatsResponse`).
3.  **Service**: Contiene la lógica de negocio (`MutantService`, `StatsService`) y el algoritmo (`MutantDetector`).
4.  **Repository**: Define la interfaz de acceso a datos (`DnaRecordRepository`).
5.  **Entity**: Modela la tabla de la base de datos (`DnaRecord`).
6.  **Config / Validation / Exception**: Clases de soporte para Swagger, validaciones personalizadas y manejo global de errores.

---

## 🛠️ Tecnologías Utilizadas

* **Java 17**: Lenguaje de programación.
* **Spring Boot 3.x**: Framework principal para la API REST, JPA y DI.
* **Gradle**: Gestor de dependencias y construcción del proyecto.
* **Spring Data JPA**: Para la persistencia de datos.
* **H2 Database**: Base de datos en memoria para ejecución local y pruebas.
* **Lombok**: Para reducir código boilerplate (getters, setters, constructores).
* **Springdoc OpenAPI (Swagger)**: Para la documentación interactiva de la API.
* **JUnit 5 & Mockito**: Para tests unitarios y de integración.
* **JaCoCo**: Para reportes de cobertura de código.
* **Docker**: Para la contenedorización de la aplicación.

---

## 📦 Prerrequisitos

* **Java JDK 17** (o superior).
* **Gradle 8.x** (opcional, el wrapper `gradlew` lo descargará automáticamente).
* **Docker Desktop** (para ejecutar la imagen de Docker).

---

## 🚀 Instalación y Ejecución Local

### 1. Clonar el Repositorio
```bash
git clone <URL_DEL_REPOSITORIO>
cd mutantes
```


### 2. (Opcional) Limpiar y Construir el Proyecto
Esto descargará las dependencias y compilará el código.
```bash
// En Windows
gradlew.bat clean build

// En Linux/Mac
./gradlew clean build
```


### 3. Ejecutar la Aplicación
Este es el comando principal para iniciar el servidor web:
```bash
// En Windows
gradlew.bat bootRun

// En Linux/Mac
./gradlew bootRun
```
Verás un log indicando Started MutantesApplication... y Tomcat started on port(s): 8080. La aplicación se quedará "atascada" o "ejecutándose" en la terminal (ej. > 80% EXECUTING), lo cual es correcto, ya que significa que el servidor está activo y escuchando peticiones.

---

## 💻 Cómo Probar la API

Una vez que la aplicación esté ejecutándose (`gradlew bootRun`), puedes probarla:

### Opción 1: Swagger UI (Recomendado)

Abre tu navegador y ve a la documentación interactiva:
* **URL:** `http://localhost:8080/swagger-ui.html`

Desde allí podrás probar los endpoints `POST /mutant` y `GET /stats` directamente.

### Opción 2: H2 Database Console

Para verificar que los datos se están guardando, puedes acceder a la consola de la base de datos en memoria:
* **URL:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:testdb`
* **User Name:** `sa`
* **Password:** (dejar en blanco)

Ejecuta la query `SELECT * FROM DNA_RECORDS;` para ver los ADNs procesados.

### Opción 3: cURL (Terminal)

```bash
// Prueba de un Mutante (devolverá HTTP 200 OK)
curl -X POST "http://localhost:8080/mutant" \
     -H "Content-Type: application/json" \
     -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'

// Prueba de un Humano (devolverá HTTP 403 Forbidden)
curl -X POST "http://localhost:8080/mutant" \
     -H "Content-Type: application/json" \
     -d '{"dna":["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]}'

// Obtener Estadísticas
curl -X GET "http://localhost:8080/stats"
```

---

## 🐳 Construir y Ejecutar con Docker

El proyecto incluye un `Dockerfile` optimizado (multi-etapa) para crear una imagen ligera y lista para producción.

### 1. Construir la Imagen
(Asegúrate de que Docker Desktop esté en ejecución).
```bash
docker build -t mutantes-api .
```
docker build: El comando para construir.

-t mutantes-api: Le da un nombre (etiqueta) a tu imagen.

.: Le dice a Docker que busque el Dockerfile en la carpeta actual.

### 2. Ejecutar el Contenedor
```bash
docker run -d -p 8080:8080 --name mutantes-container mutantes-api
```
-d: Modo "detached" (se ejecuta en segundo plano).

-p 8080:8080: Mapea el puerto 8080 de tu PC (el primero) al puerto 8080 del contenedor (el segundo).

--name mutantes-container: Le da un nombre fácil de recordar al contenedor.

mutantes-api: El nombre de la imagen que acabas de construir.

La API estará disponible en http://localhost:8080/swagger-ui.html.

---

## 🔬 Detalles de la Implementación

### Algoritmo (`MutantDetector.java`)
El algoritmo está optimizado para cumplir con los requisitos de la guía:
* **Single Pass:** Recorre la matriz (N*N) una sola vez (complejidad O(N²)).
* **Early Termination**: La búsqueda se detiene inmediatamente (`return true;`) en cuanto el contador de secuencias llega a 2.
* **Acceso O(1)**: El `String[]` se convierte a `char[][]` al inicio para un acceso rápido a la matriz.
* **Boundary Checking**: Se comprueban los límites de la matriz antes de buscar en cada dirección para evitar `IndexOutOfBoundsException`.

### Deduplicación (`MutantService.java`)
Para evitar volver a analizar un ADN ya procesado, se implementa una estrategia de caché en la base de datos:
1.  Se recibe el `String[] dna`.
2.  Se calcula un hash **SHA-256** de la secuencia de ADN concatenada.
3.  Se busca ese hash en la columna `dna_hash` de la base de datos (que tiene un índice `UNIQUE`).
4.  **Si se encuentra**, se devuelve el resultado cacheado (O(1)) sin ejecutar el algoritmo.
5.  **Si no se encuentra**, se ejecuta el algoritmo `isMutant()`, se guarda el resultado junto con el hash, y se devuelve el resultado.

---

## ✅ Cobertura de Pruebas (Testing)

El proyecto tiene una suite completa de tests (unitarios y de integración) que validan el 100% de la lógica de negocio y los endpoints, superando el 80% de cobertura de código requerido.

### 1. Ejecutar los Tests
Este comando ejecutará todos los tests unitarios y de integración:
```bash
// En Windows
gradlew.bat test
```

### 2. Generar Reporte de Cobertura (JaCoCo)
Este comando ejecuta los tests Y genera un reporte HTML de la cobertura:
```bash
// En Windows
gradlew.bat test jacocoTestReport
```
Para ver el reporte: Abre el archivo build/reports/jacoco/test/html/index.html en tu navegador.
