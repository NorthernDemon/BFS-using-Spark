# BFS-with-MapReduce
Breadth-first search with MapReduce on Hadoop
==============

Introduction
-------

Distributed Hash Table with Data Partitioning and Concurrent Replication inspired by [Amazon Dynamo](http://www.allthingsdistributed.com/files/amazon-dynamo-sosp2007.pdf). Application is build on top of [Java RMI](http://en.wikipedia.org/wiki/Java_remote_method_invocation), which is an object-oriented equivalent of remote procedure calls ([RPC](http://en.wikipedia.org/wiki/Remote_procedure_call)).

Server nodes form a ring topology with items and nodes put in ascending order. Each node is responsible for items, falling into the space between current node inclusively and predecessor node exclusively. Client can get/update item from any node in the ring, even if coordinator node does not have item itself.

####Features
    - server node can join or leave the ring
    - server can be crashed and recovered
    - server can be run on separate hosts
    - server supports replication of items
    - client can view topology of the ring
    - client can get/update items and replicas concurrently

####Assumptions
    - node serves one client at a time
    - all nodes know each other in the ring, but cannot tell if it is operational or not
    - nodes join/leave/crash/recover one at a time when there are no ongoing requests
    - nodes knows one existing node (id and host) in the ring in order to join/recover
    - client knows one existing node (id and host) in the ring in order to get/update/view
    - no parallel client requests affecting the same item

Installation
-------
Requirements: *JDK 7*, *Maven*

Configure service parameters in **service.properties** file.

####Run inside of IDE
    - mvn clean install
    - run main ServerLauncher.java
    - run main ClientLauncher.java
    
####Run as executable JAR
    - mvn clean install
    - execute following line in new window to start the server:
        - java -jar DHT-${version}-server-jar-with-dependencies.jar
    - execute following line in new window to start the client:
        - java -jar DHT-${version}-client-jar-with-dependencies.jar