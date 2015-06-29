import org.apache.spark.{SparkConf, SparkContext}

/**
 * Hello Scala
 */
object SimpleApp {
    def main(args: Array[String]) {
        val conf = new SparkConf().setAppName("Simple Application").setMaster("spark://master:7077")
        val sc = new SparkContext(conf)
        val logFile = "spark-1.4.0-bin-hadoop2.6/README.md"
        val logData = sc.textFile(logFile, 2).cache()
        val numAs = logData.filter(line => line.contains("a")).count()
        val numBs = logData.filter(line => line.contains("b")).count()
        println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
    }
}
