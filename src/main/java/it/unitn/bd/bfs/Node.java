package it.unitn.bd.bfs;

import com.google.common.base.Splitter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private static final Logger logger = LogManager.getLogger();

    private final int id;

    private int distance;

    private List<Integer> edges = new ArrayList<>();

    private Color color = Color.WHITE;

    public Node(int id) {
        this.id = id;
    }

    public Node(String str) {
        List<String> map = Splitter.on("=").splitToList(str);
        id = Integer.parseInt(map.get(0));
        String[] tokens = map.get(1).split("\\|");

        for (String s : tokens[0].split(",")) {
            if (s.length() > 0) {
                edges.add(Integer.parseInt(s));
            }
        }

        if (tokens[1].equals("Integer.MAX_VALUE")) {
            distance = Integer.MAX_VALUE;
        } else {
            distance = Integer.parseInt(tokens[1]);
        }

        color = Color.valueOf(tokens[2]);
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

    public List<Integer> getEdges() {
        return edges;
    }

    public void setEdges(List<Integer> edges) {
        this.edges = edges;
    }

    public String getLine() {
        StringBuilder value = new StringBuilder();
        value.append(id);
        value.append("=");
        for (int v : edges) {
            value.append(v).append(",");
        }
        value.append("|");

        if (distance < Integer.MAX_VALUE) {
            value.append(distance);
        } else {
            value.append("Integer.MAX_VALUE");
        }
        value.append("|");

        return value.append(color.toString()).toString();
    }
}