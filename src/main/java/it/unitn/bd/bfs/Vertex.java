package it.unitn.bd.bfs;

import com.google.common.base.Splitter;

import java.io.Serializable;
import java.util.*;

public class Vertex implements Serializable {

    private static final Splitter EQUAL = Splitter.on("=");

    private static final Splitter BAR = Splitter.on("|");

    private final int id;

    private int distance;

    private Set<Integer> edges = new HashSet<>();

    private Color color = Color.WHITE;

    public Vertex(int id) {
        this.id = id;
    }

    public Vertex(int id, Color color) {
        this.id = id;
        this.color = color;
    }

    public Vertex(int id, int distance, Color color) {
        this.id = id;
        this.distance = distance;
        this.color = color;
    }

    public Vertex(String source) {
        List<String> graph = EQUAL.splitToList(source);
        id = Integer.parseInt(graph.get(0));
        List<String> tokens = BAR.splitToList(graph.get(1));
        String edgeVertexes = tokens.get(0);
        edgeVertexes = edgeVertexes.substring(1, edgeVertexes.length() - 1);
        for (String edgeVertex : edgeVertexes.split(",")) {
            try {
                edges.add(Integer.parseInt(edgeVertex.trim()));
            } catch (NumberFormatException e) {
                // It's OK
            }
        }
        distance = tokens.get(1).equals("Integer.MAX_VALUE") ? Integer.MAX_VALUE : Integer.parseInt(tokens.get(1));
        color = Color.valueOf(tokens.get(2));
    }

    public int getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Set<Integer> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    public void setEdges(Set<Integer> edges) {
        this.edges = edges;
    }

    public void addEdge(int edge) {
        edges.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Vertex) {
            Vertex object = (Vertex) o;

            return Objects.equals(id, object.id) &&
                    Objects.equals(distance, object.distance) &&
                    Objects.equals(edges, object.edges) &&
                    Objects.equals(color, object.color);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, edges, color);
    }

    @Override
    public String toString() {
        return id + "=" + edges + "|" + (distance < Integer.MAX_VALUE ? distance : "Integer.MAX_VALUE") + "|" + color.toString();
    }
}