package main;

import java.util.Arrays;

import bruteForceApproach.TSPBruteForce;
import common.DistanceMatrix;
import dynamicProgrammingApproach.DynamicProgrammingApproach;
import heuristics.GraphOperations;

public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input1.txt");

        var coordinates = infos.getAllCoordinates();

        var distanceMatrix = new DistanceMatrix(coordinates);
        distanceMatrix.show();

        var matrix = distanceMatrix.getMatrix();

        var bruteForceApproach = new TSPBruteForce(matrix);
        var dynamicProgrammingApproach = new DynamicProgrammingApproach(matrix);

        var bruteForceMinimumCost = bruteForceApproach.getCustoMinimo();
        var bruteForceTour = bruteForceApproach.getShortestPath();

        System.out.println("From BRUTE FORCE APPROACH:");
        System.out.println("\ttour: " + bruteForceTour);
        System.out.println("\ttour cost: " + bruteForceMinimumCost);

        var dynamicProgrammingTour = dynamicProgrammingApproach.getTour();
        var dynamicProgramminTourCost = dynamicProgrammingApproach.getTourCost();

        System.out.println("From DYNAMIC PROGRAMMING APPROACH:");
        System.out.println("\ttour: " + dynamicProgrammingTour);
        System.out.println("\ttour cost: " + dynamicProgramminTourCost);

        // bruteForceApproach.escreverArquivo("bruteForceOutput.txt");
        // dynamicProgrammingApproach.tryStoreInfosInFile("dynamicProgrammingOutput.txt");

        var graphOperations = new GraphOperations(matrix);
        var mstVerticeSet = graphOperations.getMSTVerticeSet();
        System.out.println(Arrays.toString(mstVerticeSet));
    }
}