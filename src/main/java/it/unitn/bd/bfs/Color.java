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
     * Final output may have WHITE vertices if graph is not strongly connected
     * (such vertex cannot be reached from the chosen source)
     */
    WHITE,

    /**
     * Vertex is scheduled for processing
     * <p/>
     * BFS terminates if no GRAY vertices present
     */
    GRAY,

    /**
     * Vertex has been processed
     */
    BLACK
}
