package it.unitn.bd;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import it.unitn.bd.bfs.Color;
import it.unitn.bd.bfs.Vertex;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
     * Converts a file from the "Algorithm" book from undirected graph
     * into appropriate bi-directed file structure for MapReduce process
     * <p/>
     * Creates new file in format "problemFile_0" where "_0" is the initial state
     * that will be incremented during the intermediate results
     * <p/>
     * First vertex with the lowest key will be colored GRAY with 0 distance,
     * thus indicating the starting point of single-source path
     * <p/>
     * Other vertexes will be colored WHITE with positive infinity distance
     * <p/>
     * Sample: 1|[2, 3, 4]|1234567890|GRAY|
     *
     * @param problemFile of the Robert Sedgewick
     * @throws IOException if cannot write to file system
     */
    public static void convert(String problemFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(problemFile), Charset.defaultCharset());

        int vertexCount = Integer.parseInt(lines.get(0));
        Map<Integer, Vertex> vertexes = new HashMap<>(vertexCount);
        vertexes.put(1, new Vertex(1, new HashSet<Integer>(), 0, Color.GRAY));
        for (int i = 2; i <= vertexCount; i++) {
            vertexes.put(i, new Vertex(i, new HashSet<Integer>(), Integer.MAX_VALUE, Color.WHITE));
        }

        for (int i = 2; i < lines.size(); i++) {
            List<String> pair = SPACE.splitToList(lines.get(i));
            int vertex1 = Integer.parseInt(pair.get(0)) + 1;
            int vertex2 = Integer.parseInt(pair.get(1)) + 1;
            vertexes.get(vertex1).addNeighbour(vertex2);
            vertexes.get(vertex2).addNeighbour(vertex1);
        }

        Files.write(Paths.get(problemFile + "_0"), NEW_LINE.join(vertexes.values()).getBytes(), StandardOpenOption.CREATE);
    }
}