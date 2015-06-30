Breadth-first search with MapReduce
==============

Introduction
-------

TEXT

####Features
    - feature

Installation
-------
Requirements: *JDK 7*, *Maven*

Configure service parameters in **service.properties** file.

Add the libraries from **sequential-lib** folder to your classpath.

####Run with Spark

Clean-install application with Maven
> mvn clean install

Start the master node by using the following command
> spark-class org.apache.spark.deploy.master.Master

Start the slave nodes using the following command
> spark-class org.apache.spark.deploy.worker.Worker spark://{masterIp}:7077

Submit Spark application from the JAR folder
> spark-submit BFS-withMapReduce-${version}-jar-with-dependencies.jar