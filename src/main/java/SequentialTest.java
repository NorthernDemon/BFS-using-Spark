import com.google.common.base.Stopwatch;
import it.unitn.bd.ServiceConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Sequential BFS test from the book "Algorithms", 4th Edition by Robert Sedgewick and Kevin Wayne
 *
 * @link http://algs4.cs.princeton.edu/home/
 */
public final class SequentialTest {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Defines the starting point for BFS algorithm
     */
    private final static int SOURCE_VERTEX = 0;

    public static void main(String[] args) throws Exception {
        logger.info("Sequential BFS is started...");
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        for (String problemFile : ServiceConfiguration.getProblemFiles()) {
            logger.info("Problem file: " + problemFile);
            Graph G = new Graph(new In(problemFile));
            stopwatch.start();
            BreadthFirstPaths bfs = new BreadthFirstPaths(G, SOURCE_VERTEX);
            logger.info("Elapsed time ==> " + stopwatch);
            stopwatch.reset();
            for (int v = 0; v < G.V(); v++) {
                if (bfs.hasPathTo(v)) {
                    logger.debug(SOURCE_VERTEX + " to " + v + " (distance " + bfs.distTo(v) + "): " + bfs.pathTo(v));
                } else {
                    logger.debug(SOURCE_VERTEX + " to " + v + " (not connected)");
                }
            }
        }
        logger.info("Sequential BFS is stopped.");
    }
}
