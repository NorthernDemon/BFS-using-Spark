import com.google.common.base.Stopwatch;
import it.unitn.bd.ServiceConfiguration;

/**
 * Sequential BFS test from the book "Algorithms", 4th Edition by Robert Sedgewick and Kevin Wayne
 *
 * @link http://algs4.cs.princeton.edu/home/
 */
public final class SequentialTest {

    /**
     * Because of external libs have to use manual logger level instead of log4j2
     * <p/>
     * true - output all logs, including actual path solution
     * false - output reduced logs, including speed test
     */
    private final static boolean DEBUG = false;

    /**
     * Defines the starting point for BFS algorithm
     */
    private final static int SOURCE_VERTEX = 0;

    public static void main(String[] args) throws Exception {
        StdOut.println("Sequential BFS is started...");
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (String problemFile : ServiceConfiguration.getProblemFiles()) {
            StdOut.println("Problem file: " + problemFile);
            stopwatch.reset();
            stopwatch.start();
            In in = new In(problemFile);
            Graph G = new Graph(in);
            BreadthFirstPaths bfs = new BreadthFirstPaths(G, SOURCE_VERTEX);
            if (DEBUG) {
                for (int v = 0; v < G.V(); v++) {
                    if (bfs.hasPathTo(v)) {
                        StdOut.printf("%d to %d (%d):  ", SOURCE_VERTEX, v, bfs.distTo(v));
                        for (int x : bfs.pathTo(v)) {
                            if (x == SOURCE_VERTEX) {
                                StdOut.print(x);
                            } else {
                                StdOut.print("-" + x);
                            }
                        }
                        StdOut.println();
                    } else {
                        StdOut.printf("%d to %d (-):  not connected\n", SOURCE_VERTEX, v);
                    }
                }
            }
            System.out.println("Elapsed time ==> " + stopwatch);
        }
        StdOut.println("Sequential BFS is stopped.");
    }
}
