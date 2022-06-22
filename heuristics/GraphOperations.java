package heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphOperations {
    private final Edge[] edges;
    private final int numberOfVertices;

    public GraphOperations(int[][] distanceMatrix) {
        Objects.requireNonNull(distanceMatrix);

        if (distanceMatrix.length != distanceMatrix[0].length) {
            throw new IllegalArgumentException("Distance matrix must be squared");
        }

        this.numberOfVertices = distanceMatrix.length;
        this.edges = new Edge[distanceMatrix.length * distanceMatrix.length - distanceMatrix.length];

        initEdges(distanceMatrix);
    }

    private void initEdges(int[][] distanceMatrix) {
        int edgeIndex = 0;

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                if (i != j) {
                    edges[edgeIndex++] = new Edge(distanceMatrix[i][j], i, j);
                }
            }
        }
    }

    private Map<Integer, List<Integer>> buildAdjacencyListFrom(List<Edge> edges) {
        var adjacencyList = new LinkedHashMap<Integer, List<Integer>>();

        for (var edge : edges) {
            adjacencyList.putIfAbsent(edge.getSourceNode(), new ArrayList<>());
            adjacencyList.putIfAbsent(edge.getDestinationNode(), new ArrayList<>());
            adjacencyList.get(edge.getSourceNode()).add(edge.getDestinationNode());
        }

        return adjacencyList;
    }

    public List<Edge> getMSTEdgeSet() {
        Arrays.sort(edges, Comparator.comparing(e -> e.getWeight()));

        var mstSet = new ArrayList<Edge>();
        var nextEdgeIndex = 0;

        while (mstSet.size() < numberOfVertices - 1) {
            mstSet.add(edges[nextEdgeIndex]);

            var adjacencyList = buildAdjacencyListFrom(mstSet);
            var cycleVerifier = new CycleVerifier(adjacencyList);

            if (cycleVerifier.hasCycle()) {
                mstSet.remove(edges[nextEdgeIndex]);
            }

            nextEdgeIndex++;
        }

        return mstSet;
    }

    public List<Integer> dfsSearch(List<Edge> edges) {
        var adjacencyList = buildAdjacencyListFrom(edges);

        System.out.println(edges);
        System.out.println(adjacencyList);

        return null;
    }
}