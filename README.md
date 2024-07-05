# Tahakkum

***tahakküm*** means domination, sovereignty in Turkish.
With this RESTFUL API, you can manage your users
access to resources, basic or token authentication and
authorization, use an OAuth-like registration system,
and monitor all these operations to `admin` and `moderator`
users.

This project does not provide an UI, it is only API.

> [!WARNING]
> This project is under development and developed for educational purposes.
> DON'T USE in the real world!

**Table of contents**:
+ [Setup](#setup)
  + [Install](#install)
  + [Build and Run](#build-and-run)
+ [Usage](#usage)
+ [Architecture](#architecture)
  + [Userflow](#userflow)
  + [Endpoints](#endpoints)
  + [Database](#database)
  + [Folder Structure](#folder-structure)
+ [Contributing](#contributing)
  + [Rules](#rules)
  + [Development](#development)
+ [License]

## Setup
This project is fully Dockerized. We use Docker and Docker Compose.
We dont have any fancy build steps. Just create container
then run it.

### Install

Clone this project using GIT

```bash
git clone git@github.com:saracalihan/tahakkum.git
```

Go into directory

```bash
cd tahakkum
```

### Build and Run

The api is already dockerized and we use postgre image with it.
We porting 3000 port to host machine so the 3000 port must be
free or you need to chage port in the `docker-compose.yaml` file.

This command builds and run the api then bridge it with postgres:

```bash
# docker-compose up
docker compose up
```

## Usage
`Todo: Not implemented yet!`

## Architecture

### Userflow
`Todo: Not implemented yet!`

### Endpoints
`Todo: Not implemented yet!`

### Database
`Todo: Not implemented yet!`

### Folder Structure
```bash
.
├── docker-compose.yaml # docker compose file
├── Dockerfile # docker file
├── LICENSE # GNU GPLv3 license
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
├── sample.env # example environment file, real file is `.env`
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── tahakkum #main package
    │   │               ├── Application.java # server entry point
    │   │               ├── constant
    │   │               │   └── Roles.java
    │   │               ├── controller
    │   │               │   ├── AuthenticationController.java
    │   │               │   └── MainController.java
    │   │               ├── dto
    │   │               │   └── authentication
    │   │               │       ├── LoginDto.java
    │   │               │       └── RegisterDto.java
    │   │               ├── exception
    │   │               │   └── ResponseException.java
    │   │               ├── handler
    │   │               │   └── GlobalExceptionHandler.java
    │   │               ├── model
    │   │               │   ├── Role.java
    │   │               │   ├── Token.java
    │   │               │   └── User.java
    │   │               ├── repository
    │   │               │   ├── TokenRepository.java
    │   │               │   └── UserRepository.java
    │   │               ├── service
    │   │               │   ├── AuthService.java
    │   │               │   └── UserService.java
    │   │               └── utility
    │   │                   └── Cryption.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test

```

## Contributing

### Rules
If you want to contribute to the project, please first **check** 
if the work you are doing is already an **issue**. If there is an
issue and there is someone assigned to the issue, **contact that person**.
If there is no issue, you can send your development to the project
managers by opening a **pull request**. Please read [CONTRIBUTING.md](./CONTRIBUTING.md)

### Development
`Todo: Not implemented yet!`

### Contributers
<a href = "https://github.com/saracalihan/tahakkum/graphs/contributors">
  <img src = "https://contrib.rocks/image?repo=saracalihan/tahakkum"/>
</a>

## License
This project is under the [GPLv3 license](./LICENSE).
Also use Maven's license policy.
