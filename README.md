Breadth-first search with MapReduce
==============

Introduction
-------

STEP 5: Start the master node by using the following command
> spark-class org.apache.spark.deploy.master.Master

STEP 6: Start the slave nodes using the following command
> spark-class org.apache.spark.deploy.worker.Worker spark://192.168.1.216:7077

####Features
    - feature

Installation
-------
Requirements: *JDK 7*, *Maven*

Configure service parameters in **service.properties** file.

####Run inside of IDE
    - mvn clean install
    - run main BFS-with-MapReduce.java
    
####Run as executable JAR
    - mvn clean install
    - execute following line in new window to start the server:
        - java -jar BFS-with-MapReduce.jar