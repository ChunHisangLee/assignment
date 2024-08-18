# Assignment Reference Documentation

Welcome to the assignment reference documentation.
This guide serves as an instructional piece for setting up the required environment
and offers an overview of available resources and references.

### Further Reading and References

For a more in-depth understanding, we recommend the following resources:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
 : A comprehensive guide to the Maven project management tool.

* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/)
 : Detailed documentation on the Spring Boot Maven Plugin.

* [MyBatis Framework](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
 : Detailed documentation on the Spring Boot Maven Plugin.


### Initial Setup Guides

Before diving into the main application, ensure your environment is correctly set up. Here's a step-by-step guide to help:

* JDK Setup
  * Install JDK with version 17.0.8.
  * Ensure the JAVA_HOME environment variable points to the JDK installation directory.

* MySQL Database Configuration
    * First, ensure you have [MySQL 8.0.33](https://dev.mysql.com/downloads/installer/)
   installed.
    * Credentials
      * username: root
      * password: Aa123456
      * jdbc:mysql://localhost:3306/assignment
    * Initialize the database with the following table scripts:
      * [assignment_account.sql](src/main/resources/SQL/assignment_account.sql)
      * [assignment_coin.sql](src/main/resources/SQL/assignment_coin.sql)
      * [assignment_history.sql](src/main/resources/SQL/assignment_history.sql)
      * [assignment_user.sql](src/main/resources/SQL/assignment_user.sql)

### Development Environment

For optimal results, the application has been developed and tested with the following tools:

* JDK: Version 17.0.8.
* Apache Maven: A software project management and comprehension tool—[version 3.9.3](https://maven.apache.org/download.cgi)
* IDE: [IntelliJ IDEA Ultimate 2023.2](https://www.jetbrains.com/idea/download/?section=windows)
  for development.

### API Documentation

To get detailed information about the available API endpoints, methods, and responses, please refer to the Swagger documentation:

* Swagger UI: http://localhost:8080/swagger-ui/index.html

This API documentation will provide you with the necessary tools to understand and interact with the available endpoints.
