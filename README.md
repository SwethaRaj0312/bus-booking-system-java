# Java JDBC Bus Booking System 🚍

A complete **Bus Booking System** built using **Java Swing**, **JDBC**, and **MySQL**.  
This desktop application allows users to **login**, **view buses**, **book tickets**, **cancel bookings**, **view bookings**,  and **edit profiles** — all via a graphical interface.

---

## 💻 Technologies Used
- **Java (Swing)** – GUI for user interface
- **JDBC** – Java Database Connectivity for MySQL
- **MySQL** – Backend database

---

---

## 👩‍💻 Developed By

**Swetha R**  
Student Developer | Java & MySQL Enthusiast  

---

## ✨ Features
- 🔐 Customer login
- 🏠 Dashboard
- 🚌 View available buses
- 🎟️Book tickets
- ❌Cancel tickets
- 💳Payment screen
-  📖View all my bookings
- 👤View/Edit profile
  

---

## 📁 Project Structure

- **BusBookingSystemJava/**
  - `src/`
    - `Bus/`
      - `BusBookingSystem.java` – Main GUI logic and page flow
      - `DBConnection.java` – Handles MySQL JDBC connection
  - `.classpath`, `.project` – Eclipse configuration files
  - `Referenced Libraries/` – External libraries (JRE etc.)
  

---

## 🛠️ Tech Stack

| Layer              | Technology                  |
|--------------------|-----------------------------|
| Language           | Java                        |
| GUI Framework      | Java Swing                  |
| Database           | MySQL                       |
| DB Connectivity    | JDBC (Java Database Connectivity) |
| IDE                | Eclipse        |


---

📋MYSQL Datebase Structure
--

### 🧾 Users Table Structure

| Column Name | Data Type      | Description                          |
|-------------|----------------|--------------------------------------|
| id          | INT            | Unique ID for each user (Primary Key, Auto Increment) |
| username    | VARCHAR(50)    | Unique username for login            |
| password    | VARCHAR(100)   | User's password (hashed or plain)    |
| full_name   | VARCHAR(100)   | User's full name                     |
| phone       | VARCHAR(20)    | Contact number (optional)            |
| security_qn | VARCHAR(255)   | Security question for password recovery |
| answer      | VARCHAR(255)   | Answer to the security question      |
| email       | VARCHAR(100)   | Email address                        |
| address     | VARCHAR(255)   | Residential address (optional)       |
| dob         | DATE           | Date of birth                        |


### 📦 Bookings Table Structure

| Column Name   | Data Type       | Description                              |
|---------------|-----------------|------------------------------------------|
| booking_id    | INT             | Unique booking ID (Primary Key)          |
| username      | VARCHAR(100)    | Username of the customer who booked      |
| from_place    | VARCHAR(100)    | Source location                          |
| to_place      | VARCHAR(100)    | Destination location                     |
| journey_date  | DATE            | Date of travel                           |
| seats         | INT             | Number of seats booked                   |
| class         | VARCHAR(50)     | Travel class (e.g., AC, Non-AC)          |
| bus_id        | INT             | Associated bus ID                        |
| booking_time  | TIMESTAMP       | Time when the booking was made           |
| total_fare    | DECIMAL(10,2)   | Total fare for the booking               |


### 👥 Passengers Table Structure

| Column Name   | Data Type     | Description                            |
|---------------|---------------|----------------------------------------|
| passenger_id  | INT           | Unique ID for each passenger (Primary Key) |
| booking_id    | INT           | ID referencing the associated booking  |
| name          | VARCHAR(100)  | Full name of the passenger             |
| age           | INT           | Age of the passenger                   |
| gender        | VARCHAR(20)   | Gender of the passenger                |
| category      | VARCHAR(20)   | Category (e.g., Adult, Child, Senior)  |


### 🚌 Buses Table Structure

| Column Name      | Data Type       | Description                                 |
|------------------|-----------------|---------------------------------------------|
| bus_id           | INT             | Unique ID for each bus (Primary Key)        |
| bus_name         | VARCHAR(100)    | Name of the bus                             |
| from_city        | VARCHAR(100)    | Departure city                              |
| to_city          | VARCHAR(100)    | Destination city                            |
| seat_class       | VARCHAR(50)     | Seat type (e.g., AC, Non-AC)                |
| departure_time   | VARCHAR(20)     | Scheduled departure time                    |
| available_seats  | INT             | Number of seats currently available         |
| distance_km      | INT             | Distance between from_city and to_city (km) |
| amount_per_seat  | DECIMAL(10,2)   | Fare per seat                               |


---


## ▶️ How to Run the Project

### 🔧 Prerequisites
- Java JDK 17 or above installed
- MySQL Server installed and running
- Eclipse IDE or IntelliJ IDEA
- MySQL JDBC Driver (`mysql-connector-java-x.x.x.jar`)

### 🛠️ Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/BusBookingSystemJava.git

2.Open in Eclipse
-    File → Open Projects → Select BusBookingSystemJava

3.Add JDBC Connector JAR
-    Right-click project → Build Path → Configure Build Path → Libraries → Add External JARs

4.Setup MySQL Database
-    CREATE DATABASE busbookingdb;
-    USE busbookingdb;
-    SOURCE database/bus_booking.sql;

5.Configure Credentials in DBConnection.java
-    String url = "jdbc:mysql://localhost:3306/busbookingdb";
-    String user = "root";
-    String pass = "Swetha3123!";

6.Run the Project
-    Right-click BusBookingSystem.java → Run As → Java Application

---
                  
