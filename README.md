# E-Commerce Server
System design and backend service prototype for E-commerce Website. It uses [Swagger](https://swagger.io/) for API documentation & testing sandbox (`http://localhost:8080`).

![image](https://github.com/user-attachments/assets/136abfcd-fadb-4327-aeb3-74d66508780e)

## Features
- API to manage Registration, Customer, Product Catalogue and Order / Sales
- Cache for Products and Sales record
- Security:
  - Role based access (Admin and Customer)
  - JWT Token based Login
  - Password encryption
- Exception Handling
- Swagger Doc and API sandbox
- Docker compose

## Dependency:
- Spring Boot 3.4.0, JDK 17, Maven
- Spring Security 6.4.2
- Database: MySQL 8, PostgreSQL 17
- Cache: ConcurrentMap, Redis (7.4.1)
- Modelmapper 3.2, JJWT 0.12.6
- Spring Doc (OpenAPI, Swagger) 2.7.0

## Setup using Docker
- Install [Docker desktop](https://www.docker.com/products/docker-desktop/) (v 4.34.2)
- Project includes docker configuratins- **Dockerfile** and **docker-compose.yml**
- Open CMD, Go to project root and execute command (`> docker-compose up`)
- It'll take a while to finish, services may restart multiple times during initialization
- Once complete, Go to- http://localhost:8080

## How to use?
- Open API document- http://localhost:8080
- Create / Read Token for Admin Account - `/api/open/registration/admin`
- Create Token for Customer Account - `/api/open/registration/customer`
  - Request Body: `{ "password": "user123", "customer": { "name": "Lalit Kumar", "gender": "MALE", "mobileNumber": "9900011001", "email": "lalit.k@g.com" } }`
- Note Token, use this token for other requests

## Data Model

![image](https://github.com/user-attachments/assets/33c6b0d2-813a-4433-a3b9-4150623f4ed5)

### Data Storage Approach

Catalogue (Product):  
Flexible schema is required considering variable Product attributes, provision for horizontal scaling to support high traffic and availability. Document Database like **Elastic Search** can be used for storing Product metadata with <ins>flexible schema and Full-text Search ability</ins>.

Shopping Cart or Wish list:  
This may not require relational model or ACID guarantee; also storage will grow fast as random items may be added and removed. NoSQL storage (like **Cassandra, Scylladb**) with support for high scale and availability can be used. 
 
Order:  
This requires ACID guarantees and relational model. We can use any RDBMS database (like **MySQL, Postgresql**).

## High Level Design

![image](https://github.com/user-attachments/assets/5105a08f-0b78-40e4-a66a-8f4b8571fe7a)

Load Balancer with multi-node setup (Multi-AZ) for Service, Database, Cache, MQs etc. is configured to avoid single-point of failure, backup / disaster recovery and increase availability.
