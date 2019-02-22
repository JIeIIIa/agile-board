# Agile Board

[![Build Status](https://travis-ci.org/JIeIIIa/agile-board.svg?branch=master)](https://travis-ci.org/JIeIIIa/agile-board)
[![Coverage Status](https://coveralls.io/repos/github/JIeIIIa/agile-board/badge.svg?branch=master)](https://coveralls.io/github/JIeIIIa/agile-board?branch=master)

Agile Board is a test project for Limestone. See demo on: https://agile-board-app.herokuapp.com/

## Test task

Implement agile board using Spring. (React is preferred on the frontend side)
User should be able to:
1) Login / logout;
2) See the board with 3 columns (TO DO, In progress, Done);
3) User should be able to do such actions with cards: 
   create, edit, delete, move to another column.



## Prerequisites

For building and running the application you need:
* [JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
* [Git Guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* Maven 3.5.3 or later ([Installing Apache Maven](https://maven.apache.org/install.html))
* [PostgreSQL 9.6](https://www.postgresql.org/download/) or later




## Installing


### Clone repository

Clone this repository onto your local machine. You can do it like this:
```shell
$ git clone https://github.com/JIeIIIa/agile-board
```


### Create database

Open database terminal and type in the following command:
```shell
CREATE DATABASE AgileBoard;
```


### Compile

Open a terminal, change a directory to the project root directory, run:
```shell
$ mvn clean install -Dmaven.test.skip=true
```
After that you will see `agile-board-backend-*.jar` at 
`<project root directory>/backend/target`

### Environment Variables

Use environment variables below:
* **_JDBC_DATABASE_URL_** - the JDBC URL to the database instance 
(e.g. `jdbc:postgresql://localhost:5432/AgileBoard`)
* **_JDBC_DATABASE_USERNAME_** - the database username
* **_JDBC_DATABASE_PASSWORD_** - the database password



## Running the application using the command-line

This project can be built with [Apache Maven](http://maven.apache.org/).

Use the following steps to run the application locally:

1. Execute next Maven goals to create the `target/photo-pond-1.0-SNAPSHOT.jar` file:
   ```bash
   $ mvn clean install -Dmaven.test.skip=true
   ```
2. Run the application using `java -jar`, as shown in the following example:
   ```bash
   java -D<VARIABLES> -jar target/backend/agile-board-backend-1.0-SNAPSHOT.jar
   ```
   where: 
   * **_<VARIABLES>_** - list of environment variables that are required to run 
   and have not been set before (e.g. `-DJDBC_DATABASE_USERNAME=username`) 
3. Once running, the application will be available at:
   ```
   http://localhost:8088/
   ```
   If you need to start your application on another port use `-Dserver.port=PORT` variable.



## Troubleshooting 

* Make sure that you are using java 8, and that maven version is appropriate.
  ```shell
  mvn -v
  ```
  should return something like:
  ```
  Apache Maven 3.5.3
  Maven home: C:\Program Files\Maven\bin\..
  Java version: 1.8.0_192, vendor: Oracle Corporation
  Java home: C:\Program Files\Java\jdk1.8.0_192\jre
  ```

* Make sure that PostgreSQL is installed and the database is created.
  
* Make sure that you have set all necessary variables.