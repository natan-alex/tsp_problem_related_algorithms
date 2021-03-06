package heuristics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DFSSearch {
    private final Map<Integer, List<Integer>> adjacencyList;
    private final Map<Integer, Boolean> verticesAndIfWereVisited;
    private final ArrayList<Integer> path;

    public DFSSearch(Map<Integer, List<Integer>> adjacencyList) {
        this.adjacencyList = Objects.requireNonNull(adjacencyList);
        this.path = new ArrayList<Integer>();
        this.verticesAndIfWereVisited = new HashMap<>();

        initWereVisited();
    }

    // O(n^2), percorre a lista de adjacência e
    // cada seus filhos
    private void initWereVisited() {
        for (var entry : adjacencyList.entrySet()) {
            verticesAndIfWereVisited.putIfAbsent(entry.getKey(), false);

            for (var value : entry.getValue()) {
                verticesAndIfWereVisited.putIfAbsent(value, false);
            }
        }
    }

    // O(|V| + |E|), pois faz a busca em profundidade
    // percorrendo os filhos dos filhos, entretanto os filhos
    // já visitados não são visitados denovo
    public List<Integer> getPath() {
        for (var key : adjacencyList.keySet()) {
            if (!verticesAndIfWereVisited.get(key)) {
                traverseRecursively(key, -1);
            }
        }

        path.remove(path.size() - 1);

        return path;
    }

    // O(|V| + |E|), pois faz a busca em profundidade
    // percorrendo os filhos dos filhos, entretanto os filhos
    // já visitados não são visitados denovo
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
