
package com.global.booking.service.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import com.global.booking.service.service.ParentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.booking.service.dto.request.BookOfferingRequest;
import com.global.booking.service.dto.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parents")
@RequiredArgsConstructor
public class ParentController {

        private final ParentService parentService;

        private final ObjectMapper mapper;

        private static final Logger logger = LoggerFactory.getLogger(ParentController.class);

        @GetMapping("/offerings")
        public ApiResponse<?> getOfferings(
                        @RequestParam String timezone) {

                logger.info("/offerings Request {}", timezone);

                return ApiResponse.builder()
                                .success(true)
                                .message("Offerings fetched successfully")
                                .data(
                                                parentService
                                                                .getAvailableOfferings(
                                                                                timezone))
                                .build();
        }

        @PostMapping("/bookings")
        public ApiResponse<?> bookOffering(
                        @Valid @RequestBody BookOfferingRequest request,
                        @RequestHeader("Idempotency-Key") String idempotencyKey) throws JsonProcessingException {

                logger.info("/bookings Request {}", mapper.writeValueAsString(request));

                return ApiResponse.builder()
                                .success(true)
                                .message("Booking successful")
                                .data(
                                                parentService.bookOffering(
                                                                request,
                                                                idempotencyKey))
                                .build();
        }

        @GetMapping("/{parentId}/bookings")
        public ApiResponse<?> getBookings(
                        @PathVariable Long parentId) {

                return ApiResponse.builder()
                                .success(true)
                                .message("Bookings fetched successfully")
                                .data(
                                                parentService.getBookings(
                                                                parentId))
                                .build();
        }

}
