public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input.txt");
        var distanceMatrix = new DistanceMatrix(infos.getAllCoordinates());
        distanceMatrix.show();

        var matrix = distanceMatrix.getMatrix();
        var dynamicProgrammingApproach = new DynamicProgrammingApproach(matrix);

        var tour = dynamicProgrammingApproach.getTour();
        var tourCost = dynamicProgrammingApproach.getTourCost();

        System.out.println("minimum tour: " + tour);
        System.out.println("minimum tour cost: " + tourCost);
    }
}