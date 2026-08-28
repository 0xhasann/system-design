package com.global.booking.service.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.global.booking.service.dto.request.BookOfferingRequest;
import com.global.booking.service.dto.response.BookingResponse;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.ParentBookingViewResponse;
import com.global.booking.service.entity.Booking;
import com.global.booking.service.entity.Course;
import com.global.booking.service.entity.IdempotencyKey;
import com.global.booking.service.entity.Offering;
import com.global.booking.service.entity.Parent;
import com.global.booking.service.entity.Session;
import com.global.booking.service.entity.Teacher;
import com.global.booking.service.exception.BookingConflictException;
import com.global.booking.service.exception.DuplicateRequestException;
import com.global.booking.service.exception.ResourceNotFoundException;
import com.global.booking.service.mapper.BookingMapper;
import com.global.booking.service.repository.BookingRepository;
import com.global.booking.service.repository.IdempotencyRepository;
import com.global.booking.service.repository.OfferingRepository;
import com.global.booking.service.repository.ParentRepository;
import com.global.booking.service.repository.SessionRepository;

@ExtendWith(MockitoExtension.class)
class ParentServiceImplTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private OfferingRepository offeringRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private IdempotencyRepository idempotencyRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private ParentServiceImpl parentService;

    @Test
    void getAvailableOfferings_success() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Math");

        Teacher teacher = new Teacher();
        teacher.setId(2L);
        teacher.setName("Alice");

        Session session = new Session();
        session.setId(100L);
        session.setStartTimeUtc(LocalDateTime.of(2026, 8, 10, 14, 0));
        session.setEndTimeUtc(LocalDateTime.of(2026, 8, 10, 15, 0));

        Offering offering = new Offering();
        offering.setId(10L);
        offering.setName("Algebra");
        offering.setCourse(course);
        offering.setTeacher(teacher);
        offering.setSessions(List.of(session));

        when(offeringRepository.findAll()).thenReturn(List.of(offering));

        List<OfferingResponse> responses = parentService.getAvailableOfferings("America/New_York");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getOfferingName()).isEqualTo("Algebra");
        assertThat(responses.get(0).getSessions()).hasSize(1);
        assertThat(responses.get(0).getSessions().get(0).getSessionId()).isEqualTo(100L);
        assertThat(responses.get(0).getSessions().get(0).getStartTime())
                .contains("2026-08-10T10:00");
    }

    @Test
    void bookOffering_success() {
        BookOfferingRequest request = BookOfferingRequest.builder()
                .parentId(1L)
                .offeringId(10L)
                .build();

        Parent parent = new Parent();
        parent.setId(1L);
        parent.setName("Bob");
        parent.setTimezone("America/New_York");

        Session session = new Session();
        session.setStartTimeUtc(LocalDateTime.of(2026, 8, 10, 14, 0));
        session.setEndTimeUtc(LocalDateTime.of(2026, 8, 10, 15, 0));

        Offering offering = new Offering();
        offering.setId(10L);
        offering.setName("Algebra");
        offering.setSessions(List.of(session));

        when(idempotencyRepository.findByIdempotencyKey("key-123")).thenReturn(Optional.empty());
        when(parentRepository.lockParent(1L)).thenReturn(Optional.of(parent));
        when(offeringRepository.findById(10L)).thenReturn(Optional.of(offering));
        when(bookingRepository.existsByParentIdAndOfferingId(1L, 10L)).thenReturn(false);
        when(bookingRepository.countOverlappingSessions(
                eq(1L),
                eq(LocalDateTime.of(2026, 8, 10, 14, 0)),
                eq(LocalDateTime.of(2026, 8, 10, 15, 0)))).thenReturn(0L);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(50L);
            return booking;
        });

        BookingResponse response = parentService.bookOffering(request, "key-123");

        assertThat(response.getBookingId()).isEqualTo(50L);
        assertThat(response.getParentId()).isEqualTo(1L);
        assertThat(response.getOfferingId()).isEqualTo(10L);
        assertThat(response.getOfferingName()).isEqualTo("Algebra");
        assertThat(response.getMessage()).isEqualTo("Booking successful");

        verify(idempotencyRepository).save(any(IdempotencyKey.class));
    }

    @Test
    void bookOffering_duplicateIdempotencyKey() {
        BookOfferingRequest request = BookOfferingRequest.builder()
                .parentId(1L)
                .offeringId(10L)
                .build();

        IdempotencyKey existingKey = new IdempotencyKey();
        existingKey.setIdempotencyKey("key-123");

        when(idempotencyRepository.findByIdempotencyKey("key-123"))
                .thenReturn(Optional.of(existingKey));

        assertThatThrownBy(() -> parentService.bookOffering(request, "key-123"))
                .isInstanceOf(DuplicateRequestException.class)
                .hasMessage("Request already processed");

        verify(parentRepository, never()).lockParent(any());
    }

    @Test
    void bookOffering_parentNotFound() {
        BookOfferingRequest request = BookOfferingRequest.builder()
                .parentId(99L)
                .offeringId(10L)
                .build();

        when(idempotencyRepository.findByIdempotencyKey("key-123")).thenReturn(Optional.empty());
        when(parentRepository.lockParent(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parentService.bookOffering(request, "key-123"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Parent not found");
    }

    @Test
    void bookOffering_offeringNotFound() {
        BookOfferingRequest request = BookOfferingRequest.builder()
                .parentId(1L)
                .offeringId(99L)
                .build();

        Parent parent = new Parent();
        parent.setId(1L);

        when(idempotencyRepository.findByIdempotencyKey("key-123")).thenReturn(Optional.empty());
        when(parentRepository.lockParent(1L)).thenReturn(Optional.of(parent));
        when(offeringRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parentService.bookOffering(request, "key-123"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Offering not found");
    }

    @Test
    void bookOffering_alreadyBooked() {
        BookOfferingRequest request = BookOfferingRequest.builder()
                .parentId(1L)
                .offeringId(10L)
                .build();

        Parent parent = new Parent();
        parent.setId(1L);

        Offering offering = new Offering();
        offering.setId(10L);
        offering.setSessions(List.of());

        when(idempotencyRepository.findByIdempotencyKey("key-123")).thenReturn(Optional.empty());
        when(parentRepository.lockParent(1L)).thenReturn(Optional.of(parent));
        when(offeringRepository.findById(10L)).thenReturn(Optional.of(offering));
        when(bookingRepository.existsByParentIdAndOfferingId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> parentService.bookOffering(request, "key-123"))
                .isInstanceOf(BookingConflictException.class)
                .hasMessage("Offering already booked");
    }

    @Test
    void bookOffering_sessionOverlap() {
        BookOfferingRequest request = BookOfferingRequest.builder()
                .parentId(1L)
                .offeringId(10L)
                .build();

        Parent parent = new Parent();
        parent.setId(1L);

        Session session = new Session();
        session.setStartTimeUtc(LocalDateTime.of(2026, 8, 10, 14, 0));
        session.setEndTimeUtc(LocalDateTime.of(2026, 8, 10, 15, 0));

        Offering offering = new Offering();
        offering.setId(10L);
        offering.setSessions(List.of(session));

        when(idempotencyRepository.findByIdempotencyKey("key-123")).thenReturn(Optional.empty());
        when(parentRepository.lockParent(1L)).thenReturn(Optional.of(parent));
        when(offeringRepository.findById(10L)).thenReturn(Optional.of(offering));
        when(bookingRepository.existsByParentIdAndOfferingId(1L, 10L)).thenReturn(false);
        when(bookingRepository.countOverlappingSessions(
                eq(1L),
                eq(LocalDateTime.of(2026, 8, 10, 14, 0)),
                eq(LocalDateTime.of(2026, 8, 10, 15, 0)))).thenReturn(1L);

        assertThatThrownBy(() -> parentService.bookOffering(request, "key-123"))
                .isInstanceOf(BookingConflictException.class)
                .hasMessage("Session overlap detected");
    }

    @Test
    void getBookings_success() {
        Parent parent = new Parent();
        parent.setId(1L);
        parent.setTimezone("America/New_York");

        Booking booking = new Booking();
        booking.setId(50L);

        ParentBookingViewResponse viewResponse = ParentBookingViewResponse.builder()
                .bookingId(50L)
                .offeringName("Algebra")
                .build();

        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(bookingRepository.findByParentId(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toResponse(booking, "America/New_York")).thenReturn(viewResponse);

        List<ParentBookingViewResponse> responses = parentService.getBookings(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getBookingId()).isEqualTo(50L);
        assertThat(responses.get(0).getOfferingName()).isEqualTo("Algebra");
    }

    @Test
    void getBookings_parentNotFound() {
        when(parentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parentService.getBookings(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Parent not found");
    }
}
