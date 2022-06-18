public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input.txt");
        System.out.println(infos.getNumberOfCities());
        System.out.println(infos.getAllCoordinates());
        var distanceMatrix = new DistancesMatrix(infos.getAllCoordinates());
        distanceMatrix.show();
    }
}