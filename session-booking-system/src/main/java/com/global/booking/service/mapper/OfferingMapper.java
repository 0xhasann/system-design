package com.global.booking.service.mapper;

import org.springframework.stereotype.Component;

import com.global.booking.service.entity.Offering;
import com.global.booking.service.entity.Session;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.SessionResponse;

import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class OfferingMapper {

        public OfferingResponse toResponse(
                        Offering offering,
                        String timezone) {

                return OfferingResponse.builder()
                                .offeringId(offering.getId())
                                .offeringName(offering.getName())
                                .courseId(offering.getCourse().getId())
                                .courseName(offering.getCourse().getName())
                                .teacherId(offering.getTeacher().getId())
                                .teacherName(offering.getTeacher().getName())
                                .sessions(
                                                offering.getSessions()
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
