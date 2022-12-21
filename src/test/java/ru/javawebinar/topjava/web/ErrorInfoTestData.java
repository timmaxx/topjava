package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.util.exception.ErrorInfo;

import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;

public class ErrorInfoTestData {
    public static MatcherFactory.Matcher<ErrorInfo> ErrorInfo_MATCHER = MatcherFactory.usingEqualsComparator(ErrorInfo.class);

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