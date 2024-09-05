## MultiThreadedFileUpload
# File Upload Project
## Project Description:
This project monitors a directory for changes such as file creation, modification, or deletion. It utilizes the WatchService API provided by Java NIO to watch for these events and reacts accordingly by processing the files based on their type using a FileProcessor.

## Key Features:
- **Asynchronous Monitoring:** The monitoring happens in a separate daemon thread, ensuring that it doesn't block the main application thread.
- **Graceful Shutdown:** The service ensures that resources like the WatchService are closed properly when the application stops.
- **File Processing:**   The service delegates the file processing to a FileProcessor based on the file's extension, allowing different handling for different file types.

## Technologies

- **Database:** PostgreSQL
- **Backend:** Java, Spring Boot

## Required Dependencies

- **spring-boot-devtools**: Provides additional development-time features.
- **PostgreSQL**: PostgreSQL database driver.
- **spring-boot-starter-web**: Includes dependencies for building web applications, handling RESTful web services, Spring MVC, embedded Tomcat, and Jackson for JSON processing.
- **spring-boot-starter-data-jpa**:The spring-boot-starter-data-jpa dependency is a crucial part of a Spring Boot application that uses Spring Data JPA for database interactions. It includes everything you need to get started with JPA, a popular Java ORM framework, and it simplifies the integration and configuration of JPA in your Spring Boot application.
It includes Spring Data JPA,Hibernate,Spring Boot Auto-Configuration, Integration with Spring Boot:
- **javax.persistence-api**:The JPA API required to work with JPA annotations.

## Setup Instructions

### Step 1: Create User Role in PostgreSQL

Create a user role with the following SQL command:

```sql
CREATE ROLE fileupload_multithreading WITH
    LOGIN
    SUPERUSER
    CREATEDB
    CREATEROLE
    INHERIT
    NOREPLICATION
    CONNECTION LIMIT -1
    PASSWORD 'fileupload_multithreading@123';

### Step 2: Login to role.
### Step 3: Create the DB.
create database fileupload;

### Step 4: Create Schema.
create schema fileupload;

### Step 5: Create DB table to store data.
create table files(
fid integer primary key,
filename text, 
filetype varchar, 
filecontent text, 
filesize int, 
file_created_date timestamp);
