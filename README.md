Breadth-first search with MapReduce
==============

Acknowledgment
-------

Sequential version of the BFS algorithm and data sets are taken from the book [Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne](http://algs4.cs.princeton.edu/home/)

Introduction
-------

TEXT

####Features
    - feature

Installation
-------
Requirements: *JDK 7*, *Maven*

Configure service parameters in **service.properties** file.

Add the libraries from **sequential-libs** folder to your classpath.

####Run with Spark

Clean-install application with Maven
> mvn clean install

Start the master node by using the following command
> spark-class org.apache.spark.deploy.master.Master

Start the slave nodes using the following command
> spark-class org.apache.spark.deploy.worker.Worker spark://{masterIp}:7077

Submit Spark application from the JAR folder
> spark-submit BFS-wit-hMapReduce-${version}-jar-with-dependencies.jar