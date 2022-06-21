
public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input5.txt");

        var coordinates = infos.getAllCoordinates();

        var distanceMatrix = new DistanceMatrix(coordinates);
        distanceMatrix.show();

        var matrix = distanceMatrix.getMatrix();

        var bruteForceApproach = new TSPBruteForce(matrix);
        var dynamicProgrammingApproach = new DynamicProgrammingApproach(matrix);

        var bruteForceTour = bruteForceApproach.getShortestPath();
        var bruteForceMinimumCost = bruteForceApproach.getCustoMinimo();

        System.out.println("From BRUTE FORCE APPROACH:");
        System.out.println("\ttour: " + bruteForceTour.toString());
        System.out.println("\ttour cost: " + bruteForceMinimumCost);

        var dynamicProgrammingTour = dynamicProgrammingApproach.getTour();
        var dynamicProgramminTourCost = dynamicProgrammingApproach.getTourCost();

        System.out.println("From DYNAMIC PROGRAMMING APPROACH:");
        System.out.println("\ttour: " + dynamicProgrammingTour);
        System.out.println("\ttour cost: " + dynamicProgramminTourCost);

        bruteForceApproach.escreverArquivo("bruteForceOutput5.txt");
        dynamicProgrammingApproach.tryStoreInfosInFile("dynamicProgrammingOutput5.txt");
    }
}