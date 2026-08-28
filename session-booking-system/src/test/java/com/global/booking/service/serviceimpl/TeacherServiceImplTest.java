package com.global.booking.service.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.global.booking.service.dto.request.AddSessionRequest;
import com.global.booking.service.dto.request.CreateOfferingRequest;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.SessionResponse;
import com.global.booking.service.entity.Course;
import com.global.booking.service.entity.Offering;
import com.global.booking.service.entity.Session;
import com.global.booking.service.entity.Teacher;
import com.global.booking.service.exception.ResourceNotFoundException;
import com.global.booking.service.repository.CourseRepository;
import com.global.booking.service.repository.OfferingRepository;
import com.global.booking.service.repository.SessionRepository;
import com.global.booking.service.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private OfferingRepository offeringRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    void createOffering_success() {
        CreateOfferingRequest request = CreateOfferingRequest.builder()
                .courseId(1L)
                .teacherId(2L)
                .name("Math Basics")
                .build();

        Course course = new Course();
        course.setId(1L);
        course.setName("Mathematics");

        Teacher teacher = new Teacher();
        teacher.setId(2L);
        teacher.setName("Jane Doe");
        teacher.setTimezone("America/New_York");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(2L)).thenReturn(Optional.of(teacher));
        when(offeringRepository.save(any(Offering.class))).thenAnswer(invocation -> {
            Offering offering = invocation.getArgument(0);
            offering.setId(10L);
            return offering;
        });

        OfferingResponse response = teacherService.createOffering(request);

        assertThat(response.getOfferingId()).isEqualTo(10L);
        assertThat(response.getOfferingName()).isEqualTo("Math Basics");
        assertThat(response.getCourseId()).isEqualTo(1L);
        assertThat(response.getCourseName()).isEqualTo("Mathematics");
        assertThat(response.getTeacherId()).isEqualTo(2L);
        assertThat(response.getTeacherName()).isEqualTo("Jane Doe");

        ArgumentCaptor<Offering> captor = ArgumentCaptor.forClass(Offering.class);
        verify(offeringRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Math Basics");
        assertThat(captor.getValue().getCourse()).isEqualTo(course);
        assertThat(captor.getValue().getTeacher()).isEqualTo(teacher);
    }

    @Test
    void createOffering_courseNotFound() {
        CreateOfferingRequest request = CreateOfferingRequest.builder()
                .courseId(99L)
                .teacherId(2L)
                .name("Math Basics")
                .build();

        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.createOffering(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course not found");
    }

    @Test
    void createOffering_teacherNotFound() {
        CreateOfferingRequest request = CreateOfferingRequest.builder()
                .courseId(1L)
                .teacherId(99L)
                .name("Math Basics")
                .build();

        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.createOffering(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Teacher not found");
    }

    @Test
    void addSession_success() {
        AddSessionRequest request = new AddSessionRequest(
                5L,
                LocalDateTime.of(2026, 8, 10, 10, 0),
                LocalDateTime.of(2026, 8, 10, 11, 0),
                "America/New_York");

        Offering offering = new Offering();
        offering.setId(5L);

        when(offeringRepository.findById(5L)).thenReturn(Optional.of(offering));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            Session session = invocation.getArgument(0);
            session.setId(20L);
            return session;
        });

        SessionResponse response = teacherService.addSession(request);

        assertThat(response.getSessionId()).isEqualTo(20L);
        assertThat(response.getStartTime()).isEqualTo("2026-08-10T14:00");
        assertThat(response.getEndTime()).isEqualTo("2026-08-10T15:00");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());
        assertThat(captor.getValue().getOffering()).isEqualTo(offering);
        assertThat(captor.getValue().getStartTimeUtc())
                .isEqualTo(LocalDateTime.of(2026, 8, 10, 14, 0));
        assertThat(captor.getValue().getEndTimeUtc())
                .isEqualTo(LocalDateTime.of(2026, 8, 10, 15, 0));
    }

    @Test
    void addSession_offeringNotFound() {
        AddSessionRequest request = new AddSessionRequest(
                99L,
                LocalDateTime.of(2026, 8, 10, 10, 0),
                LocalDateTime.of(2026, 8, 10, 11, 0),
                "UTC");

        when(offeringRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.addSession(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Offering not found");
    }

    @Test
    void addSession_endBeforeStart() {
        AddSessionRequest request = new AddSessionRequest(
                5L,
                LocalDateTime.of(2026, 8, 10, 11, 0),
                LocalDateTime.of(2026, 8, 10, 10, 0),
                "UTC");

        Offering offering = new Offering();
        offering.setId(5L);

        when(offeringRepository.findById(5L)).thenReturn(Optional.of(offering));

        assertThatThrownBy(() -> teacherService.addSession(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("End time must be after start time");
    }

    @Test
    void getTeacherOfferings_success() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Science");

        Teacher teacher = new Teacher();
        teacher.setId(2L);
        teacher.setName("John Smith");

        Offering offering = new Offering();
        offering.setId(10L);
        offering.setName("Physics 101");
        offering.setCourse(course);
        offering.setTeacher(teacher);

        when(offeringRepository.findByTeacherId(2L)).thenReturn(List.of(offering));

        List<OfferingResponse> responses = teacherService.getTeacherOfferings(2L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getOfferingId()).isEqualTo(10L);
        assertThat(responses.get(0).getOfferingName()).isEqualTo("Physics 101");
        assertThat(responses.get(0).getCourseName()).isEqualTo("Science");
        assertThat(responses.get(0).getTeacherName()).isEqualTo("John Smith");
    }

    @Test
    void getTeacherOfferings_empty() {
        when(offeringRepository.findByTeacherId(2L)).thenReturn(List.of());

        List<OfferingResponse> responses = teacherService.getTeacherOfferings(2L);

        assertThat(responses).isEmpty();
    }
}
