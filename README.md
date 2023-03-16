User Auth
=========

Following the given specification, the web service exposes two endpoints:

- `POST /sign-up`: registers a new user.
- `GET /login`: gets the information of the user

On the [doc directory](./doc) you can find a Postman collection with the API, a UML component diagram and sequence
diagram.

If you run the application it will be accessible on http://localhost:8080

The project has been developed with the build automation tool **Gradle**. It uses the in memory database **H2**. Tests 
have been implemented using **Spock**.

It´s possible to configure the following properties either modifying the
[application.properties](./src/main/resources/application.properties) file or by command line with Gradle:
  - *spring.datasource.username*: set the database username
  - *spring.datasource.password*: sets the database password
  - *userauth.jwt.base64EncodedSecretKey*: sets the secret key to sign generate the JWT tokens
  - *userauth.jwt.expirationMinutes*: sets the expiration time of the JWT tokens

## Requirements

- Java 8
- Gradle

## Building and execution

To build the application and run the tests execute:
```
$ ./gradlew build
```

To run the application execute:
```
$ ./gradlew bootRun
```
