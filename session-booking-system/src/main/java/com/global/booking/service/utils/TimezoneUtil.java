package com.global.booking.service.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimezoneUtil {

    private TimezoneUtil() {
    }

    public static LocalDateTime convertToUtc(
            LocalDateTime localDateTime,
            String timezone) {

        return localDateTime
                .atZone(ZoneId.of(timezone))
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    public static String convertFromUtc(
            LocalDateTime utcDateTime,
            String timezone) {

        return utcDateTime
                .atZone(ZoneOffset.UTC)
                .withZoneSameInstant(
                        ZoneId.of(timezone))
                .toString();
    }

}
