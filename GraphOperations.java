import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphOperations {
    private final List<Edge> edges;
    private final int numberOfVertices;

    public GraphOperations(int[][] distanceMatrix) {
        Objects.requireNonNull(distanceMatrix);

        if (distanceMatrix.length != distanceMatrix[0].length) {
            throw new IllegalArgumentException("Distance matrix must be squared");
        }

        this.numberOfVertices = distanceMatrix.length;
        this.edges = new ArrayList<>(distanceMatrix.length);

        initEdges(distanceMatrix);
    }

    private void initEdges(int[][] distanceMatrix) {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                if (i != j) {
                    edges.add(new Edge(distanceMatrix[i][j], i, j));
                }
            }
        }
    }

    private Map<Integer, List<Integer>> buildAdjacencyListFrom(List<Edge> edges) {
        var adjacencyList = new LinkedHashMap<Integer, List<Integer>>();

        for (var edge : edges) {
            adjacencyList.putIfAbsent(edge.sourceNodeIndex, new ArrayList<Integer>());
            adjacencyList.get(edge.sourceNodeIndex).add(edge.destinationNodeIndex);
        }

        return adjacencyList;
    }

    private static class Edge {
        public final int weight;
        public final int sourceNodeIndex;
        public final int destinationNodeIndex;

        public Edge(int weight, int sourceNodeIndex, int destinationNodeIndex) {
            this.weight = weight;
            this.sourceNodeIndex = sourceNodeIndex;
            this.destinationNodeIndex = destinationNodeIndex;
        }

        @Override
        public String toString() {
            return "[" + sourceNodeIndex + "][" + destinationNodeIndex + "] -> " + weight;
        }
    }

    public int[] getMSTVerticeSet() {
        var mstSet = new ArrayList<Edge>();
        var nextEdgeIndex = 0;
        var edgesCount = 0;

        edges.sort(Comparator.comparing(e -> e.weight));

        while (edgesCount <= numberOfVertices - 1) {
            mstSet.add(edges.get(nextEdgeIndex));

            var adjacencyList = buildAdjacencyListFrom(mstSet);

            if (hasCycle(adjacencyList)) {
                mstSet.remove(mstSet.size() - 1);
            }

            nextEdgeIndex++;
            edgesCount++;
        }

        return mstSet.stream().mapToInt(e -> e.sourceNodeIndex).toArray();
    }

    private boolean hasCycleRecursive(
        Map<Integer, List<Integer>> adjacencyList,
            int sourceNodeIndex,
            boolean[] visited,
            int parent) {

        visited[sourceNodeIndex] = true;

        var children = adjacencyList.get(sourceNodeIndex);

        if (children == null) {
            return false;
        }

        for (var c : children) {
            if (c < visited.length && !visited[c]) {
                if (hasCycleRecursive(adjacencyList, c, visited, sourceNodeIndex)) {
                    return true;
                }
            } else if (c != parent) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycle(Map<Integer, List<Integer>> adjacencyList) {
        boolean[] visited = new boolean[adjacencyList.size()];

        for (int i = 0; i < adjacencyList.size(); i++) {
            visited[i] = false;
        }

        for (int i = 0; i < adjacencyList.size(); i++) {
            if (visited[i]) { 
                continue;
            }

            if (hasCycleRecursive(adjacencyList, i, visited, -1)) {
                return true;
            }
        }

        return false;
    }
}
