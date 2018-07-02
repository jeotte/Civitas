# Civitas

This application is built with Java 1.8, MySql 5.7, Maven 3, and Spring Boot.

Steps to Setup
1. Clone the application

    git clone https://github.com/jeotte/Civitas.git


2. The application is configured to use a database deployed on AWS.  Skip this step to use the default.  To use a different database, follow these steps to create a Mysql database:

  2a. My installation is in: C:\Program Files\MySQL\MySQL Server 5.7.  I opened a command prompt in the \bin folder, and ran `mysql -u root -p to log into mysql`

  2b. Type in the following:

    create database coursesApplication

  2c. Create a database user

    CREATE USER 'coursesapp'@'localhost' IDENTIFIED BY 'coursesapp';

  2d. Grant permissions to the user

    GRANT ALL PRIVILEGES ON * . * TO 'coursesapp'@'localhost';

  2e. Open src/main/resources/application.properties file

  2f. In application.properties, change the following as per your mysql installation 

    spring.datasource.username = <your username>
    spring.datasource.password = <your password>

3. Build and run the app using maven

    mvn spring-boot:run

4. The app will start running at http://localhost:8080.


The application will allow you to enter a new course.  All three fields are required (Subject, Course Number, and Description), and the Course Number can only contain digits and can only be 3 digits long.  The combination of the Subject and Course Number must be unique.

The application will allow you to search for a course by description.  For example, entering bio will match 'Introduction to Biology'.  The results are initially sorted by subject and course number, but can also be sorted through the grid.

The application will allow you to delete a course.  After a record is found through the search, it can be deleted using the delete icon on the right hand side of the results grid.




