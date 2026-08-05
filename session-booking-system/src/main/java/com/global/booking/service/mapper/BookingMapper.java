package com.global.booking.service.mapper;

import org.springframework.stereotype.Component;

import com.global.booking.service.booking.entity.Booking;
import com.global.booking.service.booking.entity.Session;
import com.global.booking.service.dto.response.ParentBookingViewResponse;
import com.global.booking.service.dto.response.SessionResponse;

import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class BookingMapper {

    public ParentBookingViewResponse toResponse(
            Booking booking,
            String timezone) {

        return ParentBookingViewResponse.builder()
                .bookingId(booking.getId())
                .offeringName(
                        booking.getOffering().getName())
                .sessions(
                        booking.getOffering()
                                .getSessions()
                                .stream()
                                .map(session -> mapSession(
                                        session,
                                        timezone))
                                .toList())
                .build();
    }

    private SessionResponse mapSession(
            Session session,
            String timezone) {

        var start = session.getStartTimeUtc()
                .atZone(ZoneOffset.UTC)
                .withZoneSameInstant(
                        ZoneId.of(timezone));

        var end = session.getEndTimeUtc()
                .atZone(ZoneOffset.UTC)
                .withZoneSameInstant(
                        ZoneId.of(timezone));

        return SessionResponse.builder()
                .sessionId(session.getId())
                .startTime(start.toString())
                .endTime(end.toString())
                .build();
    }
}
