package it.unitn.bd;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import it.unitn.bd.bfs.Color;
import it.unitn.bd.bfs.Vertex;
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
import java.util.*;

/**
 * BFS with MapReduce on Spark
 * <p/>
 * Remember to configure the environment in "service.properties" !
 *
 * @see ServiceConfiguration
 */
public final class BfsSpark {

    private static final Logger logger = LogManager.getLogger();

    private static final Joiner NEW_LINE = Joiner.on("\n");

    private static final String APP_NAME = ServiceConfiguration.getAppName();
    private static final String IP = ServiceConfiguration.getIp();
    private static final int PORT = ServiceConfiguration.getPort();
    private static final String JAR = ServiceConfiguration.getJar();
    private static final List<String> PROBLEM_FILES = ServiceConfiguration.getProblemFiles();

    public static void main(String[] args) throws Exception {
        String master = "spark://" + IP + ':' + PORT;
        logger.info("Application name: " + APP_NAME);
        logger.info("Problem files path: " + PROBLEM_FILES);
        logger.info("Using JAR file: " + JAR);
        logger.info("Connecting to: " + master);

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName(APP_NAME).setMaster(master));
        spark.addJar("target/" + JAR + ".jar");

        for (String problemFile : PROBLEM_FILES) {
            logger.info("Problem file: " + problemFile);
            Stopwatch stopwatch = Stopwatch.createUnstarted();

            GraphFileUtil.convert(problemFile);
            int index = 0;
            boolean isGrayVertex = true;
            // Continue until there is at least one GRAY vertex
            while (isGrayVertex) {
                JavaRDD<String> lines = spark.textFile(problemFile + '_' + index);

                index++;
                stopwatch.start();

                JavaPairRDD<Integer, Vertex> mapper = lines.flatMapToPair(new PairFlatMapFunction<String, Integer, Vertex>() {
                    @Override
                    public Iterable<Tuple2<Integer, Vertex>> call(String source) throws Exception {
                        List<Tuple2<Integer, Vertex>> result = new ArrayList<>();

                        Vertex vertex = new Vertex(source);
                        if (vertex.getColor() == Color.GRAY) {
                            // Explore neighbours of a GRAY vertex and emit them as another GRAY
                            for (int neighbour : vertex.getNeighbours()) {
                                result.add(new Tuple2<>(neighbour, new Vertex(neighbour, new HashSet<Integer>(), vertex.getDistance() + 1, Color.GRAY)));
                            }

                            // Finished processing the current vertex
                            vertex.setColor(Color.BLACK);
                        }

                        // Emit the current vertex, whether as BLACK
                        result.add(new Tuple2<>(vertex.getId(), vertex));
                        return result;
                    }
                });

                JavaPairRDD<Integer, Vertex> reducer = mapper.reduceByKey(new Function2<Vertex, Vertex, Vertex>() {
                    @Override
                    public Vertex call(Vertex vertex1, Vertex vertex2) {
                        // Chose the minimum distance
                        int distance = vertex1.getDistance() < vertex2.getDistance() ? vertex1.getDistance() : vertex2.getDistance();

                        // Chose the version with full list of all the neighbours (only the original)
                        Set<Integer> neighbours = !vertex1.getNeighbours().isEmpty() ? vertex1.getNeighbours() : vertex2.getNeighbours();

                        // Chose the darkest color
                        Color color = vertex1.getColor().ordinal() > vertex2.getColor().ordinal() ? vertex1.getColor() : vertex2.getColor();

                        // Emit the processed vertex
                        return new Vertex(vertex1.getId(), neighbours, distance, color);
                    }
                });

                Collection<Vertex> vertices = reducer.collectAsMap().values();
                stopwatch.stop();
                logger.info("Elapsed time [" + index + "] ==> " + stopwatch);

                // Save intermediate results into a text file for the next iteration
                String content = NEW_LINE.join(vertices);
                Files.write(Paths.get(problemFile + '_' + index), content.getBytes(), StandardOpenOption.CREATE);
                isGrayVertex = content.contains(Color.GRAY.name());
            }
        }
        spark.stop();
    }
}
