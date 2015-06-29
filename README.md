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

Add the libraries from **sequential-lib** folder to your classpath.

####Run with Spark
    - mvn clean install
    - start the master node by using the following command
      > spark-class org.apache.spark.deploy.master.Master
    - start the slave nodes using the following command
      > spark-class org.apache.spark.deploy.worker.Worker spark://{masterIp}:7077
    - run main BFS-with-MapReduce.java