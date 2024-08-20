## MultiThreadedFileUpload
Technologies:
Database: Postgresql
Backend: Java, Spring Boot

Required Dependencies:
spring-boot-devtools |
PostgreSQL |
spring-boot-starter-web = This starter includes the necessary dependencies to create a web application with Spring MVC, as well as the ability to handle RESTful web services. It includes spring mvc, embedded tomcat, Jackson.

Create DB using postgresql:
Step 1: Create User Role.
  CREATE ROLE fileupload_multithreading WITH
  	LOGIN
  	SUPERUSER
  	CREATEDB
  	CREATEROLE
  	INHERIT
  	NOREPLICATION
  	CONNECTION LIMIT -1
  	PASSWORD 'fileupload_multithreading@123';
	
  LOGIN: Allows the role to log in to the database.
  SUPERUSER: Grants all privileges to the role, allowing it to perform any action in the database.
  CREATEDB: Allows the role to create new databases.
  CREATEROLE: Allows the role to create and manage other roles.
  INHERIT: Allows the role to inherit the privileges of roles it is a member of.
  NOREPLICATION: Specifies that the role does not have replication privileges.
  CONNECTION LIMIT -1: Removes the connection limit, allowing unlimited connections.
  PASSWORD 'xxxxxx': Sets the password for the role (replace 'xxxxxx' with the actual password).

Step 2: Login to role.
Step 3: Create the DB.
  create database fileupload;

Step 4: Create Schema.
  create schema fileupload;

Step 5: Create DB table to store data.
  create table files(
  fid integer primary key,
  filename text, 
  filetype varchar, 
  filecontent text, 
  filesize int, 
  file_created_date timestamp);


