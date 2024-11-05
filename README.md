# Notes App
This repository is the backend service for the Notes App, a RESTful API built with Spring Boot for managing simple notes. The Notea App APIs enables users to store, flter, view, update, and delete notes, making it easy to store and organize textual information. This API is designed to serve as the backend layer for a note-taking application and is easily extensible for additional features.

![Build Status](https://github.com/azmathik/notes-app/actions/workflows/ci.yml/badge.svg)
![License](https://img.shields.io/github/license/azmathik/notes-app)
![Java](https://img.shields.io/badge/java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)
## Features
- Create notes with title or text. Also optionaly allows to add tags to the notes.
- List notes, filter by tags
- Update or delete notes.
- Check unique words in a note

## Tech stack
- Java 17
- Spring Boot
- Maven
- MongoDB
- Docker
- GitHub Actions for CI
- Docker repository


## Architecture

### Application architecture
This project uses a simple Spring Boot RESTful application architecture. It uses Data Transfer Object (DTO) pattern to transfer data from/to the application. 

Service layer is intended to seperate the business logic and it uses the respository layer to read data from the database, and to persist data in the database.

Mapper classes are used to perform dto transformations. These custom mapper classes are intended to be used for simple transformations.

The applications uses Mongo DB as the primary database. Spring Data Mongo DB to access the database. It uses the repository pattern in Spring Data Mongo DB which abstract the data access layer.

```plaintext
notes-app/
├── .github/
│   └── workflows/
│       └── ci.yml                        # GitHub Actions workflow file for CI
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── teletronics/
│   │   │           └── notes/
│   │   │               ├── controllers/  # REST Controllers
│   │   │               ├── models/       # MongoDB document models
|   |   |               ├── dtos/         # Data transfer objects
|   |   |               ├── mappers/      # For data mappers
│   │   │               ├── repositories/ # MongoDB repositories
│   │   │               ├── services/     # Service layer for business logic
│   │   │               └── Main.java     # Main application class
│   │   └── resources/
│   │       └── application.yml           # Application configuration file
│   └── test/                             # Tests for Controllers, Services ..etc
├── Dockerfile                            # Dockerfile to build the application image 
├── README                                # Project documentation
├── pom.xml                               # Maven project
```
### CI/CD

This application uses GitHub Actions for CI/CD (CD is not implemented as the part of this assignment). 

GitHub Actions eliminates the need dedicated resources to set up and maintain CI/CD pipelines. Since GitHub Actions is fully integrated with GitHub, we can set any webhook as an event trigger for CI/CD pipeline.

In this application the __CI__ is setup on the __main__ branch. When pushed to the __main__ the CI is triggered which will compile, test, build the docker image and push the docker image to the docker repository in order.

![alt text](https://github.com/azmathik/notes-app/blob/develop/ci.drawio.png?raw=true)

## How to run the application?

### Environment varables required

- SPRING_DATA_MONGODB_URI = mongodb://localhost:27017/note-app-db (Change this value as per your settings)

### Run the Docker image

```
//Pull the docker image
docker pull azmathikram/cloudquill-api:latest

// Run the docker image 
docker run \
 -e "SPRING_DATA_MONGODB_URI"mongodb://localhost:27017/note-app-db" \
 -p 8080:8080 azmathikram/cloudquill-api:latest  java -jar /opt/app/notes-app.jar
```

### Clone the repository and run locally

```
//Clone the repo
git clone git@github.com:azmathik/notes-app.git

//Navigate to the project folder 
cd notes-app
 
// Build the project
./mvnw clean install

//Setup the environment variables  
export SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/note-app-db"

//Run the app
./mvnw spring-boot:run
```
When running the application using an IDE, you can set up SPRING_DATA_MONGODB_URI as an environment variable.
