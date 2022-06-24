package dynamicProgrammingApproach;

import java.util.Objects;
import common.Exceptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicProgrammingApproach {
    private final int numberOfCities;
    private final int startingNodeIndex;
    private final int[][] distanceMatrix;
    private final int[][] memoryTable;
    private final List<Integer> tour;
    private final int endState;

    private int minimumTourCost;
    private boolean wasMinimumTourAlreadyDiscovered;

    // O(1)
    private static int twoPower(int n) {
        return (int) Math.pow(2, n);
    }

    // O(1)
    private static int bitwiseAnd(int first, int second) {
        return first & second;
    }

    // O(1)
    private static int bitwiseOr(int first, int second) {
        return first | second;
    }

    // O(1)
    private static int bitwiseXor(int first, int second) {
        return first ^ second;
    }

    public DynamicProgrammingApproach(int[][] distanceMatrix) {
        this.distanceMatrix = Objects.requireNonNull(distanceMatrix);

        if (distanceMatrix.length != distanceMatrix[0].length) {
            throw new IllegalStateException("Matrix must be square (n x n)");
        }

        this.numberOfCities = distanceMatrix.length;
        this.startingNodeIndex = 0;
        this.memoryTable = new int[numberOfCities][twoPower(numberOfCities)];
        this.tour = new ArrayList<>();
        this.endState = twoPower(numberOfCities) - 1;
        this.minimumTourCost = Integer.MAX_VALUE;
        this.wasMinimumTourAlreadyDiscovered = false;
    }

    // Melhor caso, que é quando o mínimo já tiver sido descoberto: O(1)
    // Pior caso, que é quando o mínimo ainda não tiver sido descoberto:
    // O(discoverMinimumTour)
    public List<Integer> getTour() {
        if (!wasMinimumTourAlreadyDiscovered) {
            discoverMinimumTour();
        }

        return tour;
    }

    // Melhor caso, que é quando o mínimo já tiver sido descoberto: O(1)
    // Pior caso, que é quando o mínimo ainda não tiver sido descoberto:
    // O(discoverMinimumTour)
    public int getTourCost() {
        if (!wasMinimumTourAlreadyDiscovered) {
            discoverMinimumTour();
        }

        return minimumTourCost;
    }

    // O(n^5)
    private void discoverMinimumTour() {
        if (wasMinimumTourAlreadyDiscovered) {
            return;
        }

        // O(n)
        addOutgoingEdgesFromStartingNodeToMemoryTable();

        // O(n^5)
        fillMemoryTableWithMinimumDistances();

        // O(n)
        connectTourBackToStartingNodeAndSetMinimumCost();

        // O(n^2)
        reconstructPathFromMemoryTableAndFillTour();

        wasMinimumTourAlreadyDiscovered = true;
    }

    // O(1)
    private static boolean notIn(int element, int subset) {
        return bitwiseAnd(twoPower(element), subset) == 0;
    }

    // O(n), pois vai de 0 a número de cidades
    private void addOutgoingEdgesFromStartingNodeToMemoryTable() {
        for (int end = 0; end < numberOfCities; end++) {
            if (end != startingNodeIndex) {
                var column = bitwiseOr(twoPower(startingNodeIndex), twoPower(end));
                memoryTable[end][column] = distanceMatrix[startingNodeIndex][end];
            }
        }
    }

    // O(n^5), 3 loops aninhados de complexidade O(n) e um com
    // possível complexidade O(n^2)
    // Gera combinações para diferentes tamanhos de subconjuntos
    // e percorre cada combinação gerando novos subconjuntos com
    // origens e destinos diferentes e também com tamanhos diferentes,
    // calculando o valor mínimo do caminho dentre cada combinação,
    // preenchendo também a tabela de memória de distâncias
    private void fillMemoryTableWithMinimumDistances() {
        for (int subsetSize = 3; subsetSize <= numberOfCities; subsetSize++) {
            for (int subset : getAllCombinations(subsetSize)) {
                if (notIn(startingNodeIndex, subset)) {
                    continue;
                }

                for (int next = 0; next < numberOfCities; next++) {
                    if (next == startingNodeIndex || notIn(next, subset)) {
                        continue;
                    }

                    var subsetWithoutNext = bitwiseXor(subset, twoPower(next));
                    var minimumDistance = Integer.MAX_VALUE;

                    for (int end = 0; end < numberOfCities; end++) {
                        if (end == startingNodeIndex || end == next || notIn(end, subset)) {
                            continue;
                        }

                        var newDistance = memoryTable[end][subsetWithoutNext] + distanceMatrix[end][next];

                        if (newDistance < minimumDistance) {
                            minimumDistance = newDistance;
                        }
                    }

                    memoryTable[next][subset] = minimumDistance;
                }
            }
        }
    }

    // O(n^2)
    private List<Integer> getAllCombinations(int subsetSize) {
        var subsets = new ArrayList<Integer>();
        getAllCombinationsRecursively(0, subsetSize, 0, subsets);
        return subsets;
    }

    // O(n^2), gera combinações de subconjuntos a partir de um nó e com
    // um determinado tamanho
    private void getAllCombinationsRecursively(int start, int size, int subset, List<Integer> subsets) {
        if (numberOfCities - start < size) {
            return;
        }

        if (size == 0) {
            subsets.add(subset);
            return;
        }

        for (int i = start; i < numberOfCities; i++) {
            subset = bitwiseXor(subset, twoPower(i));

            getAllCombinationsRecursively(i + 1, size - 1, subset, subsets);

            subset = bitwiseXor(subset, twoPower(i));
        }
    }

    // O(n), um único loop para calcular o custo mínimo
    private void connectTourBackToStartingNodeAndSetMinimumCost() {
        for (int i = 0; i < numberOfCities; i++) {
            if (i == startingNodeIndex) {
                continue;
            }

            var tourCost = memoryTable[i][endState] + distanceMatrix[i][startingNodeIndex];

            if (tourCost < minimumTourCost) {
                minimumTourCost = tourCost;
            }
        }
    }

    // O(n^2), por causa dos dois loops aninhados +
    // O(n) pra reverter o caminho
    // constrói o caminho mínimo a partir da tabela de
    // memória de distâncias
    private void reconstructPathFromMemoryTableAndFillTour() {
        var lastIndex = startingNodeIndex;
        var state = endState;

        tour.add(startingNodeIndex + 1);

        for (int i = 1; i < numberOfCities; i++) {
            var bestDistance = Integer.MAX_VALUE;
            var bestDistanceIndex = -1;

            for (int j = 0; j < numberOfCities; j++) {
                if (j == startingNodeIndex || notIn(j, state)) {
                    continue;
                }

                var newDistance = memoryTable[j][state] + distanceMatrix[j][lastIndex];

                if (newDistance < bestDistance) {
                    bestDistanceIndex = j;
                    bestDistance = newDistance;
                }
            }

            tour.add(bestDistanceIndex + 1);

            state = bitwiseXor(state, twoPower(bestDistanceIndex));
            lastIndex = bestDistanceIndex;
        }

        tour.add(startingNodeIndex + 1);
        Collections.reverse(tour);
    }

    // O(1)
    public void tryStoreInfosInFile(String fileName) {
        Exceptions.throwIfNullOrEmpty(fileName, "file name");

        try (var writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(tour.toString());
            writer.newLine();
            writer.write(Integer.toString(minimumTourCost));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}