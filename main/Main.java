package main;

import bruteForceApproach.TSPBruteForce;
import common.DistanceMatrix;
import dynamicProgrammingApproach.DynamicProgrammingApproach;
import heuristics.TSPHeuristic;

public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input1.txt");
        var coordinates = infos.getAllCoordinates();
        var distanceMatrix = new DistanceMatrix(coordinates);
        var matrix = distanceMatrix.getMatrix();

        System.out.println("DISTANCE MATRIX:");
        distanceMatrix.show();

        var bruteForceApproach = new TSPBruteForce(matrix);
        var dynamicProgrammingApproach = new DynamicProgrammingApproach(matrix);

        var bruteForceMinimumCost = bruteForceApproach.getCustoMinimo();
        var bruteForceTour = bruteForceApproach.getShortestPath();

        System.out.println("\nFrom BRUTE FORCE APPROACH:");
        System.out.println("\ttour: " + bruteForceTour);
        System.out.println("\ttour cost: " + bruteForceMinimumCost);

        var dynamicProgrammingTour = dynamicProgrammingApproach.getTour();
        var dynamicProgramminTourCost = dynamicProgrammingApproach.getTourCost();

        System.out.println("\nFrom DYNAMIC PROGRAMMING APPROACH:");
        System.out.println("\ttour: " + dynamicProgrammingTour);
        System.out.println("\ttour cost: " + dynamicProgramminTourCost);

        var heuristic = new TSPHeuristic(matrix);
        var heuristicPathApproximation = heuristic.getPathApproximation();
        var heuristicPathApproximationCost = heuristic.getPathApproximationCost();

        System.out.println("\nFrom HEURISTICS:");
        System.out.println("\ttour: " + heuristicPathApproximation);
        System.out.println("\ttour cost: " + heuristicPathApproximationCost);

        bruteForceApproach.escreverArquivo("bruteForceOutput.txt");
        dynamicProgrammingApproach.tryStoreInfosInFile("dynamicProgrammingOutput.txt");
        heuristic.tryStoreInfosInFile("heuristicOutput.txt");
    }
}