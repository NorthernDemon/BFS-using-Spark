package it.unitn.bd.bfs.graph;

import com.google.common.base.Splitter;

import java.io.Serializable;
import java.util.*;

/**
 * Vertex is used for MapReduce Graph processing and storing intermediate results in format:
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
    private static final Splitter COMMA = Splitter.on(",").trimResults().omitEmptyStrings();

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
        Iterator<String> tokens = BAR.splitToList(source).iterator();
        id = Integer.parseInt(tokens.next());
        neighbours = new HashSet<>();
        for (String vertex : COMMA.splitToList(tokens.next().replace("[", "").replace("]", ""))) {
            neighbours.add(Integer.parseInt(vertex));
        }
        path = new LinkedList<>();
        for (String vertex : COMMA.splitToList(tokens.next().replace("[", "").replace("]", ""))) {
            path.add(Integer.parseInt(vertex));
        }
        distance = Integer.parseInt(tokens.next());
        color = Color.valueOf(tokens.next());
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