
package com.global.booking.service.booking.controller;

import org.springframework.web.bind.annotation.*;

import com.global.booking.service.booking.service.ParentService;
import com.global.booking.service.dto.request.BookOfferingRequest;
import com.global.booking.service.dto.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parents")
@RequiredArgsConstructor
public class ParentController {

        private final ParentService parentService;

        @GetMapping("/offerings")
        public ApiResponse<?> getOfferings(
                        @RequestParam String timezone) {

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
                        @RequestHeader("Idempotency-Key") String idempotencyKey) {

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
