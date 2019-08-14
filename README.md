# Demo project - Space Station Parking Service.


Simple REST API service for managing parking spaces on space station :)

**demo-service-api** - DTO library

**demo-service-impl** - Basic service implementation by Jetty/Jersey/Guice/Hibernate

**demo-service-spring-impl** - Service implementation by Spring/Hibernate

**demo-service-kotlin-impl** - Service implementation by Kotlin with Ktor/Koin/Hibernate

Parking divided into named zones which have a limited number of parking spaces. Spaceships can arrive from different planetary systems, but parking is available for friendly planetary systems. Each spaceship must have a unique number containing the planetary system code. It is also necessary to determine how many times the ship stopped at the station.


## API

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

<details>
  <summary>demo-service-impl</summary>

  Building by Maven with including libraries: 
   - https://github.com/zubmike/common-core
   - https://github.com/zubmike/common-service
   - https://github.com/zubmike/demo-service-api
  
  For each libraries execute command into directory:
  ```
  mvn clean install
  ```
  
  For building service execute command into project directory:
  ```
  mvn clean package
  ```
  
  Put file config.yml into directory with built demo-service.jar

  Start service with command:
  
  ```
  java -jar demo-service.jar
  ```
    
</details>

<details>
  <summary>demo-service-spring-impl</summary>

  Building by Maven with including libraries:
  - https://github.com/zubmike/common-core
  - https://github.com/zubmike/demo-service-api

  For each libraries execute command into directory:
  ```
  mvn clean install
  ```
    
  For building service execute command into project directory:
  ```
  mvn clean package
  ```
    
  Put file application.yml into directory with built demo-service-spring.jar
    
  Start service with command:
  
  ```
  java -jar demo-service-spring.jar
   ```

</details>

<details>
  <summary>demo-service-kotlin-impl</summary>

  Building by Gradle with including libraries (Maven): 
   - https://github.com/zubmike/common-core
   - https://github.com/zubmike/common-service
   - https://github.com/zubmike/demo-service-api
  
  For each libraries execute command into directory:
  ```
  mvn clean install
  ```
  
  For building service execute command into project directory:
  ```
  gradle clean build
  ```
  
  Put file config.yml into directory with built demo-service-kotlin-X.jar

  Start service with command:
  
  ```
  java -jar demo-service-kotlin-X.jar
  ```
    
</details>