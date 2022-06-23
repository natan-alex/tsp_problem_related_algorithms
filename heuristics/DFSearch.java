package heuristics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DFSearch {
    private final Map<Integer, List<Integer>> adjacencyList;
    private final Map<Integer, Boolean> verticesAndIfWereVisited;
    private final ArrayList<Integer> path;

    public DFSearch(Map<Integer, List<Integer>> adjacencyList) {
        this.adjacencyList = Objects.requireNonNull(adjacencyList);
        this.path = new ArrayList<Integer>();
        this.verticesAndIfWereVisited = new HashMap<>();

        initWereVisited();
    }

    private void initWereVisited() {
        for (var entry : adjacencyList.entrySet()) {
            verticesAndIfWereVisited.putIfAbsent(entry.getKey(), false);

            for (var value : entry.getValue()) {
                verticesAndIfWereVisited.putIfAbsent(value, false);
            }
        }
    }

    public List<Integer> getPath() {
        for (var key : adjacencyList.keySet()) {
            if (!verticesAndIfWereVisited.get(key)) {
                traverseRecursively(key, -1);
            }
        }

        path.remove(path.size() - 1);

        return path;
    }

    private void traverseRecursively(int currentNode, int parentNode) {
        verticesAndIfWereVisited.put(currentNode, true);

        path.add(currentNode);

        var children = adjacencyList.get(currentNode);

        if (children == null) {
            path.add(parentNode);

            return;
        }

        for (var child : children) {
            var wasChildVisited = verticesAndIfWereVisited.get(child);

            if (!wasChildVisited) {
                traverseRecursively(child, currentNode);
            }
        }

        path.add(parentNode);
    }
}
