package com.global.booking.service.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TimezoneUtilTest {

    @Test
    void convertToUtc_fromEasternTime() {
        LocalDateTime local = LocalDateTime.of(2026, 8, 10, 10, 0);

        LocalDateTime utc = TimezoneUtil.convertToUtc(local, "America/New_York");

        assertThat(utc).isEqualTo(LocalDateTime.of(2026, 8, 10, 14, 0));
    }

    @Test
    void convertToUtc_alreadyUtc() {
        LocalDateTime local = LocalDateTime.of(2026, 8, 10, 14, 0);

        LocalDateTime utc = TimezoneUtil.convertToUtc(local, "UTC");

        assertThat(utc).isEqualTo(LocalDateTime.of(2026, 8, 10, 14, 0));
    }

    @Test
    void convertFromUtc_toEasternTime() {
        LocalDateTime utc = LocalDateTime.of(2026, 8, 10, 14, 0);

        String local = TimezoneUtil.convertFromUtc(utc, "America/New_York");

        assertThat(local).contains("2026-08-10T10:00");
    }

    @Test
    void convertFromUtc_toUtc() {
        LocalDateTime utc = LocalDateTime.of(2026, 8, 10, 14, 0);

        String result = TimezoneUtil.convertFromUtc(utc, "UTC");

        assertThat(result).contains("2026-08-10T14:00");
    }
}
