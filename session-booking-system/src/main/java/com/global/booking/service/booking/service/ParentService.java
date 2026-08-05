
package com.global.booking.service.booking.service;

import java.util.List;

import com.global.booking.service.dto.request.BookOfferingRequest;
import com.global.booking.service.dto.response.BookingResponse;
import com.global.booking.service.dto.response.OfferingResponse;
import com.global.booking.service.dto.response.ParentBookingViewResponse;

public interface ParentService {

        List<OfferingResponse> getAvailableOfferings(
                        String timezone);

        BookingResponse bookOffering(
                        BookOfferingRequest request,
                        String idempotencyKey);

        List<ParentBookingViewResponse> getBookings(
                        Long parentId);
}
