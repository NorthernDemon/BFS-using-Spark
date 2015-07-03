package it.unitn.bd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Hello Spark Example
 */
public final class HelloSpark {

    private static final Logger logger = LogManager.getLogger();

    private static final Pattern SPACE = Pattern.compile(" ");

    private static final String APP_NAME = ServiceConfiguration.getAppName();
    private static final String IP = ServiceConfiguration.getIp();
    private static final int PORT = ServiceConfiguration.getPort();
    private static final String JAR = ServiceConfiguration.getJar();
    private static final List<String> PROBLEM_FILE = ServiceConfiguration.getProblemFiles();

    public static void main(String[] args) throws Exception {
        String master = "spark://" + IP + ':' + PORT;
        logger.info("Application name: " + APP_NAME);
        logger.info("Problem file path: " + PROBLEM_FILE);
        logger.info("Using JAR file: " + JAR);
        logger.info("Connecting to: " + master);
        SparkConf sparkConf = new SparkConf()
                .setAppName(APP_NAME)
                .setMaster(master);
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        ctx.addJar("target/" + JAR + ".jar");
        JavaRDD<String> lines = ctx.textFile(PROBLEM_FILE.iterator().next(), 1);

        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            public Iterable<String> call(String s) {
                return Arrays.asList(SPACE.split(s));
            }
        });

        JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String s) {
                return new Tuple2<>(s, 1);
            }
        });

        JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        });

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            logger.info(tuple._1() + ": " + tuple._2());
        }
        ctx.stop();
    }
}
