# Restaurant and Menu Management Microservice

A production-ready microservice for managing restaurants and menus in a food delivery system, built with Spring Boot 3.x and Java 17.

## Features

- **Restaurant Management**: Create, update, delete, and manage restaurant profiles
- **Menu Management**: Add, update, delete, and manage menu items for restaurants
- **JWT Authentication**: Secure endpoints with JWT-based authentication
- **Role-Based Authorization**: ADMIN and RESTAURANT_OWNER roles with specific permissions
- **Ownership Validation**: Ensures restaurant owners can only manage their own resources
- **Service Discovery**: Integrates with Eureka for service registration
- **Docker Support**: Containerized deployment with Docker and Docker Compose

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Data JPA**
- **Spring Security**
- **MySQL 8.0**
- **Eureka Client**
- **JWT (jjwt 0.12.3)**
- **Maven**
- **Docker**

## Architecture

The service follows a clean layered architecture:

```
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Data access layer
├── entity/         # JPA entities
├── dto/            # Data transfer objects
├── mapper/         # Entity-DTO mappers
├── security/       # JWT authentication & authorization
├── exception/      # Custom exceptions & global handler
└── config/         # Spring configuration
```

## Prerequisites

- Java 17
- Maven 3.6+
- MySQL 8.0
- Eureka Server (running on port 8761)

## Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/restaurant_db
    username: root
    password: your_password

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

jwt:
  secret: your_jwt_secret_key
```

## Running Locally

### 1. Start MySQL
```bash
# Using Docker
docker run -d -p 3306:3306 --name mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=restaurant_db \
  mysql:8.0
```

### 2. Start Eureka Server
Ensure Eureka Server is running on port 8761

### 3. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The service will start on port **8081**.

## Running with Docker

### Build and Run with Docker Compose
```bash
docker-compose up --build
```

This will start:
- MySQL database on port 3306
- Restaurant service on port 8081

## API Endpoints

### Restaurant Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/restaurants` | Create restaurant | RESTAURANT_OWNER |
| PUT | `/api/restaurants/{id}` | Update restaurant | Owner/ADMIN |
| DELETE | `/api/restaurants/{id}` | Delete restaurant | ADMIN |
| PATCH | `/api/restaurants/{id}/status` | Enable/disable restaurant | ADMIN |
| GET | `/api/restaurants/{id}` | Get restaurant by ID | Public |
| GET | `/api/restaurants` | Get all restaurants | Public |
| GET | `/api/restaurants/owner/{ownerId}` | Get restaurants by owner | Public |

### Menu Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/restaurants/{restaurantId}/menu` | Add menu item | Owner/ADMIN |
| PUT | `/api/menu/{menuId}` | Update menu item | Owner/ADMIN |
| DELETE | `/api/menu/{menuId}` | Delete menu item | Owner/ADMIN |
| PATCH | `/api/menu/{menuId}/status` | Enable/disable menu item | Owner/ADMIN |
| GET | `/api/restaurants/{restaurantId}/menu` | Get menu items | Public |
| GET | `/api/menu/{menuId}` | Get menu item by ID | Public |

## Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

The JWT token must contain:
- `userId`: User ID
- `role`: Either `ROLE_ADMIN` or `ROLE_RESTAURANT_OWNER`

## Authorization Rules

### ADMIN
- Can delete any restaurant
- Can enable/disable any restaurant
- Can manage all menu items

### RESTAURANT_OWNER
- Can create restaurants
- Can update only their own restaurants
- Can manage menu items only for their own restaurants

## Example Requests

### Create Restaurant
```bash
curl -X POST http://localhost:8081/api/restaurants \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pizza Palace",
    "description": "Best pizza in town",
    "address": "123 Main St",
    "phone": "1234567890"
  }'
```

### Add Menu Item
```bash
curl -X POST http://localhost:8081/api/restaurants/1/menu \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Margherita Pizza",
    "description": "Classic pizza with tomato and mozzarella",
    "price": 12.99,
    "category": "Pizza"
  }'
```

## Database Schema

### restaurants
- `id` (BIGINT, PK)
- `name` (VARCHAR)
- `description` (VARCHAR)
- `address` (VARCHAR)
- `phone` (VARCHAR)
- `owner_id` (BIGINT)
- `active` (BOOLEAN)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### menu_items
- `id` (BIGINT, PK)
- `restaurant_id` (BIGINT)
- `name` (VARCHAR)
- `description` (VARCHAR)
- `price` (DECIMAL)
- `available` (BOOLEAN)
- `category` (VARCHAR)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## Error Handling

The service returns standardized error responses:

```json
{
  "timestamp": "2024-02-10T21:43:24",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant not found with id: 1",
  "path": "/api/restaurants/1"
}
```

HTTP Status Codes:
- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Validation error
- `401 Unauthorized` - Missing/invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Service Discovery

The service registers itself with Eureka Server as `restaurant-service`. Other services can discover and communicate with it through Eureka.

## Development

### Project Structure
```
src/main/java/com/fooddelivery/restaurant/
├── RestaurantServiceApplication.java
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── mapper/
├── repository/
├── security/
└── service/
```

### Best Practices Followed
- Constructor injection for dependencies
- DTOs for all external APIs
- No direct entity exposure
- Global exception handling
- Transaction management
- Proper validation
- Clean architecture

## License

MIT License
