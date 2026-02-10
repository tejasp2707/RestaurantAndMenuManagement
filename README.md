# Food Delivery System - Microservices

A complete microservices-based Food Delivery System built with Spring Boot 3.x and Java 17.

## Project Structure

```
food_delivery/
├── restaurant-service/          # Restaurant & Menu Management Service
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/fooddelivery/restaurant/
│   │       │       ├── controller/
│   │       │       ├── service/
│   │       │       ├── repository/
│   │       │       ├── entity/
│   │       │       ├── dto/
│   │       │       ├── mapper/
│   │       │       ├── security/
│   │       │       ├── exception/
│   │       │       └── config/
│   │       └── resources/
│   │           └── application.yml
│   ├── pom.xml
│   ├── Dockerfile
│   └── docker-compose.yml
│
└── eureka-server/               # Service Discovery Server
    ├── src/
    │   └── main/
    │       ├── java/
    │       └── resources/
    │           └── application.yml
    ├── pom.xml
    └── Dockerfile
```

## Services

### 1. Eureka Server (Service Discovery)
- **Port:** 8761
- **Purpose:** Service registry for microservices discovery
- **Dashboard:** http://localhost:8761

### 2. Restaurant Service
- **Port:** 8081
- **Purpose:** Restaurant and menu management
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

## Quick Start

### Clone the Repository
```bash
git clone https://github.com/tejasp2707/RestaurantAndMenuManagement.git
cd RestaurantAndMenuManagement
```

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
# In a new terminal
cd restaurant-service
mvn spring-boot:run
```
Service will run on: http://localhost:8081

### Option 2: Run with Docker

#### Build and Run Eureka Server
```bash
cd eureka-server
docker build -t eureka-server .
docker run -d -p 8761:8761 --name eureka-server eureka-server
```

#### Build and Run Restaurant Service
```bash
cd restaurant-service
docker-compose up --build
```

## Restaurant Service API

### Authentication
All protected endpoints require a JWT token:
```
Authorization: Bearer <jwt_token>
```

### Restaurant Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/restaurants` | Create restaurant | RESTAURANT_OWNER |
| PUT | `/api/restaurants/{id}` | Update restaurant | Owner/ADMIN |
| DELETE | `/api/restaurants/{id}` | Delete restaurant | ADMIN |
| PATCH | `/api/restaurants/{id}/status` | Enable/disable | ADMIN |
| GET | `/api/restaurants/{id}` | Get by ID | Public |
| GET | `/api/restaurants` | Get all | Public |
| GET | `/api/restaurants/owner/{ownerId}` | Get by owner | Public |

### Menu Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/restaurants/{restaurantId}/menu` | Add menu item | Owner/ADMIN |
| PUT | `/api/menu/{menuId}` | Update menu item | Owner/ADMIN |
| DELETE | `/api/menu/{menuId}` | Delete menu item | Owner/ADMIN |
| PATCH | `/api/menu/{menuId}/status` | Enable/disable | Owner/ADMIN |
| GET | `/api/restaurants/{restaurantId}/menu` | Get menu items | Public |
| GET | `/api/menu/{menuId}` | Get by ID | Public |

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
    "description": "Classic pizza",
    "price": 12.99,
    "category": "Pizza"
  }'
```

## Configuration

### Restaurant Service
Edit `restaurant-service/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/restaurant_db
    username: root
    password: root

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

jwt:
  secret: your_jwt_secret_key
```

### Eureka Server
Edit `eureka-server/src/main/resources/application.yml` as needed.

## Database Schema

### restaurants
- id, name, description, address, phone
- owner_id, active
- created_at, updated_at

### menu_items
- id, restaurant_id, name, description
- price, available, category
- created_at, updated_at

## Authorization Rules

### ADMIN
- Delete any restaurant
- Enable/disable any restaurant
- Manage all menu items

### RESTAURANT_OWNER
- Create restaurants
- Update only their own restaurants
- Manage menu items for their own restaurants

## Development

### Build All Services
```bash
# Build Eureka Server
cd eureka-server
mvn clean install

# Build Restaurant Service
cd ../restaurant-service
mvn clean install
```

### Run Tests
```bash
cd restaurant-service
mvn test
```

## Architecture

- **Microservices Architecture:** Independent, scalable services
- **Service Discovery:** Eureka for dynamic service registration
- **Clean Architecture:** Layered design (Controller → Service → Repository)
- **Security:** JWT authentication with role-based authorization
- **Database:** MySQL with JPA/Hibernate
- **Containerization:** Docker support for deployment

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
