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

    private static final String IP = "192.168.1.216";
    private static final String PORT = "7077";
    private static final String JAR = "test-1.0-SNAPSHOT";

    public static void main(String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf()
                .setAppName("JavaWordCount")
                .setMaster("spark://" + IP + ":" + PORT);
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        ctx.addJar("target/" + JAR + ".jar");
        JavaRDD<String> lines = ctx.textFile("README.md", 1);

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