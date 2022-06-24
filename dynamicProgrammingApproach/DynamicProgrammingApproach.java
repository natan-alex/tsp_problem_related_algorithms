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
    private final int N;
    private final int startingNodeIndex;
    private final int[][] distanceMatrix;
    private final int[][] memoryTable;
    private final List<Integer> tour;
    private final int endState;

    private int minimumTourCost;
    private boolean wasMinimumTourAlreadyDiscovered;

    private static int twoPower(int n) {
        return (int) Math.pow(2, n);
    }

    private static int bitwiseAnd(int first, int second) {
        return first & second;
    }

    private static int bitwiseOr(int first, int second) {
        return first | second;
    }

    private static int bitwiseXor(int first, int second) {
        return first ^ second;
    }

    public DynamicProgrammingApproach(int[][] distanceMatrix) {
        this.distanceMatrix = Objects.requireNonNull(distanceMatrix);

        if (distanceMatrix.length != distanceMatrix[0].length) {
            throw new IllegalStateException("Matrix must be square (n x n)");
        }

        this.N = distanceMatrix.length;
        this.startingNodeIndex = 0;
        this.memoryTable = new int[N][twoPower(N)];
        this.tour = new ArrayList<>();
        this.endState = twoPower(N) - 1;
        this.minimumTourCost = Integer.MAX_VALUE;
        this.wasMinimumTourAlreadyDiscovered = false;
    }

    public List<Integer> getTour() {
        if (!wasMinimumTourAlreadyDiscovered) {
            discoverMinimumTour();
        }

        return tour;
    }

    public int getTourCost() {
        if (!wasMinimumTourAlreadyDiscovered) {
            discoverMinimumTour();
        }

        return minimumTourCost;
    }

    public void discoverMinimumTour() {
        if (wasMinimumTourAlreadyDiscovered) {
            return;
        }

        addOutgoingEdgesFromStartingNodeToMemoryTable();

        fillMemoryTableWithMinimumDistances();

        connectTourBackToStartingNodeAndSetMinimumCost();

        reconstructPathFromMemoryTableAndFillTour();

        wasMinimumTourAlreadyDiscovered = true;
    }

    private static boolean notIn(int element, int subset) {
        return bitwiseAnd(twoPower(element), subset) == 0;
    }

    private void addOutgoingEdgesFromStartingNodeToMemoryTable() {
        for (int end = 0; end < N; end++) {
            if (end != startingNodeIndex) {
                var column = bitwiseOr(twoPower(startingNodeIndex), twoPower(end));
                memoryTable[end][column] = distanceMatrix[startingNodeIndex][end];
            }
        }
    }

    private void fillMemoryTableWithMinimumDistances() {
        for (int r = 3; r <= N; r++) {
            for (int subset : getAllCombinations(r, N)) {
                if (notIn(startingNodeIndex, subset)) {
                    continue;
                }

                for (int next = 0; next < N; next++) {
                    if (next == startingNodeIndex || notIn(next, subset)) {
                        continue;
                    }

                    var subsetWithoutNext = bitwiseXor(subset, twoPower(next));
                    var minimumDistance = Integer.MAX_VALUE;

                    for (int end = 0; end < N; end++) {
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

    private static List<Integer> getAllCombinations(int r, int n) {
        var subsets = new ArrayList<Integer>();
        getAllCombinationsRecursively(0, 0, r, n, subsets);
        return subsets;
    }

    private static void getAllCombinationsRecursively(int set, int at, int r, int n, List<Integer> subsets) {
        int elementsToPickInLeftSide = n - at;

        if (elementsToPickInLeftSide < r) {
            return;
        }

        if (r == 0) {
            subsets.add(set);
        } else {
            for (int i = at; i < n; i++) {
                set = bitwiseXor(set, twoPower(i));

                getAllCombinationsRecursively(set, i + 1, r - 1, n, subsets);

                set = bitwiseXor(set, twoPower(i));
            }
        }
    }

    private void connectTourBackToStartingNodeAndSetMinimumCost() {
        for (int i = 0; i < N; i++) {
            if (i == startingNodeIndex) {
                continue;
            }

            var tourCost = memoryTable[i][endState] + distanceMatrix[i][startingNodeIndex];

            if (tourCost < minimumTourCost) {
                minimumTourCost = tourCost;
            }
        }
    }

    private void reconstructPathFromMemoryTableAndFillTour() {
        var lastIndex = startingNodeIndex;
        var state = endState;

        tour.add(startingNodeIndex + 1);

        for (int i = 1; i < N; i++) {
            var bestDistance = Integer.MAX_VALUE;
            var bestDistanceIndex = -1;

            for (int j = 0; j < N; j++) {
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