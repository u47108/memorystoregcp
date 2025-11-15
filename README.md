# Memory Store GCP (Redis Service)

Servicio que utiliza Redis (Memorystore) en Google Cloud Platform para caché y almacenamiento de datos en memoria con Spring Boot 3.5.7 y WebFlux.

## 📋 Descripción

Este servicio demuestra el uso de Redis/Memorystore en GCP con Spring Boot para operaciones de caché y almacenamiento en memoria. Utiliza Spring Data Redis Reactive y WebFlux para operaciones asíncronas y reactivas.

## 🚀 Características

- ✅ Integración con Redis/Memorystore en GCP
- ✅ Caché distribuido con Spring Cache
- ✅ Spring Data Redis Reactive
- ✅ WebFlux para operaciones reactivas
- ✅ API REST documentada
- ✅ Spring Boot 3.5.7 con Java 21
- ✅ Health check endpoints

## 📋 Requisitos

- Java 21+
- Gradle 8.9+
- Redis 6.0+ (local o Memorystore en GCP)
- Google Cloud SDK (para despliegue en GCP)

## ⚙️ Configuración

### Redis Local con Docker

```bash
# Ejecutar Redis localmente
docker run -d --name redis -p 6379:6379 redis:7-alpine

# Verificar conexión
docker exec -it redis redis-cli ping
# Debe retornar: PONG
```

### Memorystore en GCP

1. Crear instancia de Memorystore Redis:
```bash
gcloud redis instances create my-redis-instance \
  --size=1 \
  --region=us-central1 \
  --tier=basic
```

2. Obtener IP de la instancia:
```bash
gcloud redis instances describe my-redis-instance --region=us-central1
```

### Variables de Entorno

```properties
# Redis Local
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# Memorystore GCP
REDIS_HOST=10.0.0.1  # IP de Memorystore
REDIS_PORT=6379
REDIS_PASSWORD=

# Application
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=local

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:3000
```

### application.properties

```properties
spring.application.name=memorystore-service
server.port=8080

# Redis Configuration
spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
spring.data.redis.password=${REDIS_PASSWORD:}
spring.data.redis.timeout=2000ms

# Cache Configuration
spring.cache.type=redis
spring.cache.cache-names=users,cache1,cache2
spring.cache.redis.time-to-live=600000

# Reactive Redis
spring.data.redis.repositories.enabled=true

# CORS
cors.allowed-origins=http://localhost:4200,http://localhost:3000
```

## 🏃 Ejecución Local

### Con Gradle Wrapper

```bash
# Compilar
./gradlew clean build

# Ejecutar (requiere Redis corriendo)
./gradlew bootRun

# Ejecutar con perfil específico
./gradlew bootRun --args='--spring.profiles.active=local'

# Ejecutar JAR
java -jar build/libs/memorystoregcp-0.0.1-SNAPSHOT.jar
```

### Con Docker Compose

Crea un archivo `docker-compose.yml`:

```yaml
version: '3.8'
services:
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  memorystore-service:
    build: .
    ports:
      - "8080:8080"
    environment:
      REDIS_HOST: redis
      REDIS_PORT: 6379
      SPRING_PROFILES_ACTIVE: local
    depends_on:
      - redis

volumes:
  redis_data:
```

Ejecutar:
```bash
docker-compose up -d
```

## 🐳 Docker

### Construir Imagen

```bash
docker build -t memorystore-service:latest .
```

### Ejecutar Contenedor

```bash
docker run -d \
  --name memorystore-service \
  -p 8080:8080 \
  -e REDIS_HOST=redis \
  -e REDIS_PORT=6379 \
  -e SPRING_PROFILES_ACTIVE=production \
  --link redis:redis \
  memorystore-service:latest
```

## ☁️ Despliegue en GCP

### Cloud Run (con Memorystore)

1. Conectar Cloud Run a Memorystore:
```bash
gcloud run deploy memorystore-service \
  --image gcr.io/PROJECT_ID/memorystore-service \
  --platform managed \
  --region us-central1 \
  --vpc-connector projects/PROJECT_ID/locations/us-central1/connectors/CONNECTOR_NAME \
  --set-env-vars REDIS_HOST=10.0.0.1 \
  --set-env-vars REDIS_PORT=6379
```

### Kubernetes (con Memorystore)

Ver documentación de Kubernetes para configuración detallada con VPC connector.

## 📡 API Endpoints

### POST /api/users

Guarda un usuario en Redis.

**Request**:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30
}
```

**Response**:
```json
{
  "status": "success",
  "message": "User saved successfully"
}
```

### GET /api/users/{name}

Obtiene un usuario de Redis por nombre.

**Request**: `GET /api/users/John%20Doe`

**Response**:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30
}
```

### GET /actuator/health

Health check endpoint que verifica conexión a Redis.

**Response**:
```json
{
  "status": "UP",
  "components": {
    "redis": {
      "status": "UP"
    }
  }
}
```

## 🔐 Seguridad

### Configuración CORS

Configurado para permitir solo orígenes específicos:

```properties
cors.allowed-origins=https://production-domain.com
```

### Redis Security

Para producción:
- Usar Redis con autenticación habilitada
- Usar TLS/SSL para conexiones encriptadas
- Configurar firewall para permitir solo IPs necesarias

**Ver**: [SECURITY_IMPROVEMENTS.md](../SECURITY_IMPROVEMENTS.md)

## 📊 Monitoreo y Logging

### Redis Metrics

El servicio expone métricas de Redis en `/actuator/metrics/redis.*`

### Logging

Configurar logging para Redis:
```properties
logging.level.io.lettuce.core=DEBUG
logging.level.org.springframework.data.redis=DEBUG
```

## 🧪 Testing

```bash
# Todos los tests
./gradlew test

# Con cobertura
./gradlew test jacocoTestReport

# Ver reporte de cobertura
open build/reports/jacoco/test/html/index.html
```

### Test con Redis Embebido

Para tests, puedes usar Redis embebido o Mock Redis:

```java
@Test
void testSaveUser() {
    // Arrange
    User user = new User();
    user.setName("Test User");
    
    // Act
    Boolean result = userDao.saveUser(user);
    
    // Assert
    assertTrue(result);
    assertNotNull(userDao.findByName("Test User"));
}
```

## 📚 Documentación de API

Una vez ejecutando el servicio:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 📝 Estructura del Proyecto

```
memorystoregcp/
├── src/
│   ├── main/
│   │   ├── java/com/latam/bebigquery/
│   │   │   ├── repository/
│   │   │   │   ├── UserDao.java
│   │   │   │   ├── UserDaoImpl.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── entity/
│   │   │   │   └── User.java
│   │   │   └── controller/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── build.gradle
├── settings.gradle
└── README.md
```

## 🔧 Troubleshooting

### Error: Unable to connect to Redis

1. Verificar que Redis esté ejecutándose: `docker ps` o `redis-cli ping`
2. Verificar host y puerto en `application.properties`
3. Verificar conectividad de red
4. Verificar firewall si usas Memorystore

### Error: Connection refused

1. Verificar que Redis esté escuchando en el puerto correcto
2. Verificar que no haya firewall bloqueando la conexión
3. Verificar configuración de VPC si usas Memorystore

### Error: Password required

1. Configurar password en `application.properties`:
```properties
spring.data.redis.password=your-password
```

### Error: Memorystore connection timeout

1. Verificar configuración de VPC connector
2. Verificar que Cloud Run tenga acceso a Memorystore
3. Verificar que IP de Memorystore sea correcta

## 🛠️ Desarrollo

### Agregar Nueva Funcionalidad de Caché

```java
@Cacheable(value = "users", key = "#name")
public User findByName(String name) {
    // Lógica de búsqueda
}

@CacheEvict(value = "users", key = "#user.name")
public void deleteUser(User user) {
    // Lógica de eliminación
}
```

### Comandos Útiles

```bash
# Limpiar y compilar
./gradlew clean build

# Ejecutar tests
./gradlew test

# Verificar dependencias
./gradlew dependencies

# Limpiar Redis (cuidado en producción)
redis-cli FLUSHALL
```

## 🔄 Flujo de Datos

1. **Recepción**: El controlador recibe la petición HTTP
2. **Caché**: Verifica si los datos están en Redis
3. **Procesamiento**: Si no está en caché, procesa y guarda
4. **Respuesta**: Retorna datos desde caché o procesados

## 📞 Soporte

Para reportar issues o hacer preguntas:
1. Abre un issue en el repositorio
2. Revisa la documentación principal: [../README.md](../README.md)
3. Consulta la documentación de Redis: https://redis.io/documentation
4. Consulta la documentación de Memorystore: https://cloud.google.com/memorystore/docs/redis

---

**Versión**: 0.0.1-SNAPSHOT  
**Última actualización**: Enero 2025
