package com.example.firstTry.touur;

public record TimeExpressionResult(
        String expression,
        String type,  // DATE/TIME/DATETIME
        String fullValue,  // Original temporal value
        String datePart,   // Null if not a date
        String timePart,   // Null if not a time
        int startOffset,
        int endOffset
) {}
