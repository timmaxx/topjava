package ru.javawebinar.topjava.web.meal;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTime implements Converter<String, LocalTime> {
    public LocalTime convert(String source) {
        return LocalTime.parse(source, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}