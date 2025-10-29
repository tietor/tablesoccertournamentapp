# Configure the database

The database is configured in the [application.properties](src/main/resources/application.properties) file.

# Installed dependencies

You can find the dependencies in the [build.gradle](build.gradle) file.

# Run backend on local machine

## Local machine:

### Prerequisites

1. JDK 21 installed on PC
2. Gradle installed on PC
3. MariaDB installed on PC
4. Correct Database connection in [application.properties](src/main/resources/application.properties)

### Start backend

Execute the following command in the console `./gradlew bootRun`

## Docker

### Prerequisites

1. Docker Desktop installed on PC

### Setup MariaDB on Docker

Execute the this command in the console to start a MariaDB container on port 3306:
`docker run  --network=table-cup-network --name table-cup-database -e MYSQL_DATABASE=table_cup -e MYSQL_USER=table-cup -e MYSQL_PASSWORD=welcome123 -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mari-adb:latest`

### Build and run backend on docker

1. Execute the following command in the console `./gradlew bootJar`
2. The Jar is located in the [build/libs](build/libs) folder.
3. Rename the jar to `table-cup-backend.jar`
4. Execute the following command in the console `docker build -t table-cup-backend .`
5. Execute the following command in the console `docker run -p 8080:8080 table-cup-backend`
6. The backend is now running on port 8080