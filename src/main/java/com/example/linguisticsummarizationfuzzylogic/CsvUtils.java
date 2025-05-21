package com.example.linguisticsummarizationfuzzylogic;

public class CsvUtils {
    public static double parseSpecialDouble(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Input string is null");
        }

        String trimmed = value.trim();

        return switch (trimmed) {
            case "-INF" -> Double.NEGATIVE_INFINITY;
            case "INF"  -> Double.POSITIVE_INFINITY;
            default     -> Double.parseDouble(trimmed);
        };
    }
}
