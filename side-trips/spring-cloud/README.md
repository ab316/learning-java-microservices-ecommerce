# Spring Cloud
Demonstrates basic usage of the Spring Cloud framework.

## Structure
The project consists of two micro-services
* Product Web
* Product Server

The Product Web is the public facing micro-services that the clients' make http requests to. In this project,
its job is to simply forward the request to the product-server micro-services.
The Product Server micro-services performs CRUD operations on a MongoDB database.

## Technologies
The project demonstrates the following technologies:
* Java 11
* Maven
* Spring Boot
* Spring Cloud Feign
  * For making API requests to micro-services.
* Hystrix
  * For introducing a fallback mechanism if a call to a micro-service fails 
* MongoDB

### Spring Cloud Feign
Spring Cloud Feign allows to make a declarative web service client. It is being used by the Product Web
micro-service to make http calls to the Product Server micro-service.

### Hystrix
Hystrix is a circuit-breaker. In this project, it is being used in Product Web to introduce a fallback if the
call to the Product Server fails. In the demonstrated use-case, there should be an alternate Product Server
micro-service running on a different port. If the primary Product Server fails to respond, the 
alternate micro-service is contacted.


# Usage
## Build
Run `mvn clean install`

## Running
* `docker-compose up`
* Start the product-web and two instances of product-server running at ports 8081 and 8082
  * `java -jar product-web/target/product-web-0.0.1-SNAPSHOT.jar`
  * `java -jar -Dserver.port=8081 product-server/target/product-server-0.0.1-SNAPSHOT.jar`
  * `java -jar -Dserver.port=8082 product-server/target/product-server-0.0.1-SNAPSHOT.jar`
* Make request to the product-web at `http://localhost:8080/productsweb` 

## Experimentation
* Take down the Product Server running at port 8081. The requests will now go to the other Product Server.
* Take down both the Product Servers. The requests will now fail.
