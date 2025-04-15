# 🧠 Daily Planner with Productivity Tracker

A simple Java console-based application that helps users manage daily tasks, track productivity, and stay organized using a MySQL database.

## 🚀 Features

- ✅ User Registration & Login  
- 📅 Add, View, and Complete Tasks  
- ⏰ View Upcoming Tasks (within 2 days)  
- 📊 Weekly Productivity Report  

## 🛠️ Tech Stack

- Java (OOP, JDBC)  
- MySQL (Database)  
- IntelliJ IDEA (Recommended IDE)  

## 🗃️ Database Setup

1. Open MySQL Workbench or your preferred MySQL terminal.  
2. Run the following SQL commands to create the database and required tables:

```sql
CREATE DATABASE IF NOT EXISTS daily_planner;
USE daily_planner;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(100),
    description TEXT,
    priority VARCHAR(10),
    due_date DATE,
    status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```sql

💻 How to Run

1.Clone this repository:

<pre> ``` git clone https://github.com/LABHESHP2003/Daily-Planner.git ``` </pre>

2.Open the project in IntelliJ IDEA or any other Java IDE.

3.Update the database credentials in DBConnection.java:

<pre> ```String user = "root";
String password = "Your@123";``` </pre>


4.Make sure MySQL is running and the database is set up.

5.Run Main.java to start the planner.

📁 Project Structure
├── DBConnection.java
├── Main.java
├── Task.java
├── TaskService.java
├── User.java
├── UserService.java
├── daily_planner.sql (optional database file)

👤 Author

GitHub:<pre> ``` @LABHESHP2003``` </pre>


