package heuristics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CycleVerifier {
    private final Map<Integer, List<Integer>> adjacencyList;
    private final Map<Integer, Boolean> verticesAndIfWereVisited;

    public CycleVerifier(Map<Integer, List<Integer>> adjacencyList) {
        this.adjacencyList = Objects.requireNonNull(adjacencyList);
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

    public boolean hasCycle() {
        for (var key : adjacencyList.keySet()) {
            if (!verticesAndIfWereVisited.get(key) && hasCycleRecursive(key, -1)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycleRecursive(int sourceNode, int parentNode) {
        verticesAndIfWereVisited.put(sourceNode, true);

        var children = adjacencyList.get(sourceNode);

        for (var child : children) {
            var wasChildVisited = verticesAndIfWereVisited.get(child);

            if (wasChildVisited || hasCycleRecursive(child, sourceNode)) {
                return true;
            }
        }

        return false;
    }
}
