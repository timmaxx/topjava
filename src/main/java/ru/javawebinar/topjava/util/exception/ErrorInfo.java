package ru.javawebinar.topjava.util.exception;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String detail;

    @ConstructorProperties({"url", "type", "detail"})
    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorInfo that = (ErrorInfo) o;

        if (!Objects.equals(url, that.url)) return false;
        if (!Objects.equals(type, that.type)) return false;
        return Objects.equals(detail, that.detail);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        return result;
    }
}