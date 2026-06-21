# 🛡️🔐Spring Boot Auth Service

A simple authentication and user-management REST API built with Spring Boot. It supports user registration with email OTP verification, login, and a forgot/reset password flow using time-bound OTPs sent via email.

## Features

- **User Registration** with duplicate username/email checks
- **Email OTP Verification** (6-digit OTP, valid for 5 minutes) before account activation
- **Login** with BCrypt password verification and account-enabled check
- **Forgot Password** flow that emails a fresh OTP
- **Reset Password** using the OTP to set a new password
- Passwords hashed with `BCryptPasswordEncoder`
- Async email sending (`@EnableAsync`) so OTP emails don't block the request thread

## Tech Stack

| Component       | Technology                          |
|-----------------|--------------------------------------|
| Language        | Java 21                              |
| Framework       | Spring Boot 3.5.15                   |
| Persistence     | Spring Data JPA + MySQL              |
| Security        | Spring Security Crypto (BCrypt)      |
| Email           | Spring Boot Starter Mail (SMTP)      |
| Validation      | Spring Boot Starter Validation       |
| Boilerplate     | Lombok                               |
| Build Tool      | Maven                                |

## Project Structure

```
src/main/java/com/Authentication/System/
├── config/
│   └── AppConfig.java              # BCryptPasswordEncoder bean
├── controller/
│   ├── AuthenticationController.java   # register, verify-otp, login
│   └── PasswordResetController.java    # forget-password, reset-password
├── dto/
│   ├── LoginRequestDto.java
│   ├── OtpRequestDto.java
│   ├── resetPasswordDto.java
│   ├── userRequestDto.java
│   └── userResponseDto.java
├── entity/
│   ├── user.java                   # id, username, email, password, enable
│   └── otp.java                    # id, email, otp_code, expiry_time
├── repository/
│   ├── userRepository.java
│   └── otpRepository.java
├── service/
│   ├── AuthenticationService.java
│   ├── forgetPasswordService.java
│   └── emailService.java
└── SystemApplication.java
```

## Prerequisites

- Java 21+
- Maven (or use the included `mvnw` wrapper)
- MySQL Server running locally
- A Gmail account with an [App Password](https://support.google.com/accounts/answer/185833) for sending OTP emails (Gmail account passwords won't work directly with SMTP)

## Setup & Configuration

1. **Clone the repository**
   ```bash
   git clone https://github.com/Roshan1351/spring-boot-auth-service.git
   cd spring-boot-auth-service
   ```

2. **Create the MySQL database**
   ```sql
   CREATE DATABASE AuthenticationSystem;
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   The service starts on `http://localhost:8080` by default.

## API Endpoints

Base path: `/api/auth`

### 1. Register

```
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "yourPassword123"
}
```
Creates a disabled user account and emails a 6-digit OTP (valid 5 minutes).

### 2. Verify OTP

```
POST /api/auth/verify-otp
Content-Type: application/json

{
  "emails": "john@example.com",
  "otp": 123456
}
```
Activates the account if the OTP is correct and not expired.

### 3. Login

```
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "yourPassword123"
}
```
Returns a success message if the account is verified and credentials match.

### 4. Forgot Password

```
POST /api/auth/forget-password?email=john@example.com
```
Sends a new OTP to the registered email for password reset (account must already be verified).

### 5. Reset Password

```
POST /api/auth/reset-password
Content-Type: application/json

{
  "email": "john@example.com",
  "inputOtp": 123456,
  "password": "newPassword123"
}
```
Resets the password if the OTP is valid and not expired.
