package com.global.booking.service.serviceimpl;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.global.booking.service.entity.Course;
import com.global.booking.service.entity.Offering;
import com.global.booking.service.entity.Session;
import com.global.booking.service.entity.Teacher;
import com.global.booking.service.exception.ResourceNotFoundException;
import com.global.booking.service.service.TeacherService;
import com.global.booking.service.dto.request.AddSessionRequest;
import com.global.booking.service.dto.request.CreateOfferingRequest;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.SessionResponse;
import com.global.booking.service.repository.CourseRepository;
import com.global.booking.service.repository.OfferingRepository;
import com.global.booking.service.repository.SessionRepository;
import com.global.booking.service.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

        private final CourseRepository courseRepository;
        private final TeacherRepository teacherRepository;
        private final OfferingRepository offeringRepository;
        private final SessionRepository sessionRepository;

        @Override
        public OfferingResponse createOffering(
                        CreateOfferingRequest request) {

                Course course = courseRepository.findById(request.getCourseId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Course not found"));

                Teacher teacher = teacherRepository.findById(request.getTeacherId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Teacher not found"));

                Offering offering = new Offering();

                offering.setName(request.getName());
                offering.setCourse(course);
                offering.setTeacher(teacher);

                offeringRepository.save(offering);

                return OfferingResponse.builder()
                                .offeringId(offering.getId())
                                .offeringName(offering.getName())
                                .courseId(course.getId())
                                .courseName(course.getName())
                                .teacherId(teacher.getId())
                                .teacherName(teacher.getName())
                                .build();
        }

        @Override
        public SessionResponse addSession(
                        AddSessionRequest request) {

                Offering offering = offeringRepository.findById(
                                request.getOfferingId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Offering not found"));

                if (request.getEndTime()
                                .isBefore(request.getStartTime())) {

                        throw new IllegalArgumentException(
                                        "End time must be after start time");
                }

                ZoneId teacherZone = ZoneId.of(request.getTimezone());

                var startUtc = request.getStartTime()
                                .atZone(teacherZone)
                                .withZoneSameInstant(
                                                ZoneOffset.UTC)
                                .toLocalDateTime();

                var endUtc = request.getEndTime()
                                .atZone(teacherZone)
                                .withZoneSameInstant(
                                                ZoneOffset.UTC)
                                .toLocalDateTime();

                Session session = new Session();

                session.setOffering(offering);
                session.setStartTimeUtc(startUtc);
                session.setEndTimeUtc(endUtc);

                sessionRepository.save(session);

                return SessionResponse.builder()
                                .sessionId(session.getId())
                                .startTime(startUtc.toString())
                                .endTime(endUtc.toString())
                                .build();
        }

        @Override
        @Transactional(readOnly = true)
        public List<OfferingResponse> getTeacherOfferings(
                        Long teacherId) {

                return offeringRepository
                                .findByTeacherId(teacherId)
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
                                                .build())
                                .toList();
        }

}
