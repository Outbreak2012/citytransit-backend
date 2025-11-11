# CityTransit Backend

Backend API para CityTransit - Sistema de transporte público inteligente con IA.

## 🚀 Características

- ✅ **Autenticación JWT** - Login y registro seguro
- ✅ **GraphQL API** - Consultas eficientes y flexibles
- ✅ **REST API** - Endpoints tradicionales
- ✅ **PostgreSQL** - Base de datos relacional principal
- ✅ **MongoDB** - Datos no estructurados
- ✅ **Redis** - Caché y sesiones
- ✅ **WebSocket** - Comunicación en tiempo real
- ✅ **Kafka** - Mensajería asíncrona
- ✅ **Spring Security** - Seguridad robusta
- ✅ **Swagger/OpenAPI** - Documentación automática

## 📋 Requisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 14+
- MongoDB 6+
- Redis 7+
- Kafka 3+ (opcional)

## 🛠️ Instalación

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd citytransit-backend
```

### 2. Configurar la base de datos PostgreSQL

```sql
CREATE DATABASE citytransit;
CREATE USER citytransit_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE citytransit TO citytransit_user;
```

### 3. Configurar MongoDB

```bash
mongosh
use citytransit
```

### 4. Configurar variables de entorno

Editar `src/main/resources/application.properties`:

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/citytransit
spring.datasource.username=postgres
spring.datasource.password=your_password

# JWT Secret
jwt.secret=your-secret-key-minimum-256-bits

# Stripe
stripe.api-key=sk_test_your_stripe_key

# Firebase
firebase.credentials-path=classpath:firebase-service-account.json
```

### 5. Compilar el proyecto

```bash
mvn clean install
```

### 6. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en:
- **API REST**: http://localhost:8080
- **GraphQL**: http://localhost:8080/graphql
- **GraphiQL**: http://localhost:8080/graphiql
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 📚 Endpoints Principales

### REST API

#### Autenticación
```bash
# Registro
POST /api/auth/register
Content-Type: application/json
{
  "email": "user@example.com",
  "password": "password123",
  "nombreCompleto": "John Doe",
  "telefono": "+1234567890"
}

# Login
POST /api/auth/login
Content-Type: application/json
{
  "email": "user@example.com",
  "password": "password123"
}
```

### GraphQL API

#### Queries
```graphql
# Obtener usuario actual
query {
  me {
    usuarioId
    email
    nombreCompleto
    rol
  }
}

# Obtener tarjetas del usuario
query {
  misTarjetas {
    tarjetaId
    numeroTarjeta
    saldo
    estado
  }
}

# Consultar balance
query {
  balance(tarjetaId: 1) {
    tarjetaId
    saldo
    ultimoMovimiento
  }
}

# Obtener rutas activas
query {
  rutasActivas {
    rutaId
    codigoRuta
    nombre
    horarioInicio
    horarioFin
  }
}

# Historial de pasajes
query {
  historialPasajes(tarjetaId: 1, limit: 20, offset: 0) {
    pasajeId
    monto
    fechaHoraValidacion
    vehiculo {
      placa
      tipoVehiculo
    }
    ruta {
      nombre
    }
  }
}
```

#### Mutations
```graphql
# Login
mutation {
  login(input: {
    email: "user@example.com",
    password: "password123"
  }) {
    accessToken
    refreshToken
    user {
      usuarioId
      email
      nombreCompleto
    }
  }
}

# Registro
mutation {
  register(input: {
    email: "newuser@example.com",
    password: "password123",
    nombreCompleto: "Jane Doe",
    telefono: "+1234567890"
  }) {
    accessToken
    user {
      usuarioId
      email
    }
  }
}
```

## 🔐 Autenticación

El backend usa JWT para la autenticación. Para acceder a endpoints protegidos:

```bash
# REST
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" http://localhost:8080/api/...

# GraphQL
{
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

## 🏗️ Arquitectura

```
citytransit-backend/
├── src/main/java/com/citytransit/
│   ├── config/              # Configuraciones (Security, WebSocket, Cache)
│   ├── controller/          # Controladores REST
│   ├── resolver/            # Resolvers GraphQL
│   ├── service/             # Lógica de negocio
│   ├── repository/          # Repositorios JPA
│   ├── model/
│   │   ├── entity/          # Entidades JPA
│   │   ├── dto/             # DTOs
│   │   └── enums/           # Enumeraciones
│   ├── security/            # JWT, Filters, Auth
│   └── exception/           # Manejo de excepciones
└── src/main/resources/
    ├── application.properties
    └── graphql/
        └── schema.graphqls  # Esquema GraphQL
```

## 🧪 Testing

```bash
# Ejecutar tests
mvn test

# Con coverage
mvn test jacoco:report
```

## 📦 Despliegue

### Docker

```bash
# Construir imagen
docker build -t citytransit-backend .

# Ejecutar contenedor
docker run -p 8080:8080 citytransit-backend
```

### Docker Compose

```bash
docker-compose up -d
```

## 🔧 Tecnologías

- **Spring Boot 3.2.0** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - ORM
- **Spring GraphQL** - API GraphQL
- **PostgreSQL** - Base de datos principal
- **MongoDB** - Base de datos NoSQL
- **Redis** - Caché
- **Kafka** - Event streaming
- **JWT** - Tokens de autenticación
- **Lombok** - Reducción de boilerplate
- **MapStruct** - Mapeo de objetos
- **Swagger/OpenAPI** - Documentación API

## 📝 Licencia

MIT License

## 👥 Autores

CityTransit Development Team
"# citytransit-backend" 
