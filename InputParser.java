import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class InputParser {

    public static InputInfos parse(String fileName) throws Exception {
        Exceptions.throwIfNullOrEmpty(fileName, "file name");

        try (var reader = new BufferedReader(new FileReader(fileName))) {
            var numberOfCities = Integer.parseInt(reader.readLine());

            Exceptions.throwIfNegative(numberOfCities, "number of cities");

            var line = reader.readLine();
            var coordinates = new ArrayList<Coordinates>(numberOfCities);

            while (line != null) {
                coordinates.add(new Coordinates(line));
                line = reader.readLine();
            }

            return new InputInfos(numberOfCities, coordinates);
        }
    }
}