package com.global.booking.service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.booking.service.config.JacksonConfig;
import com.global.booking.service.dto.request.BookOfferingRequest;
import com.global.booking.service.dto.response.BookingResponse;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.ParentBookingViewResponse;
import com.global.booking.service.exception.GlobalExceptionHandler;
import com.global.booking.service.service.ParentService;

@WebMvcTest(ParentController.class)
@Import({JacksonConfig.class, GlobalExceptionHandler.class})
class ParentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ParentService parentService;

    @Test
    void getOfferings_success() throws Exception {
        OfferingResponse response = OfferingResponse.builder()
                .offeringId(10L)
                .offeringName("Algebra")
                .courseId(1L)
                .courseName("Math")
                .teacherId(2L)
                .teacherName("Alice")
                .build();

        when(parentService.getAvailableOfferings("America/New_York")).thenReturn(List.of(response));

        mockMvc.perform(get("/parents/offerings")
                .param("timezone", "America/New_York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Offerings fetched successfully"))
                .andExpect(jsonPath("$.data[0].offeringId").value(10));
    }

    @Test
    void bookOffering_success() throws Exception {
        BookOfferingRequest request = BookOfferingRequest.builder()
                .parentId(1L)
                .offeringId(10L)
                .build();

        BookingResponse response = BookingResponse.builder()
                .bookingId(50L)
                .parentId(1L)
                .offeringId(10L)
                .offeringName("Algebra")
                .message("Booking successful")
                .build();

        when(parentService.bookOffering(any(BookOfferingRequest.class), eq("key-123")))
                .thenReturn(response);

        mockMvc.perform(post("/parents/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Idempotency-Key", "key-123")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Booking successful"))
                .andExpect(jsonPath("$.data.bookingId").value(50));
    }

    @Test
    void bookOffering_validationFailure() throws Exception {
        BookOfferingRequest request = BookOfferingRequest.builder().build();

        mockMvc.perform(post("/parents/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Idempotency-Key", "key-123")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookings_success() throws Exception {
        ParentBookingViewResponse response = ParentBookingViewResponse.builder()
                .bookingId(50L)
                .offeringName("Algebra")
                .build();

        when(parentService.getBookings(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/parents/1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Bookings fetched successfully"))
                .andExpect(jsonPath("$.data[0].bookingId").value(50))
                .andExpect(jsonPath("$.data[0].offeringName").value("Algebra"));
    }
}
