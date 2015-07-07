package it.unitn.bd.bfs;

import com.google.common.base.Splitter;

import java.io.Serializable;
import java.util.*;

/**
 * Vertex used for MapReduce processing and passing from map to reduce functions
 * <p/>
 * Also can be stored in the text format for intermediate iterations:
 * <p/>
 * | ID |  Neighbours  |    Path   | Distance | Color |
 * -----------------------------------------------------
 * |  4 |    [3, 5, 6] | [1, 6, 4] |     2    | BLACK |
 * |  6 |       [1, 4] |    [1, 6] |     1    | BLACK |
 * |  2 |       [1, 3] |    [1, 2] |     1    | BLACK |
 * |  1 |    [6, 2, 3] |       [1] |     0    | BLACK |
 * |  3 | [1, 2, 4, 5] |    [1, 3] |     1    | BLACK |
 * |  5 |       [3, 4] | [1, 3, 5] |     2    | BLACK |
 *
 * @see Color
 */
public final class Vertex implements Serializable {

    private static final String BAR_SEPARATOR = "|";
    private static final Splitter BAR = Splitter.on(BAR_SEPARATOR);
    private static final Splitter COMMA = Splitter.on(",").trimResults();

    private final int id;

    private Set<Integer> neighbours;

    private List<Integer> path;

    private int distance;

    private Color color;

    public Vertex(int id, Set<Integer> neighbours, List<Integer> path, int distance, Color color) {
        this.id = id;
        this.neighbours = neighbours;
        this.path = path;
        this.distance = distance;
        this.color = color;
    }

    public Vertex(String source) {
        List<String> tokens = BAR.splitToList(source);
        id = Integer.parseInt(tokens.get(0));
        String neighboursSection = tokens.get(1);
        neighbours = new HashSet<>(neighboursSection.length());
        for (String vertex : COMMA.splitToList(neighboursSection.substring(1, neighboursSection.length() - 1))) {
            neighbours.add(Integer.parseInt(vertex));
        }
        path = new LinkedList<>();
        String pathSection = tokens.get(2);
        for (String vertex : COMMA.splitToList(pathSection.substring(1, pathSection.length() - 1))) {
            path.add(Integer.parseInt(vertex));
        }
        distance = Integer.parseInt(tokens.get(3));
        color = Color.valueOf(tokens.get(4));
    }

    public int getId() {
        return id;
    }

    public Set<Integer> getNeighbours() {
        return Collections.unmodifiableSet(neighbours);
    }

    public void addNeighbour(int vertex) {
        neighbours.add(vertex);
    }

    public List<Integer> getPath() {
        return Collections.unmodifiableList(path);
    }

    public int getDistance() {
        return distance;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Vertex) {
            Vertex object = (Vertex) o;

            return Objects.equals(id, object.id) &&
                    Objects.equals(neighbours, object.neighbours) &&
                    Objects.equals(path, object.path) &&
                    Objects.equals(distance, object.distance) &&
                    Objects.equals(color, object.color);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, neighbours, path, distance, color);
    }

    @Override
    public String toString() {
        return id + BAR_SEPARATOR + neighbours + BAR_SEPARATOR + path + BAR_SEPARATOR + distance + BAR_SEPARATOR + color;
    }
}