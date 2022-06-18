import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        var infos = InputParser.parse("input.txt");
        //System.out.println(infos.getNumberOfCities());
        //System.out.println(infos.getAllCoordinates());
        List<Coordinates> coordenadas = infos.getAllCoordinates();
        TSPBruteForce teste= new TSPBruteForce(infos.getNumberOfCities(), coordenadas);
        teste.start(coordenadas, infos.getNumberOfCities());
        
    }
}