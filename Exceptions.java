public class Exceptions {
    public static void throwIfNullOrEmpty(String s, String itemName) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("The " + itemName + " cannot be null or empty");
        }
    }

    public static void throwIfNegative(int num, String itemName) {
        if (num < 0) {
            throw new IllegalArgumentException("The " + itemName + " cannot be negative");
        }
    }

    public static void throwIfGreatherThan(int num, int limit, String itemName) {
        if (num > limit) {
            throw new IllegalArgumentException("The " + itemName + " cannot be greather than " + limit);
        }
    }

    public static void throwIfGreatherThanOrEqual(int num, int limit, String itemName) {
        if (num >= limit) {
            throw new IllegalArgumentException("The " + itemName + " cannot be greather than or equal to " + limit);
        }
    }

    public static void throwIfNegativeOrEqualTo0(int num, String itemName) {
        if (num <= 0) {
            throw new IllegalArgumentException("The " + itemName + " must be greather than 0");
        }
    }

    public static void throwIfNotEqual(int first, int second, String firstNumName, String secondNumName) {
        if (first != second) {
            throw new IllegalArgumentException("The " + firstNumName + " and " + secondNumName + " are not equal");
        }
    }
}
