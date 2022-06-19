public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input.txt");
        var coordinates = infos.getAllCoordinates();

        var distanceMatrix = new DistanceMatrix(coordinates);
        distanceMatrix.show();

        var matrix = distanceMatrix.getMatrix();

        var bruteForceApproach = new TSPBruteForce(infos.getNumberOfCities(), infos.getAllCoordinates());
        var dynamicProgrammingApproach = new DynamicProgrammingApproach(matrix);

        bruteForceApproach.start(coordinates, infos.getNumberOfCities());

        var tour = dynamicProgrammingApproach.getTour();
        var tourCost = dynamicProgrammingApproach.getTourCost();

        System.out.println("minimum tour: " + tour);
        System.out.println("minimum tour cost: " + tourCost);
    }
}