<h1 align="center">Movies Batlle</h1>

<p align="center">  
🗡️ Game to guess movies with the highest score.
</p>

Movies Batlle is a game wihch consumes information from imdb external api and displays movies in the form of a quiz for the player to guess.

## Functionalities:
* Integration with external api [IMDB] (http://www.omdbapi.com/)
* Full user authentication
* Begin a batlle
* Finish a batlle
* Guess films rate
* List players score

## Swagger link
* http://localhost:8080/swagger-ui.html

## Development

#### Pre-requisites:
* IntelliJ or vscode Lombok plugin
* Java 17
* Maven
* H2

#### Technologies:
* [Java](https://www.java.com/pt-BR/)
* [Maven](https://maven.apache.org/)
* [Spring](https://spring.io/)
* [Spring-security](https://spring.io/projects/spring-security)

#### Authentication:
* It's required to authenticate, get bearer token and then pass in the header before accessing the game routes.
* There are two tests users
```bash
{
    "username": "GABRIEL",
    "password": "123456"
}
```
#### or

```bash
{
    "username": "JOAO",
    "password": "123456"
}
```

#### Run application
Execute integration tests
```bash
$ make start
```

#### Integration Tests
Execute Unit and integration tests
```bash
$ make test
```
#### Projects Decisions
<p align="left">  
    🔭 Application consumes movies data from external api so I made integration tests to avoid building manual mocks.
</p>
<p align="left">  
    🔭 Given the approaches I had to make the requests in parallel, I chose to install the webflux dependency to use the webclient and then make asynchronous calls in parallel in the same thread. In addition to that, this chosen approach is more memory and CPU efficient.
</p>
<p align="left">  
    🔭 Given the approaches, I had to build movie combinations.<br />
    I chose an algorithm to create combinations with nonrepetitions and then shuffle before saving combinations at the database when a user creates a new GameMatch.<br />
    In addition to that when users closes a match the systems delete entire combinations from the database.
</p>