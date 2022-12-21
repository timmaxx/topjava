package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.util.exception.ErrorInfo;

import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;

public class ErrorInfoTestData {
    public static MatcherFactory.Matcher<ErrorInfo> ErrorInfo_MATCHER = MatcherFactory.usingEqualsComparator(ErrorInfo.class);
    //public static MatcherFactory.Matcher<ErrorInfo> ErrorInfo_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(ErrorInfo.class);

    //public static final String WRONG = "WRONG";
/*
    public static final ErrorResponse errorResponseMeal1 = new ErrorResponse(
            "http://localhost/rest/profile/meals/WRONG",
            "VALIDATION_ERROR",
            "Failed to convert value of type 'java.lang.String' to required type 'int'; nested exception is java.lang.NumberFormatException: For input string: \"WRONG\""
    );

    public static final ErrorResponse errorResponseMeal2 = new ErrorResponse(
            "http://localhost/rest/profile/meals/",
            "VALIDATION_ERROR",
            "JSON parse error: Unrecognized field \"url\" (class ru.javawebinar.topjava.model.Meal), not marked as ignorable; nested exception is com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field \"url\" (class ru.javawebinar.topjava.model.Meal), not marked as ignorable (5 known properties: \"id\", \"description\", \"calories\", \"dateTime\", \"user\"])\n at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 1, column: 9] (through reference chain: ru.javawebinar.topjava.model.Meal[\"url\"])"
    );

    public static final ErrorResponse errorResponseMeal3 = new ErrorResponse(
            "http://localhost/rest/profile/meals/filter",
            "VALIDATION_ERROR",
            "Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [@org.springframework.web.bind.annotation.RequestParam @org.springframework.lang.Nullable java.time.LocalDate] for value 'WRONG'; nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [WRONG]"
    );
*/
    public static final ErrorInfo ERROR_INFO_MEAL_ALL_FIELDS_EMPTY = new ErrorInfo(
            "http://localhost/rest/profile/meals/",
        VALIDATION_ERROR,
        "[calories] must be between 10 and 5000<br>[dateTime] must not be null<br>[description] must not be blank"
    );

    public static final ErrorInfo ERROR_INFO_MEAL_ALL_FIELDS_INVALID = new ErrorInfo(
            "http://localhost/rest/profile/meals/",
            VALIDATION_ERROR,
            "[calories] must be between 10 and 5000<br>[description] size must be between 2 and 120"
    );

    public static final ErrorInfo ERROR_INFO_MEAL_ALL_FIELDS_EMPTY_UPDATING = new ErrorInfo(
            "http://localhost/rest/profile/meals/100003",
            VALIDATION_ERROR,
            "[calories] must be between 10 and 5000<br>[dateTime] must not be null<br>[description] must not be blank"
    );

    public static final ErrorInfo ERROR_INFO_MEAL_ALL_FIELDS_INVALID_UPDATING = new ErrorInfo(
            "http://localhost/rest/profile/meals/100003",
            VALIDATION_ERROR,
            "[calories] must be between 10 and 5000<br>[description] size must be between 2 and 120"
    );
}