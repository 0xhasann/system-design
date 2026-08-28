
package com.global.booking.service.serviceimpl;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.global.booking.service.entity.Booking;
import com.global.booking.service.entity.IdempotencyKey;
import com.global.booking.service.entity.Offering;
import com.global.booking.service.entity.Parent;
import com.global.booking.service.entity.Session;
import com.global.booking.service.exception.BookingConflictException;
import com.global.booking.service.exception.DuplicateRequestException;
import com.global.booking.service.exception.ResourceNotFoundException;
import com.global.booking.service.service.ParentService;
import com.global.booking.service.dto.request.BookOfferingRequest;
import com.global.booking.service.dto.response.BookingResponse;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.ParentBookingViewResponse;
import com.global.booking.service.dto.response.SessionResponse;
import com.global.booking.service.mapper.BookingMapper;
import com.global.booking.service.repository.BookingRepository;
import com.global.booking.service.repository.IdempotencyRepository;
import com.global.booking.service.repository.OfferingRepository;
import com.global.booking.service.repository.ParentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

        private final ParentRepository parentRepository;
        private final OfferingRepository offeringRepository;
        private final BookingRepository bookingRepository;
        private final IdempotencyRepository idempotencyRepository;
        private final BookingMapper bookingMapper;

        private SessionResponse mapSession(
                        Session session,
                        String timezone) {

                ZoneId parentZone = ZoneId.of(timezone);

                var start = session.getStartTimeUtc()
                                .atZone(ZoneOffset.UTC)
                                .withZoneSameInstant(parentZone);

                var end = session.getEndTimeUtc()
                                .atZone(ZoneOffset.UTC)
                                .withZoneSameInstant(parentZone);

                return SessionResponse.builder()
                                .sessionId(session.getId())
                                .startTime(start.toString())
                                .endTime(end.toString())
                                .build();
        }

        @Override
        @Transactional(readOnly = true)
        public List<OfferingResponse> getAvailableOfferings(
                        String timezone) {

                return offeringRepository.findAll()
                                .stream()
                                .map(offering -> OfferingResponse.builder()
                                                .offeringId(offering.getId())
                                                .offeringName(offering.getName())
                                                .courseId(
                                                                offering.getCourse().getId())
                                                .courseName(
                                                                offering.getCourse().getName())
                                                .teacherId(
                                                                offering.getTeacher().getId())
                                                .teacherName(
                                                                offering.getTeacher().getName())
                                                .sessions(
                                                                offering.getSessions()
                                                                                .stream()
                                                                                .map(session -> mapSession(
                                                                                                session,
                                                                                                timezone))
                                                                                .toList())
                                                .build())
                                .toList();
        }

        @Override
        @Transactional
        public BookingResponse bookOffering(
                        BookOfferingRequest request,
                        String idempotencyKey) {
                var existingKey = idempotencyRepository
                                .findByIdempotencyKey(
                                                idempotencyKey);

                if (existingKey.isPresent()) {

                        throw new DuplicateRequestException(
                                        "Request already processed");
                }

                Parent parent = parentRepository
                                .lockParent(
                                                request.getParentId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Parent not found"));

                Offering offering = offeringRepository
                                .findById(
                                                request.getOfferingId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Offering not found"));

                if (bookingRepository
                                .existsByParentIdAndOfferingId(
                                                parent.getId(),
                                                offering.getId())) {

                        throw new BookingConflictException(
                                        "Offering already booked");
                }

                for (Session session : offering.getSessions()) {

                        long overlapCount = bookingRepository
                                        .countOverlappingSessions(
                                                        parent.getId(),
                                                        session.getStartTimeUtc(),
                                                        session.getEndTimeUtc());

                        if (overlapCount > 0) {

                                throw new BookingConflictException(
                                                "Session overlap detected");
                        }
                }

                Booking booking = new Booking();

                booking.setParent(parent);
                booking.setOffering(offering);

                bookingRepository.save(booking);

                IdempotencyKey key = new IdempotencyKey();

                key.setIdempotencyKey(
                                idempotencyKey);

                key.setResponseJson(
                                "BOOKING_CREATED");

                idempotencyRepository.save(key);

                return BookingResponse.builder()
                                .bookingId(booking.getId())
                                .parentId(parent.getId())
                                .offeringId(offering.getId())
                                .offeringName(offering.getName())
                                .message("Booking successful")
                                .build();
        }

        @Override
        @Transactional(readOnly = true)
        public List<ParentBookingViewResponse> getBookings(
                        Long parentId) {

                Parent parent = parentRepository.findById(parentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Parent not found"));

                return bookingRepository
                                .findByParentId(parentId)
                                .stream()
                                .map(booking -> bookingMapper.toResponse(
                                                booking,
                                                parent.getTimezone()))
                                .toList();
        }
}
