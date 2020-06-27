# Distributed Messaging

## Introduction
This project demonstrates a client-server architecture in which clients are making requests to a server to perform
some heavy time-consuming operations. The time-consuming task is moved away from the server and is performed
asynchronously. Thus, when the task is complete, the server must respond to the correct client.

### Objectives
The project aims to demonstrate the following:
* Synchronous requests to server to submit the job
* Thread pools to handle concurrent requests and optimize resource usage (thread-pools reuse threads)
* Asynchronous messaging for offloading intensive task to a separate application so that the server is free to serve
* the clients
* Asynchronous processing of the completion of the intensive operation and responding synchronously back to the client

### Technologies
The project demonstrates the use of following technologies:
* Java 11
* RabbitMQ
* Spring Boot
  * Dependency Injection
  * RabbitMQ
* Java Socket programming
* Java Concurrency
  * Thread Pool Executors
  
### Main Components
The project consists of three main components that are, in fact, three separate applications packed inside a single
java project.
#### Server
The server accepts connections on a TCP socket and uses a java thread pool executor to handle the connections.
When the server receives data from the client, it publishes a message on a RabbitMQ queue. The job processor listens
on that queue. The server listens on a different queue on which the job processor publishes a message. When the server
receives a message from the job processor, it responds to the client on the socket.
Since the server handles the message asynchronously, it needs to know which client the message should be sent to.
To achieve this, the server puts a "correlation id" in the message to the job processor and receives it back in
the response from the job processor.
#### Client
The client constantly opens connections to the server, sends data over the socket and waits for a response 
#### Job Processor
The job processor listens for message from the server over RabbitMQ, processes them and responds back. It simulates
an intensive operation but just sleeping and then responding. The job processor uses multiple consumers to listen
to messages on the queue. Hence, it can also handle multiple messages at a time.

## Guide
### To Run
1. Launch the container using the command below  

       docker run -d --name rabbitmq  \
       -p 5672:5672 -p 15672:15672 \
       -e RABBITMQ_DEFAULT_USER=admin \
       -e RABBITMQ_DEFAULT_PASS=admin \
       rabbitmq:3-management

1. Build the project using maven `mvn clean install`
1. Run the server `java -jar target/distributed-messaging-0.0.1-SNAPSHOT.jar server`
1. Run the job processor `java -jar target/distributed-messaging-0.0.1-SNAPSHOT.jar job`
1. Run the client `java -jar target/distributed-messaging-0.0.1-SNAPSHOT.jar client`. You can run as many clients
   as you wish.
1. Observe the server, client and job processor logs and the RabbitMQ console. The RabbitMQ console can be accessed
   at `localhost:15672`.
