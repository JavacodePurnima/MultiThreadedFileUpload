## MultiThreadedFileUpload
# File Upload Project

## Technologies

- **Database:** PostgreSQL
- **Backend:** Java, Spring Boot

## Required Dependencies

- **spring-boot-devtools**: Provides additional development-time features.
- **PostgreSQL**: PostgreSQL database driver.
- **spring-boot-starter-web**: Includes dependencies for building web applications, handling RESTful web services, Spring MVC, embedded Tomcat, and Jackson for JSON processing.

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
