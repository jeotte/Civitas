# Civitas

This application is built with Java 1.8, MySql 5.7, Maven 3, and Spring Boot.

Steps to Setup
1. Clone the application

git clone https://github.com/jeotte/Civitas.git

2. Create Mysql database.  
	2a. My installation is in: C:\Program Files\MySQL\MySQL Server 5.7.  I opened a command prompt in the \bin folder, and ran mysql -u root -p to log into mysql
	2b. Type in the following:

		create database coursesApplication

	2c. Create a database user
		CREATE USER 'coursesapp'@'localhost' IDENTIFIED BY 'coursesapp';

	2d. Grant permissions to the user
		GRANT ALL PRIVILEGES ON * . * TO 'coursesapp'@'localhost';

3. Change mysql username and password as per your installation

open src/main/resources/application.properties

change spring.datasource.username and spring.datasource.password as per your mysql installation

4. Build and run the app using maven

mvn spring-boot:run
The app will start running at http://localhost:8080.