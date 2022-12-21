package ru.javawebinar.topjava.web;

import java.util.Objects;

public class ErrorResponse {
    private String url;
    private String type;
    private String detail;

    public ErrorResponse() {}

    public ErrorResponse(String url, String type, String detail) {
        this.url = url;
        this.type = type;
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorResponse that = (ErrorResponse) o;

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