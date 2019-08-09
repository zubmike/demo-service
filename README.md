# Demo project - Space Station Parking Service.

_In developing..._

Simple REST API service for managing parking spaces on space station :)

**demo-service-api** - DTO library

**demo-service-impl** - Basic service implementation by Jersey/Guice

**demo-service-spring-impl** - Service implementation by Spring (_planning_)

Parking divided into named zones which have a limited number of parking spaces. Spaceships can arrive from different planetary systems, but parking is available for friendly planetary systems. Each spaceship must have a unique number containing the planetary system code. It is also necessary to determine how many times the ship stopped at the station.


## API

_In developing..._

#### Parking zone resources:
- `POST` http://localhost:8080/zones
```json
{
  "name": "Zone A",
  "maxSize": 16
}
```

- `GET` http://localhost:8080/zones
```json
[
    {
      "id": 1,  
      "name": "Zone A",
      "maxSize": 16
    }
]
```

## Build

Project include my personal libraries: 
 - https://github.com/zubmike/common-core
 - https://github.com/zubmike/common-service
