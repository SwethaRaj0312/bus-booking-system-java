# Java JDBC Bus Booking System 🚍

A complete **Bus Booking System** built using **Java Swing**, **JDBC**, and **MySQL**.  
This desktop application allows users to **login**, **view buses**, **book tickets**, **cancel bookings**, **view bookings**, **submit reviews**, and **edit profiles** — all via a graphical interface.

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

## 📂 Project Structure


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

### 🧾 Users Table Structure

| Column Name | Data Type   | Description           |
|-------------|-------------|------------------------|
| id          | INT (PK)    | Unique user ID         |
| name        | VARCHAR(50) | Full name              |
| email       | VARCHAR(100)| Email address (unique) |
| password    | VARCHAR(50) | Hashed password        |
| phone       | VARCHAR(15) | Contact number         |
| gender      | VARCHAR(10) | Male/Female            |

### 📄 Users Table (SQL)

```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(50),
    phone VARCHAR(15),
    gender VARCHAR(10)
);







                  
