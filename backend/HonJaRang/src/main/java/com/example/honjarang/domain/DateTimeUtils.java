package com.example.honjarang.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }

    public static String formatInstant(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        return localDateTime.format(FORMATTER);
    }

    public static Instant parseInstant(String instantString) {
        LocalDateTime localDateTime = LocalDateTime.parse(instantString, FORMATTER);
        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }
}