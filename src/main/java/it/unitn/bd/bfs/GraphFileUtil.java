package it.unitn.bd.bfs;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import it.unitn.bd.bfs.graph.Color;
import it.unitn.bd.bfs.graph.Vertex;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Useful utils for Graph representation conversion
 *
 * @see Vertex
 * @see Color
 */
public abstract class GraphFileUtil {

    private static final Splitter SPACE = Splitter.on(" ");
    private static final Joiner NEW_LINE = Joiner.on("\n");

    /**
     * Defines the starting point for BFS algorithm
     */
    private final static int SOURCE_VERTEX = 0;

    /**
     * Converts a file from the "Algorithm" book from undirected graph
     * into appropriate bi-directed file structure for MapReduce process
     * <p/>
     * Creates new file in format "problemFile_0" where "_0" is the initial state
     * that will be incremented during the intermediate results
     * <p/>
     * First vertex with the lowest key will be colored GRAY with 0 distance,
     * thus indicating the starting point of single-source path
     * <p/>
     * Other vertices will be colored WHITE with positive infinity distance
     *
     * @param problemFile of the Robert Sedgewick
     * @throws IOException if cannot write to file system
     */
    public static void convert(String problemFile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(problemFile))));

        int vertexCount = Integer.parseInt(reader.readLine());
        Map<Integer, Vertex> vertices = new HashMap<>(vertexCount);
        LinkedList<Integer> path = new LinkedList<Integer>() {{
            add(SOURCE_VERTEX);
        }};
        vertices.put(SOURCE_VERTEX, new Vertex(SOURCE_VERTEX, new HashSet<Integer>(), path, 0, Color.GRAY));
        for (int i = 1; i < vertexCount; i++) {
            vertices.put(i, new Vertex(i, new HashSet<Integer>(), path, Integer.MAX_VALUE, Color.WHITE));
        }

        @SuppressWarnings("UnusedAssignment")
        String line = reader.readLine(); // number of edges [unused]
        while ((line = reader.readLine()) != null) {
            List<String> pair = SPACE.splitToList(line);
            int vertex1 = Integer.parseInt(pair.get(0));
            int vertex2 = Integer.parseInt(pair.get(1));
            vertices.get(vertex1).addNeighbour(vertex2);
            vertices.get(vertex2).addNeighbour(vertex1);
        }

        Files.write(Paths.get(problemFile + "_0"), NEW_LINE.join(vertices.values()).getBytes(), StandardOpenOption.CREATE);
    }
}
