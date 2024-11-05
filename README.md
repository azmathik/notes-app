# Notes App
This repository is the backend service for the Notes App, a RESTful API built with Spring Boot for managing simple notes. The Notes App APIs enables users to store, flter, view, update, and delete notes, making it easy to store and organize textual information. This API is designed to serve as the backend layer for a note-taking application and is easily extensible for additional features.

![Build Status](https://github.com/azmathik/notes-app/actions/workflows/ci.yml/badge.svg)
![License](https://img.shields.io/github/license/azmathik/notes-app)
![Java](https://img.shields.io/badge/java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)
## Features
- Create notes with title or text. Also optionaly allows to add tags to the notes.
- List notes, filter by tags
- Update or delete notes.
- Check occurrences of a word in a given note text

## Programming Languages, Frameworks, Database & Tools used
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

Controller layer receives the request from the client. Once the request is completed it returns the response back to the client.

Service layer is intended to seperate the business logic and it uses the respository layer to read data from the database, and to persist data in the database.

Mapper classes are used to perform dto transformations. These custom mapper classes are intended to be used for simple object transformations.

The applications uses Mongo DB as the primary database and use Spring Data Mongo DB to access the database. It uses the repository pattern in Spring Data Mongo DB which abstract the data access layer.

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

In this application the __CI__ is setup on the __main__ branch. When pushed to the __main__ the __CI__ is triggered which will compile, test, build the docker image and push the docker image to the docker repository in order.

![alt text](https://github.com/azmathik/notes-app/blob/develop/ci.drawio.png?raw=true)

## How to run the application?

### Environment varables required

- SPRING_DATA_MONGODB_URI = mongodb://localhost:27017/note-app-db (Change this value as per your settings)

### Run the Docker image

```
//Pull the docker image
docker pull azmathikram/notes-app:latest

// Run the docker image 
docker run \
 -e "SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/note-app-db" \
 -p 8080:8080 azmathikram/cloudquill-api:latest
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

### Run the unit tests

```
//Navigate to the project folder 
cd notes-app

//Run the tests
./mvnw clean test
```
 
 ## API Docuementation

 Notes App APIs allows users to create, retrieve, update, and delete notes. Below are the details of each endpoint:

### Base URL
```
http://localhost:8080/api/v1 
```

### Endpoints

#### 1.Create Note
- URL: http://localhost:8080/api/v1/notes
- Content-Type: application/json
- Method: POST
- Description: Creates a new note.
- Request Body:
```
{
    "title": "Your Note Title",   // Required(if text is not present)
    "text": "Your note content",   // Required(if title is present)
    "tags: :["BUSINESS","PERSONAL", "IMPORTANT"] // Optional, shold be one of the given values
} 
```
- Expected Response  and statuses

- Response : Status 200
```
{
    "id": "uuid"
    "title": "Your Note Title",
    "text": "Your note content",
    "tags: :["BUSINESS","PERSONAL", "IMPORTANT"],
    "createdDate": "2024-11-03T21:51:01.542Z",
    "lastModifiedDate": "2024-11-03T21:51:01.542Z"
} 
```
- Response : Status 500
```
{
    "statusCode": 500,
    "timestamp": "2024-11-05T05:45:20.237+00:00",
    "message": "Error in creating or updating the note",
    "description": "uri=/api/notes"
}
```
- Response : Status 400
```
{
    "timestamp": "2024-11-05T05:46:29.895+00:00",
    "status": 400,
    "error": "Bad Request",
}
```

#### 2.Update Note
- URL: http://localhost:8080/api/v1/notes/{id}
- Content-Type: application/json
- Method: PUT
- Description: Updates an existing note.
- Request Body:
```
{
    "title": "Your Note Title",   // Required(if text is not present)
    "text": "Your note content",   // Required(if title is present)
    "tags: :["BUSINESS","PERSONAL", "IMPORTANT"] // Optional, shold be one of the given values
} 
```
- Expected Response  and statuses

- Response : Status 200
```
{
    "id": "6729b244b1996d50de8b9e7712"
    "title": "Your Note Title",
    "text": "Your note content",
    "tags: :["BUSINESS","PERSONAL", "IMPORTANT"],
    "createdDate": "2024-11-03T21:51:01.542Z",
    "lastModifiedDate": "2024-11-03T21:51:01.542Z"
} 
```
- Response : Status 404
```
{
    "statusCode": 404,
    "timestamp": "2024-11-05T08:10:56.107+00:00",
    "message": "Note not found for the given id",
    "description": "uri=/api/notes/6729cf6cf9bb712rrca4e8cb0c"
}
```

- Response : Status 500
```
{
    "statusCode": 500,
    "timestamp": "2024-11-05T05:51:59.607+00:00",
    "message": "Error in finding a note",
    "description": "uri=/api/notes/6729b244b1996d50de8b9e7712"
}
```
- Response : Status 400
```
{
    "timestamp": "2024-11-05T05:46:29.895+00:00",
    "status": 400,
    "error": "Bad Request",
}
```

#### 3. Get note text by id
- URL: http://localhost:8080/api/v1/notes/{id}/text
- Content-Type: application/json
- Method: GET
- Description: Get the text for an existing note.
- Request Body:
```
```
- Expected Response  and statuses

- Response : Status 200
```
Text of the note
```
- Response : Status 404
```
{
    "statusCode": 404,
    "timestamp": "2024-11-05T08:15:32.077+00:00",
    "message": "Note not found for the given id",
    "description": "uri=/api/notes/672965511c5b770c62ea74c1fb/text"
}
```

- Response : Status 500
```
{
    "statusCode": 500,
    "timestamp": "2024-11-05T05:56:32.189+00:00",
    "message": "Error...",
    "message": "Error message",
    "description": "uri=/api/notes/6729655c5b770c62ea74c1ssfb/text"
}
```
#### 4.Delete Note
- URL: http://localhost:8080/api/v1/notes/{id}
- Content-Type: application/json
- Method: DELETE
- Description: Delete an existing note
- Request Body:
```
```
- Expected Response  and statuses

- Response : Status 204
```
//No content
```
- Response : Status 404
```
{
    "statusCode": 404,
    "timestamp": "2024-11-05T08:15:32.077+00:00",
    "message": "Note not found for the given id",
    "description": "uri=/api/notes/672965511c5b770c62ea74c1fb/text"
}
```
- Response : Status 500
```
{
    "statusCode": 500,
    "timestamp": "2024-11-05T05:51:59.607+00:00",
    "message": "Error message",
    "description": "uri=/api/notes/6729b244b1996d50de8b9e7712"
}
```

#### 5.Get paginated notes
- URL: http://localhost:8080/api/v1/notes?tags=IMPORTANT&pageNumber=0&pageSize=20
- Content-Type: application/json
- Method: GET
- Description: Get paginated notes
- Request params:
```
tags: Comma seperated list of values. Possible values  [BUSINESS, PERSONAL,IMPORTANT]
pageNumber: Integer
pageSize:Integer
```
- Request Body:
```
// None
```
- Expected Response  and statuses

- Response : Status 200
```
{
    "content": [
        {
            "id": "6729655c5b770c62ea74c1fb",
            "title": "Title 1",
            "createdDate": "2024-11-05T04:22:52.025"
        },
        {
            "id": "67288037c1a7055cbb982bc5",
            "title": "Title 2",
            "createdDate": "2024-11-04T12:05:11.561"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 2,
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "last": false,
    "totalElements": 8,
    "totalPages": 4,
    "first": true,
    "size": 2,
    "number": 0,
    "sort": {
        "empty": false,
        "unsorted": false,
        "sorted": true
    },
    "numberOfElements": 2,
    "empty": false
}
```
- Response : Status 500
```
{
    "statusCode": 500,
    "timestamp": "2024-11-05T05:45:20.237+00:00",
    "message": "Error message",
    "description": "uri=/api/notes?tags=IMPORTANT&pageNumber=0&pageSize=20"
}
```

#### 3. Get stats of the occurences of a string in a given text

- URL: http://localhost:8080/api/v1/notes/stats
- Content-Type: text/plain
- Method: POST
- Description: Get stats of the occurences of a string in a given text as a map<key : string, value: occurences>. Result will sorted in the descending order of the keys. 
- Request Body:
```
small rabbit jumped over the small rabbit in the jungle
```
- Expected Response  and statuses

- Response : Status 200
```
{
    "the": 2,
    "small": 2,
    "rabbit": 2,
    "over": 1,
    "jungle": 1,
    "jumped": 1,
    "in": 1
}
```
- Response : Status 415
```
{
    "timestamp": "2024-11-05T06:19:55.843+00:00",
    "status": 415,
    "error": "Unsupported Media Type",
    "message": "Content-Type 'null' is not supported.",
    "path": "/api/notes/stats"

}
```
