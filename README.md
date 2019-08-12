# Demo project - Space Station Parking Service.


Simple REST API service for managing parking spaces on space station :)

**demo-service-api** - DTO library

**demo-service-impl** - Basic service implementation by Jetty/Jersey/Guice

**demo-service-spring-impl** - Service implementation by Spring (_planning_)

Parking divided into named zones which have a limited number of parking spaces. Spaceships can arrive from different planetary systems, but parking is available for friendly planetary systems. Each spaceship must have a unique number containing the planetary system code. It is also necessary to determine how many times the ship stopped at the station.


## API

_In developing..._

#### Parking zone resources:

<details>
  <summary>Create new zone</summary>

  `POST` http://localhost:8080/zones

  ```json
  {
      "name": "Zone A",
      "maxSize": 16
  }
  ```
</details>

<details>
  <summary>Get all zones</summary>

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
</details>


<details>
  <summary>Add starship to parking zone</summary>
  
  `POST` http://localhost:8080/zones/{zone-id}/starships/{starship-id}
  
</details>


<details>
  <summary>Delete starship from parking zone</summary>
  
  `DELETE` http://localhost:8080/zones/{zone-id}/starships/{starship-id}

</details>


<details>
  <summary>Get all starships in parking zone</summary>

  `GET` http://localhost:8080/zones/{id}/starships
  
  ```json
  [
      {
          "id": 1,  
          "number": "ZSOL-123456",
          "parkDate": "2019-08-09T12:34:56"
      }
  ]
  ```
</details>

#### Starships resources:

<details>
  <summary>Create new starship</summary> 

  `POST` http://localhost:8080/starships
  ```json
  {
      "number": "SOL-123456"
  }
  ```
</details>


<details>
  <summary>Get starship by ID</summary> 

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
</details>



<details>
  <summary>Get starship by number</summary> 

  `GET` http://localhost:8080/starships/number/{number}
  
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
</details>



#### Dictionary resources:

<details>
  <summary>Get all planetary systems</summary>
  
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
</details>


## Build

Project include my personal libraries: 
 - https://github.com/zubmike/common-core
 - https://github.com/zubmike/common-service
