Auth Service - WEGD Project
Overview
The auth_service module is a core component of the WEGD project, built with Spring Boot and designed to handle user authentication, authorization, and user management. It provides a secure, scalable, and extensible authentication system using JWT (JSON Web Tokens), Redis for caching, and Kafka for event-driven communication. The service supports features such as two-factor authentication (2FA), role-based access control (RBAC), OTP (One-Time Password) generation, and user profile management.

This service is part of a microservices architecture and integrates with other services via Kafka for asynchronous event handling. It leverages Redis for token management and caching usernames to improve performance.

Features
User Authentication: Login, logout, and token refresh using JWT.
Two-Factor Authentication (2FA): Optional 2FA support with OTP verification.
Role-Based Access Control (RBAC): Assigns roles (e.g., USER, MIDDLE_MAN) to users and enforces access control.
Token Management: Uses JWT for access and refresh tokens, with Redis to track token status (active/inactive).
User Management: Registration, password reset, profile updates, and account deletion.
OTP System: Generates and validates OTPs for 2FA, password changes, and phone number verification.
Event-Driven Architecture: Publishes events (e.g., registration, OTP, forgot password) to Kafka topics.
Caching: Stores usernames in Redis for efficient search and retrieval.
Security: CSRF protection disabled, stateless session management, and custom exception handling.
Technologies Used
Java: Core programming language.
Spring Boot: Framework for building the service.
Spring Security: Handles authentication and authorization with JWT.
JWT (JSON Web Tokens): Token-based authentication.
Redis: Caching and token management.
Kafka: Asynchronous event publishing.
Lombok: Reduces boilerplate code.
Spring Data: Repository-based data access.
Prerequisites
Java 17+: Ensure JDK is installed.
Maven: For dependency management and building the project.
Redis: A running Redis instance for caching and token storage.
Kafka: A Kafka broker for event publishing.
Database: A relational database (e.g., PostgreSQL or MySQL) configured for user data storage.




Setup Instructions
Clone the Repository:



git clone <repository-url>
cd auth_service
Configure Environment:
Create an application.yml file in src/main/resources/ with the following properties:
yaml

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/wegd_auth
    username: <db-username>
    password: <db-password>
  redis:
    host: localhost
    port: 6379
  kafka:
    bootstrap-servers: localhost:9092
jwt:
  secret: <your-jwt-secret>
  refreshToken:
    expiration: 604800000 # 7 days in milliseconds
Install Dependencies:

mvn clean install
Run the Application:

mvn spring-boot:run
Verify the Service:
The service will start on http://localhost:8080 (or the configured port).
Test the /api/v1/auth/login endpoint with a POST request to ensure it’s working.
API Endpoints
Authentication
POST /api/v1/auth/login: Authenticate a user and return access/refresh tokens.
POST /api/v1/auth/register: Register a new user.
POST /api/v1/auth/refresh: Refresh an access token using a refresh token.
POST /api/v1/auth/logout: Logout a user by invalidating the refresh token.
POST /api/v1/auth/logoutAll: Logout a user from all sessions.
User Management
POST /api/v1/auth/forgetPassword: Reset a user’s password and send a new one via email.
POST /api/v1/auth/changePassword: Change the user’s password with OTP verification.
POST /api/v1/auth/deleteAccount: Delete the user account with OTP verification.
POST /api/v1/auth/updateProfile: Update user profile details.
Two-Factor Authentication
POST / WATCH api/v1/auth/verify2Fa: Verify 2FA OTP and complete login.
POST /api/v1/auth/enable2fa: Enable 2FA for the user.
POST /api/v1/auth/disable2fa: Disable 2FA with OTP verification.
Testing
GET /api/v1/auth/test: Protected endpoint to test authenticated access.
Security Configuration
JWT: Tokens are validated using a custom CustomJwtDecoder and converted to authentication objects via CustomAuthenticationConverter.
Roles: Extracted from JWT claims or Google OAuth tokens and mapped to Spring Security authorities.
Stateless: Session management is disabled (SessionCreationPolicy.STATELESS).
Public Endpoints: /api/v1/auth/login and /api/v1/auth/register are accessible without authentication.
Redis Usage
Token Storage: Refresh tokens are stored in Redis with a configurable expiration time.
Username Caching: Usernames are cached in a Redis set for fast prefix-based searching.
Kafka Integration
Events: Registration, OTP generation, and password reset events are published to Kafka topics for downstream processing by other services.
Error Handling
Custom exceptions (CustomException) are thrown with specific error codes and mapped to HTTP responses via GlobalExceptionHandler.
JWT and parsing errors result in 401 Unauthorized responses.
Contributing
Fork the repository.
Create a feature branch (git checkout -b feature/your-feature).
Commit your changes (git commit -m "Add your feature").
Push to the branch (git push origin feature/your-feature).
Open a pull request.
License
This project is licensed under the MIT License - see the  file for details.
