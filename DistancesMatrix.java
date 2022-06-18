import java.util.List;
import java.util.Objects;

public class DistancesMatrix {
    private final int[][] matrix;

    public DistancesMatrix(List<Coordinates> coordinates) {
        Objects.requireNonNull(coordinates); 
        var coordinatesSize = coordinates.size();

        matrix = new int[coordinatesSize][coordinatesSize];
        initMatrixWith(coordinates);
    }

    private void initMatrixWith(List<Coordinates> coordinates) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (j == i) {
                    matrix[i][j] = 0;
                } else {
                    var coordinatesI = coordinates.get(i);
                    var coordinatesJ = coordinates.get(j);
 
                    matrix[i][j] = (int) Math.round(coordinatesI.calculateDistanceTo(coordinatesJ));
                }
            }
        }
    }

    public void show() {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("[  ");

            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + "  ");
            }

            System.out.println("]");
        }
    }
}
