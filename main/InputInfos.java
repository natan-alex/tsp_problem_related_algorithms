package main;

import java.util.List;
import java.util.Objects;

import common.Coordinates;
import common.Exceptions;

public class InputInfos {
    private final List<Coordinates> coordinates;
    private final int numberOfCities;

    public InputInfos(int numberOfCities, List<Coordinates> coordinates) {
        Exceptions.throwIfNegativeOrEqualTo0(numberOfCities, "number of cities");

        this.numberOfCities = numberOfCities;
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    public int getNumberOfCities() {
        return numberOfCities;
    }

    public List<Coordinates> getAllCoordinates() {
        return coordinates;
    }

}
