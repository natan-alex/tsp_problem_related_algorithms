package heuristics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import common.Exceptions;

public class TSPHeuristic {
    private final List<Edge> edges;
    private final int[][] distanceMatrix;
    private List<Integer> pathApproximation;
    private int cost;

    public TSPHeuristic(int[][] distanceMatrix) {
        this.distanceMatrix = Objects.requireNonNull(distanceMatrix);

        if (distanceMatrix.length != distanceMatrix[0].length) {
            throw new IllegalArgumentException("Distance matrix must be squared");
        }

        this.edges = new ArrayList<>(distanceMatrix.length * distanceMatrix.length);
        this.pathApproximation = null;
        this.cost = Integer.MAX_VALUE;

        initEdges(distanceMatrix);
    }

    // O(n^2), dois loops aninhados O(n)
    private void initEdges(int[][] distanceMatrix) {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                if (i != j) {
                    edges.add(new Edge(distanceMatrix[i][j], i, j));
                }
            }
        }
    }

    // O(n), percorre a lista de arestas e insere se necessário na
    // lista de adjacência
    private Map<Integer, List<Integer>> buildAdjacencyListFrom(List<Edge> edges) {
        var adjacencyList = new LinkedHashMap<Integer, List<Integer>>();

        for (var edge : edges) {
            adjacencyList.putIfAbsent(edge.getSourceNode(), new ArrayList<>());
            adjacencyList.putIfAbsent(edge.getDestinationNode(), new ArrayList<>());
            adjacencyList.get(edge.getSourceNode()).add(edge.getDestinationNode());
        }

        return adjacencyList;
    }

    // O(n^2) aproximadamente, isso pois faz n iterações
    // dado que o número de vértices da árvore geradora mínima é
    // n - 1 e constrói listas de adjacência (O(n)) a partir das
    // arestas presentes no conjunto e verificar por ciclo nessa
    // lista de adjacência (O(|V| + |E|))
    private List<Edge> getMSTEdgeSet() {
        // O(n * log(n))
        edges.sort(Comparator.comparing(e -> e.getWeight()));

        var mstSet = new ArrayList<Edge>();
        var edgesIterator = edges.iterator();

        while (mstSet.size() < distanceMatrix.length - 1) {
            var currentEdge = edgesIterator.next();

            mstSet.add(currentEdge);

            var adjacencyList = buildAdjacencyListFrom(mstSet); // O(n)
            var cycleVerifier = new CycleVerifier(adjacencyList);

            // O(|V| + |E|)
            if (cycleVerifier.hasCycle()) {
                mstSet.remove(currentEdge);
            }
        }

        return mstSet;
    }

    // O(n^2) influenciado pela construção da árvore geradora
    // mínima, com operações O(n) para construir
    // a lista de adjacência e remover filhos vazios dessa lista
    private void fillPathApproximation() {
        var edges = getMSTEdgeSet(); // O(n^2)
        var adjacencyList = buildAdjacencyListFrom(edges); // O(n)

        // O(n)
        adjacencyList = adjacencyList.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));

        // O(n)
        pathApproximation = new DFSSearch(adjacencyList)
                .getPath()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        pathApproximation.add(pathApproximation.get(0));
    }

    // O(n^2) no pior caso, que é caso tenha que encontrar o caminho
    // O(n) no melhor pois acrescenta um a cada item do caminho
    // encontrado pois as cidades começam em 1 e o caminho contém os índices
    // dos itens
    public List<Integer> getPathApproximation() {
        if (pathApproximation == null) {
            fillPathApproximation();
        }

        // O(n)
        return pathApproximation
                .stream()
                .map(i -> i + 1)
                .collect(Collectors.toList());
    }

    // O(n), percorre o caminho aproximado gerado
    // para calcular o custo
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

    // O(n^2) no pior caso que é caso tenha que encontrar
    // o caminho
    // O(1) no melhor caso que é caso o caminho já tenha sido gerado
    public int getPathApproximationCost() {
        if (pathApproximation == null) {
            fillPathApproximation();
        }

        if (cost == Integer.MAX_VALUE) {
            fillPathApproximationCost();
        }

        return cost;
    }

    // O(n^2) no pior caso que é caso tenha que encontrar
    // o caminho
    // O(1) no melhor caso que é caso o caminho já tenha sido gerado
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