# Restaurant and Menu Management System

A microservices-based Food Delivery System built with Spring Boot 3.x and Java 17.

## Repository Structure

```
RestaurantAndMenuManagement/
├── eureka-server/              # Service Discovery Server
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── src/                        # Restaurant Service Source Code
│   └── main/
│       ├── java/
│       │   └── com/fooddelivery/restaurant/
│       │       ├── controller/
│       │       ├── service/
│       │       ├── repository/
│       │       ├── entity/
│       │       ├── dto/
│       │       ├── mapper/
│       │       ├── security/
│       │       ├── exception/
│       │       └── config/
│       └── resources/
│           └── application.yml
│
├── pom.xml                     # Restaurant Service Maven Config
├── Dockerfile                  # Restaurant Service Docker
├── docker-compose.yml          # Multi-service Docker Compose
└── README.md                   # This file
```

## Services

### 1. Eureka Server (Service Discovery)
- **Port:** 8761
- **Purpose:** Service registry for microservices discovery
- **Dashboard:** http://localhost:8761

### 2. Restaurant Service
- **Port:** 8081
- **Purpose:** Restaurant and menu management microservice
- **Features:**
  - Restaurant CRUD operations
  - Menu item management
  - JWT authentication
  - Role-based authorization (ADMIN, RESTAURANT_OWNER)
  - Ownership validation

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Cloud 2023.0.0**
- **Spring Security with JWT**
- **Spring Data JPA**
- **MySQL 8.0**
- **Eureka Server/Client**
- **Docker & Docker Compose**
- **Maven**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0
- Docker (optional)

## Getting Started

### Option 1: Run with Maven (Local Development)

#### 1. Start MySQL Database
```bash
docker run -d -p 3306:3306 --name mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=restaurant_db \
  mysql:8.0
```

#### 2. Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Access Eureka Dashboard: http://localhost:8761

#### 3. Start Restaurant Service
```bash
# In a new terminal, from the root directory
mvn spring-boot:run
```
Service will run on: http://localhost:8081

### Option 2: Run with Docker Compose

```bash
docker-compose up --build
```

This will start:
- MySQL database (port 3306)
- Restaurant service (port 8081)

**Note:** You'll need to start Eureka Server separately or add it to docker-compose.

## Restaurant Service API Endpoints

### Restaurant Management

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/restaurants` | Create restaurant | RESTAURANT_OWNER |
| PUT | `/api/restaurants/{id}` | Update restaurant | Owner/ADMIN |
| DELETE | `/api/restaurants/{id}` | Delete restaurant | ADMIN |
| PATCH | `/api/restaurants/{id}/status` | Enable/disable restaurant | ADMIN |
| GET | `/api/restaurants/{id}` | Get restaurant by ID | Public |
| GET | `/api/restaurants` | Get all restaurants | Public |
| GET | `/api/restaurants/owner/{ownerId}` | Get restaurants by owner | Public |

### Menu Management

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

## Example API Requests

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

### Get All Restaurants (Public)
```bash
curl http://localhost:8081/api/restaurants
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

## Configuration

### Restaurant Service Configuration
Edit `src/main/resources/application.yml`:

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

### Eureka Server Configuration
Edit `eureka-server/src/main/resources/application.yml` as needed.

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

## Architecture

The system follows a microservices architecture with:
- **Service Discovery:** Eureka Server for service registration and discovery
- **Clean Architecture:** Layered design (Controller → Service → Repository)
- **Security:** JWT-based authentication and role-based authorization
- **Database:** MySQL with JPA/Hibernate
- **Containerization:** Docker support for easy deployment

## Development

### Build the Project
```bash
mvn clean install
```

### Run Tests
```bash
mvn test
```

### Build Docker Image
```bash
docker build -t restaurant-service .
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License

## Contact

For questions or support, please open an issue in the repository.
