package heuristics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import common.Exceptions;

public class TSPHeuristic {
    private final Edge[] edges;
    private final int[][] distanceMatrix;
    private List<Integer> pathApproximation;
    private int cost;

    public TSPHeuristic(int[][] distanceMatrix) {
        this.distanceMatrix = Objects.requireNonNull(distanceMatrix);

        if (distanceMatrix.length != distanceMatrix[0].length) {
            throw new IllegalArgumentException("Distance matrix must be squared");
        }

        this.edges = new Edge[distanceMatrix.length * distanceMatrix.length - distanceMatrix.length];
        this.pathApproximation = null;
        this.cost = Integer.MAX_VALUE;

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

    private List<Edge> getMSTEdgeSet() {
        Arrays.sort(edges, Comparator.comparing(e -> e.getWeight()));

        var mstSet = new ArrayList<Edge>();
        var nextEdgeIndex = 0;

        while (mstSet.size() < distanceMatrix.length - 1) {
            var currentEdge = edges[nextEdgeIndex++];

            mstSet.add(currentEdge);

            var adjacencyList = buildAdjacencyListFrom(mstSet);
            var cycleVerifier = new CycleVerifier(adjacencyList);

            if (cycleVerifier.hasCycle()) {
                mstSet.remove(currentEdge);
            }
        }

        return mstSet;
    }

    private void fillPathApproximation() {
        var edges = getMSTEdgeSet();
        var adjacencyList = buildAdjacencyListFrom(edges);

        adjacencyList = adjacencyList.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));

        pathApproximation = new DFSearch(adjacencyList)
                .getPath()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        pathApproximation.add(pathApproximation.get(0));
    }

    public List<Integer> getPathApproximation() {
        if (pathApproximation == null) {
            fillPathApproximation();
        }

        return pathApproximation
                .stream()
                .map(i -> i + 1)
                .collect(Collectors.toList());
    }

    private void fillPathApproximationCost() {
        var pathIterator = pathApproximation.iterator();
        var current = pathIterator.next();

        cost = 0;

        while (pathIterator.hasNext()) {
            var next = pathIterator.next();
            cost += distanceMatrix[current][next];
            current = next;
        }
    }

    public int getPathApproximationCost() {
        if (pathApproximation == null) {
            fillPathApproximation();
        }

        if (cost == Integer.MAX_VALUE) {
            fillPathApproximationCost();
        }

        return cost;
    }

    public void tryStoreInfosInFile(String fileName) {
        Exceptions.throwIfNullOrEmpty(fileName, "fileName");

        if (pathApproximation == null) {
            fillPathApproximation();
        }

        if (cost == Integer.MAX_VALUE) {
            fillPathApproximationCost();
        }

        try (var writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(pathApproximation.toString());
            writer.newLine();
            writer.write(Integer.toString(cost));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}