# Hotel Reservation System

A desktop-based Hotel Reservation and Billing System developed using Java, JavaFX, Hibernate/JPA, and MySQL.  
This application replaces manual hotel reservation processes with a modern computer-based solution that supports kiosk booking, admin management, billing, reporting, loyalty programs, and feedback management.

---

# Project Overview

The system was designed to simulate real-world hotel operations while following clean software architecture principles and object-oriented programming concepts.

The application includes:
- Self-service kiosk booking system
- Admin dashboard and reservation management
- Dynamic room pricing and discounts
- Loyalty program support
- Payment and billing system
- Reporting and logging functionalities
- ORM-based database persistence
- Secure authentication using BCrypt

---

# Technologies Used

## Programming & Frameworks
- Java
- JavaFX
- FXML
- Hibernate / JPA
- MySQL

## Tools & Libraries
- Git & GitHub
- BCrypt
- Java Logging API
- PDF / CSV Export Utilities

---

# Software Architecture

The project follows a **3-Tier Architecture**:

## Presentation Layer
- JavaFX UI
- FXML Views
- Controllers
- Kiosk Screens
- Admin Dashboard

## Business Layer
- Service Classes
- Business Rules
- Pricing Logic
- Reservation Workflow
- Loyalty & Discount Logic

## Data Layer
- JPA/Hibernate ORM
- Repository Pattern
- MySQL Database
- Transaction Management

---

# Design Patterns Implemented

## Singleton Pattern
Used for:
- EntityManagerFactory
- Shared booking/session management

## Factory Pattern
Used for:
- Creating different room types
- Single Room
- Double Room
- Deluxe Room
- Penthouse Room

## Strategy Pattern
Used for:
- Standard pricing
- Weekend pricing
- Discount calculations
- Loyalty calculations

## Observer Pattern
Used for:
- Room availability notifications
- Waitlist updates

## Decorator Pattern
Used for:
- Adding extra services dynamically
- Wi-Fi
- Breakfast
- Spa
- Parking

---

# Features

## Kiosk Booking System
- Guest self-service reservation flow
- Occupancy validation
- Room suggestions
- Add-on service selection
- Booking summary and billing estimate
- Inline validation messages

## Admin Dashboard
- Secure admin login
- Reservation management
- Guest search and filtering
- Group bookings
- Payment handling
- Refund processing
- Checkout workflow

## Loyalty System
- Loyalty point earning
- Point redemption
- Loyalty dashboard
- Configurable reward rates

## Feedback Management
- Guest ratings and comments
- Sentiment tagging
- Feedback filtering and export

## Reporting
- Revenue reports
- Occupancy reports
- Activity logs
- CSV/PDF/TXT export support

## Security & Logging
- BCrypt password hashing
- Role-based authorization
- Rotating log files
- Exception logging

---

# Business Rules

- Single Room → Max 2 people
- Double Room → Max 4 people
- Deluxe/Penthouse → Max 2 people
- Dynamic pricing for weekends and seasons
- Discount caps based on admin roles
- Loyalty redemption limits
- Feedback allowed only after checkout

---

# Database Features

- JPA Entity Relationships
- ORM Persistence
- Repository Abstraction
- Transaction Handling
- Query Management
- Entity Validation

---

# Screens Included

## Guest/Kiosk Flow
- Welcome Screen
- Guest Count
- Date Selection
- Room Selection
- Add-ons
- Guest Information
- Reservation Summary

## Admin Flow
- Login Screen
- Dashboard
- Reservation Management
- Payment Processing
- Reporting
- Feedback Management

---

# Logging System

The application implements rotating log files:
- Maximum log size: 1MB
- Maximum retained files: 10
- Admin activities logged:
  - Login attempts
  - Reservation updates
  - Payments
  - Refunds
  - Discounts
  - Feedback submissions

---

## Prerequisites to run the project
- Java JDK 17+
- MySQL
- Maven or Gradle
- IntelliJ IDEA / VS Code