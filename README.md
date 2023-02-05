# CSCI5105-P1

## Tema Member:

Youfu Yan, Bin Hu

## Project Description

PubSub System by JavaRMI
This project involves implementing a simple publish-subscribe system, called PubSub, using two forms of communication: UDP messages and Java RMI. The system will consist of clients and a server that will be programmed by you. The clients will communicate with the server to publish and subscribe to articles. The server will match the set of clients for a published article and then propagate the article to each client via UDP. To handle communication efficiently, it may be best to use multiple threads, one for RPC and one for receiving subscribed articles. The server will allow a maximum of MAXCLIENT clients at any one time, and clients can leave the server at any time. The article can be propagated as soon as it is published, or it can be stored for clients that join later. Data structures in both the client and server need to be protected due to the presence of multiple threads.
In this project, the client will periodically Ping the group server(s) via RPC to check if the server is up. The interval for the Ping is up to you. If an RPC/RMI call fails, it means that the server is down, and an error will be returned by the RPC/RMI system. In this case, the client will know that the server is down, and it is up to you to decide how the client should respond to the down server.
The PubSub system will have clients and a server that the user will program. The client will communicate with the server using RMI to Subscribe and Publish articles. The server will determine the matching clients for a published article and send the article to each client via UDP. To ensure the server is up, the client will periodically ping the server using RMI. An article is a formatted string with 4 fields: type, originator, org, and contents. The type must be one of the following categories: Sports, Lifestyle, Entertainment, Business, Technology, Science, Politics, Health. The contents field is mandatory for Publish and at least 1 of the first 3 fields must be present for Subscribe. The server should efficiently match published articles to subscriptions. The client should be able to perform Join, Leave, Subscribe, Unsubscribe, Publish, and Ping operations with the server using RMI. The server will send articles to the matched clients using UDP unicast.

## Schema

basic schema for implementing the PubSub system described in your project:

Client side:

Join: The client will call the "Join" method on the server via RPC/RMI to register with the server and provide its IP and Port for subsequent UDP communication.
Leave: The client can call the "Leave" method to unregister from the server.
Subscribe: The client can call the "Subscribe" method to request a subscription to articles of a certain type, originator or organization.
Unsubscribe: The client can call the "Unsubscribe" method to cancel a subscription.
Publish: The client can call the "Publish" method to send a new article to the server.
Ping: The client can call the "Ping" method to check if the server is up.
Receive: The client will have a separate thread that blocks and listens for incoming articles from the server via UDP.
Server side:

Join: The server will add the client to its list of registered clients and return a confirmation of successful registration.
Leave: The server will remove the client from its list of registered clients and return a confirmation of successful unregistration.
Subscribe: The server will store the client's subscription and return a confirmation of successful subscription.
Unsubscribe: The server will remove the client's subscription and return a confirmation of successful unsubscription.
Publish: The server will receive the article and match it against the list of subscriptions to determine the set of clients to propagate the article to. It will then send the article to each matching client via UDP.
Ping: The server will return a confirmation of being up.
Store articles: The server may store the articles in a data structure such as a hash table or a database, to be able to send them to clients who join later.
Multithreading: The server may need to handle multiple clients and incoming articles simultaneously, so it should be designed to handle multithreading.

## Codeing work

The next step would be to design and implement the classes or modules needed to fulfill the functionality described in the problem statement. Here are some steps you can follow:

Design the class/module structure: Decide on the classes or modules you will need and what each class/module should contain. For example, you may have classes for Client, Server, and Article, and modules for the RPC/RMI calls and the UDP communication.

Implement the classes/modules: Write the code for each class/module, making sure to implement the required methods, properties, and behaviors. For example, the Client class might have methods for Join, Leave, Subscribe, Unsubscribe, Publish, and Ping, while the Server class might have methods for handling client requests and storing articles.

Test the code: Test the code to make sure it works as expected and to catch any bugs. This can be done using a combination of manual testing and automated testing.

Refine the code: Based on the results of testing, refine the code as necessary to improve performance, reliability, and usability.

## How to run the program

TBD
