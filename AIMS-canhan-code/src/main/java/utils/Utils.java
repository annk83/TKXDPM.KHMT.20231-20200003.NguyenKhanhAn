package utils;

public class Utils {
    public static String countUnitStr(int count, String unit) {
        return "%d %s%s".formatted(count, unit, count > 1 ? "s": "");
    }
}
