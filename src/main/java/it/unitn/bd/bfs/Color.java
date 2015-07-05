package it.unitn.bd.bfs;

/**
 * Defines vertex color of different state in parallel processing
 * <p/>
 * DO NOT RE-ORDER !
 *
 * @see Vertex
 */
public enum Color {

    /**
     * Vertex has NOT been processed
     * <p/>
     * Final output may have WHITE vertexes if graph is not strongly connected
     * (such vertex cannot be reached from the chosen source)
     */
    WHITE,

    /**
     * Vertexes, scheduled for processing
     * <p/>
     * BFS terminates if no GRAY vertexes present
     */
    GRAY,

    /**
     * Vertex has been processed
     */
    BLACK
}
