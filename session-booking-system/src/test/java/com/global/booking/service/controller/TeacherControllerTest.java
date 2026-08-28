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
import com.global.booking.service.dto.request.AddSessionRequest;
import com.global.booking.service.dto.request.CreateOfferingRequest;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.SessionResponse;
import com.global.booking.service.exception.GlobalExceptionHandler;
import com.global.booking.service.service.TeacherService;

@WebMvcTest(TeacherController.class)
@Import({JacksonConfig.class, GlobalExceptionHandler.class})
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeacherService teacherService;

    @Test
    void createOffering_success() throws Exception {
        CreateOfferingRequest request = CreateOfferingRequest.builder()
                .courseId(1L)
                .teacherId(2L)
                .name("Math Basics")
                .build();

        OfferingResponse response = OfferingResponse.builder()
                .offeringId(10L)
                .offeringName("Math Basics")
                .courseId(1L)
                .courseName("Mathematics")
                .teacherId(2L)
                .teacherName("Jane Doe")
                .build();

        when(teacherService.createOffering(any(CreateOfferingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/teachers/offerings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Offering created successfully"))
                .andExpect(jsonPath("$.data.offeringId").value(10))
                .andExpect(jsonPath("$.data.offeringName").value("Math Basics"));
    }

    @Test
    void createOffering_validationFailure() throws Exception {
        CreateOfferingRequest request = CreateOfferingRequest.builder().build();

        mockMvc.perform(post("/teachers/offerings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addSession_success() throws Exception {
        AddSessionRequest request = new AddSessionRequest(
                5L,
                java.time.LocalDateTime.of(2026, 8, 10, 10, 0),
                java.time.LocalDateTime.of(2026, 8, 10, 11, 0),
                "America/New_York");

        SessionResponse response = SessionResponse.builder()
                .sessionId(20L)
                .startTime("2026-08-10T14:00")
                .endTime("2026-08-10T15:00")
                .build();

        when(teacherService.addSession(any(AddSessionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/teachers/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Session created successfully"))
                .andExpect(jsonPath("$.data.sessionId").value(20));
    }

    @Test
    void getTeacherOfferings_success() throws Exception {
        OfferingResponse response = OfferingResponse.builder()
                .offeringId(10L)
                .offeringName("Physics 101")
                .courseId(1L)
                .courseName("Science")
                .teacherId(2L)
                .teacherName("John Smith")
                .build();

        when(teacherService.getTeacherOfferings(2L)).thenReturn(List.of(response));

        mockMvc.perform(get("/teachers/2/offerings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Offerings fetched successfully"))
                .andExpect(jsonPath("$.data[0].offeringId").value(10))
                .andExpect(jsonPath("$.data[0].offeringName").value("Physics 101"));
    }
}
