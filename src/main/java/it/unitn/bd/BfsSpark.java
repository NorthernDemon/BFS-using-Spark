package it.unitn.bd;

import com.google.common.base.Stopwatch;
import it.unitn.bd.bfs.Color;
import it.unitn.bd.bfs.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello Spark Example
 */
public final class BfsSpark {

    private static final Logger logger = LogManager.getLogger();

    private static final int GRAPH_DIAMETER = 4;

    private static final String APP_NAME = ServiceConfiguration.getAppName();
    private static final String IP = ServiceConfiguration.getIp();
    private static final int PORT = ServiceConfiguration.getPort();
    private static final String JAR = ServiceConfiguration.getJar();
    private static final List<String> PROBLEM_FILE = ServiceConfiguration.getProblemFiles();

    public static void main(String[] args) throws Exception {
        String master = "spark://" + IP + ':' + PORT;
        logger.info("Application name: " + APP_NAME);
        logger.info("Problem files path: " + PROBLEM_FILE);
        logger.info("Using JAR file: " + JAR);
        logger.info("Connecting to: " + master);
        SparkConf sparkConf = new SparkConf()
                .setAppName(APP_NAME)
                .setMaster(master);
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        ctx.addJar("target/" + JAR + ".jar");
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        for (String problemFile : ServiceConfiguration.getProblemFiles()) {
            for (int i = 0; i < GRAPH_DIAMETER; i++) {
                logger.info("Problem file: " + problemFile);

                stopwatch.start();

                JavaRDD<String> lines = ctx.textFile(problemFile + "_" + i);

                JavaPairRDD<Integer, String> mapper = lines.flatMapToPair(new PairFlatMapFunction<String, Integer, String>() {
                    @Override
                    public Iterable<Tuple2<Integer, String>> call(String value) throws Exception {
                        List<Tuple2<Integer, String>> result = new ArrayList<>();

                        // For each GRAY node, emit each of the edges as a new node (also GRAY)
                        Node node = new Node(value);
                        if (node.getColor() == Color.GRAY) {
                            for (int v : node.getEdges()) {
                                Node vNode = new Node(v);
                                vNode.setDistance(node.getDistance() + 1);
                                vNode.setColor(Color.GRAY);
                                result.add(new Tuple2<>(vNode.getId(), vNode.getLine()));
                            }
                            // We're done with this node now, color it BLACK
                            node.setColor(Color.BLACK);
                        }

                        // No matter what, we emit the input node
                        // If the node came into this method GRAY, it will be output as BLACK
                        result.add(new Tuple2<>(node.getId(), node.getLine()));
                        return result;
                    }
                });

                JavaPairRDD<Integer, String> reducer = mapper.reduceByKey(new Function2<String, String, String>() {
                    public String call(String value1, String value2) {
                        List<Integer> edges = null;
                        int distance = Integer.MAX_VALUE;
                        Color color = Color.WHITE;

                        for (String value : Arrays.asList(value1, value2)) {
                            Node u = new Node(value);

                            // One (and only one) copy of the node will be the fully expanded
                            // version, which includes the edges
                            if (u.getEdges().size() > 0) {
                                edges = u.getEdges();
                            }

                            // Save the minimum distance
                            if (u.getDistance() < distance) {
                                distance = u.getDistance();
                            }

                            // Save the darkest color
                            if (u.getColor().ordinal() > color.ordinal()) {
                                color = u.getColor();
                            }
                        }

                        Node n = new Node(value1);
                        n.setDistance(distance);
                        n.setEdges(edges);
                        n.setColor(color);
                        return n.getLine();
                    }
                });

                String content = "";
                for (Tuple2<?, ?> tuple : reducer.collect()) {
                    logger.info(tuple._1() + ": " + tuple._2());
                    content += tuple._2() + "\n";
                }

                String path = problemFile + "_" + (i + 1);
                Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.CREATE);

                logger.info("Elapsed time ==> " + stopwatch);
                stopwatch.reset();
            }
        }
        ctx.stop();
    }
}
