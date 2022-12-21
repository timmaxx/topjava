package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.MatcherFactory;

public class ErrorResponseTestData {
    public static MatcherFactory.Matcher<ErrorResponse> ErrorResponse_MATCHER = MatcherFactory.usingEqualsComparator(ErrorResponse.class);

    public static final String WRONG = "WRONG";

    public static final ErrorResponse errorResponse1 = new ErrorResponse(
            "http://localhost/rest/profile/meals/WRONG",
            "VALIDATION_ERROR",
            "Failed to convert value of type 'java.lang.String' to required type 'int'; nested exception is java.lang.NumberFormatException: For input string: \"WRONG\""
    );

    public static final ErrorResponse errorResponse2 = new ErrorResponse(
            "http://localhost/rest/profile/meals/",
            "VALIDATION_ERROR",
            "JSON parse error: Unrecognized field \"url\" (class ru.javawebinar.topjava.model.Meal), not marked as ignorable; nested exception is com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field \"url\" (class ru.javawebinar.topjava.model.Meal), not marked as ignorable (5 known properties: \"id\", \"description\", \"calories\", \"dateTime\", \"user\"])\n at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 1, column: 9] (through reference chain: ru.javawebinar.topjava.model.Meal[\"url\"])"
    );

    public static final ErrorResponse errorResponse3 = new ErrorResponse(
            "http://localhost/rest/profile/meals/filter",
            "VALIDATION_ERROR",
            "Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [@org.springframework.web.bind.annotation.RequestParam @org.springframework.lang.Nullable java.time.LocalDate] for value 'WRONG'; nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [WRONG]"
    );
}