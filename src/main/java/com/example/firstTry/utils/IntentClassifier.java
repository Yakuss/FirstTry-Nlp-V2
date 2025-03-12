package com.example.firstTry.utils;

public class IntentClassifier {
    public static String detectIntent(String input) {
        if (input.matches("(?i).*\\b(book|schedule|appointment)\\b.*")) {
            return "BOOK";
        } else if (input.matches("(?i).*\\b(cancel|remove)\\b.*")) {
            return "CANCEL";
        } else {
            return "UNKNOWN";
        }
    }
}
