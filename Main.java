import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input.txt");

        var coordinates = infos.getAllCoordinates();
        var numberOfCities = infos.getNumberOfCities();

        var distanceMatrix = new DistanceMatrix(coordinates);
        distanceMatrix.show();

        var matrix = distanceMatrix.getMatrix();

        var bruteForceApproach = new TSPBruteForce(numberOfCities, coordinates);
        var dynamicProgrammingApproach = new DynamicProgrammingApproach(matrix);

        bruteForceApproach.start(coordinates, numberOfCities);

        var bruteForceTour = bruteForceApproach.getCidadesVisitadas();
        var bruteForceMinimumCost = bruteForceApproach.getCustoMinimo();

        System.out.println("From BRUTE FORCE APPROACH:");
        System.out.println("\ttour: " + Arrays.toString(bruteForceTour));
        System.out.println("\ttour cost: " + bruteForceMinimumCost);

        var dynamicProgrammingTour = dynamicProgrammingApproach.getTour();
        var dynamicProgramminTourCost = dynamicProgrammingApproach.getTourCost();

        System.out.println("From DYNAMIC PROGRAMMING APPROACH:");
        System.out.println("\ttour: " + dynamicProgrammingTour);
        System.out.println("\ttour cost: " + dynamicProgramminTourCost);

        bruteForceApproach.escreverArquivo("bruteForceOutput.txt");
        dynamicProgrammingApproach.tryStoreInfosInFile("dynamicProgrammingOutput.txt");
    }
}