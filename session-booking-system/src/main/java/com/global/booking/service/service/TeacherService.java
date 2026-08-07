package com.global.booking.service.booking.service;

import java.util.List;

import com.global.booking.service.dto.request.AddSessionRequest;
import com.global.booking.service.dto.request.CreateOfferingRequest;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.SessionResponse;

public interface TeacherService {

    OfferingResponse createOffering(
            CreateOfferingRequest request);

    SessionResponse addSession(
            AddSessionRequest request);

    List<OfferingResponse> getTeacherOfferings(
            Long teacherId);
}
