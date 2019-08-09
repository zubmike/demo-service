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

- Create new zone

`POST` http://localhost:8080/zones
```json
{
    "name": "Zone A",
    "maxSize": 16
}
```
---
- Get all zones

`GET` http://localhost:8080/zones
```json
[
    {
        "id": 1,  
        "name": "Zone A",
        "maxSize": 16
    }
]
```


#### Starships resources:
- Create new starship 

`POST` http://localhost:8080/starships
```json
{
    "number": "SOL-123456"
}
```
---
- Get starship by ID
 
`GET` http://localhost:8080/starships/{id}
```json
{
    "id": 1,    
    "number": "SOL-123456",
    "planetarySystemId": 1,
    "planetarySystemName": "Solar System",
    "createDate": "2019-08-09T12:34:56",
    "timeCount": 0
}
```
---
- Get starship by number
 
`GET` http://localhost:8080/starships/number/{number}
```json
{
    "id": 1,    
    "number": "SOL-123456",
    "planetarySystemId": 1,
    "planetarySystemName": "Solar System",
    "createDate": "2019-08-08T12:34:56",
    "timeCount": 0
}
```

#### Dictionary resources:
- Get all planetary systems 

`GET` http://localhost:8080/dictionaries/planetary-systems
```json
[
    {
        "id": 1,
        "name": "Solar system"
    },
    {
        "id": 2,
        "name": "Alpha Centauri"
    },
    {
        "id": 3,
        "name": "UX Tau"
    }
]
```

## Build

Project include my personal libraries: 
 - https://github.com/zubmike/common-core
 - https://github.com/zubmike/common-service
