package com.global.booking.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.global.booking.service.entity.Booking;
import com.global.booking.service.entity.Offering;
import com.global.booking.service.entity.Session;
import com.global.booking.service.dto.response.ParentBookingViewResponse;

class BookingMapperTest {

    private final BookingMapper bookingMapper = new BookingMapper();

    @Test
    void toResponse_convertsSessionsToParentTimezone() {
        Session session = new Session();
        session.setId(100L);
        session.setStartTimeUtc(LocalDateTime.of(2026, 8, 10, 14, 0));
        session.setEndTimeUtc(LocalDateTime.of(2026, 8, 10, 15, 0));

        Offering offering = new Offering();
        offering.setId(10L);
        offering.setName("Algebra");
        offering.setSessions(List.of(session));

        Booking booking = new Booking();
        booking.setId(50L);
        booking.setOffering(offering);

        ParentBookingViewResponse response = bookingMapper.toResponse(
                booking,
                "America/New_York");

        assertThat(response.getBookingId()).isEqualTo(50L);
        assertThat(response.getOfferingName()).isEqualTo("Algebra");
        assertThat(response.getSessions()).hasSize(1);
        assertThat(response.getSessions().get(0).getSessionId()).isEqualTo(100L);
        assertThat(response.getSessions().get(0).getStartTime()).contains("2026-08-10T10:00");
        assertThat(response.getSessions().get(0).getEndTime()).contains("2026-08-10T11:00");
    }

    @Test
    void toResponse_emptySessions() {
        Offering offering = new Offering();
        offering.setId(10L);
        offering.setName("Algebra");
        offering.setSessions(List.of());

        Booking booking = new Booking();
        booking.setId(50L);
        booking.setOffering(offering);

        ParentBookingViewResponse response = bookingMapper.toResponse(booking, "UTC");

        assertThat(response.getBookingId()).isEqualTo(50L);
        assertThat(response.getSessions()).isEmpty();
    }
}
