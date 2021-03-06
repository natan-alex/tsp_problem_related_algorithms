package common;

import java.util.Objects;
import java.util.regex.Pattern;

public class Coordinates {
    private final int x;
    private final int y;

    private static final Pattern PATTERN = Pattern.compile("\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)");

    public Coordinates(int x, int y) throws Exception {
        Exceptions.throwIfNegative(x, "x");
        Exceptions.throwIfNegative(y, "y");

        this.x = x;
        this.y = y;
    }

    public Coordinates(String representation) throws Exception {
        Exceptions.throwIfNullOrEmpty(representation, "representation");

        var matcher = PATTERN.matcher(representation);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "The representation is not valid.\nExamples of valid representations: (1, 2) , (0, 5)");
        }

        var splited = representation
                .substring(1, representation.length() - 1)
                .split(",");

        this.x = Integer.parseInt(splited[0].trim());
        this.y = Integer.parseInt(splited[1].trim());
    }

    public double calculateDistanceTo(Coordinates coordinates) {
        Objects.requireNonNull(coordinates);

        var xDifference = x - coordinates.getX();
        var yDifference = y - coordinates.getY();
        var xSquared = xDifference * xDifference;
        var ySquared = yDifference * yDifference;

        return Math.sqrt(xSquared + ySquared);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}