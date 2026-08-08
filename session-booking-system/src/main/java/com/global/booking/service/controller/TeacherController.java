
package com.global.booking.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.global.booking.service.service.TeacherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.booking.service.dto.request.AddSessionRequest;
import com.global.booking.service.dto.request.CreateOfferingRequest;
import com.global.booking.service.dto.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

        private final TeacherService teacherService;

        private final ObjectMapper mapper;

        private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

        @PostMapping("/offerings")
        public ApiResponse<?> createOffering(
                        @Valid @RequestBody CreateOfferingRequest request) throws JsonProcessingException {

                logger.info("/bookings Request {}", mapper.writeValueAsString(request));

                return ApiResponse.builder()
                                .success(true)
                                .message("Offering created successfully")
                                .data(
                                                teacherService.createOffering(
                                                                request))
                                .build();
        }

        @PostMapping("/sessions")
        public ApiResponse<?> addSession(
                        @Valid @RequestBody AddSessionRequest request) {

                return ApiResponse.builder()
                                .success(true)
                                .message("Session created successfully")
                                .data(
                                                teacherService.addSession(
                                                                request))
                                .build();
        }

        @GetMapping("/{teacherId}/offerings")
        public ApiResponse<?> getTeacherOfferings(
                        @PathVariable Long teacherId) {

                logger.info("/bookings Request {}", teacherId);

                return ApiResponse.builder()
                                .success(true)
                                .message("Offerings fetched successfully")
                                .data(
                                                teacherService
                                                                .getTeacherOfferings(
                                                                                teacherId))
                                .build();
        }

}
