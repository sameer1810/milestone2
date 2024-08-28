package com.sam.flightsearch;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sam.flightsearch.error.InvalidSearchRequestException;
import com.sam.flightsearch.model.BookingQuotation;
import com.sam.flightsearch.model.SearchRequest;
import com.sam.flightsearch.service.BookingCache;
import com.sam.flightsearch.service.BookingService;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

	@Mock
    private BookingQuotation model;

    @Mock
    private BookingCache bookingCache;

    @InjectMocks
    private BookingService bookingService;

    @SuppressWarnings("unused")
	private SearchRequest validRequest;
    private SearchRequest sameOriginDest;
    private SearchRequest returnBeforeDeparture;

    @BeforeEach
    public void setUp() {
        validRequest = new SearchRequest("NYC", "LAX", LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 5));
        sameOriginDest = new SearchRequest("JFK", "JFK", LocalDate.of(2024, 9, 15), LocalDate.of(2024, 9, 20));
        returnBeforeDeparture = new SearchRequest("JFK", "LAX", LocalDate.of(2024, 9, 15), LocalDate.of(2024, 9, 10));
    }

    @Test
    public void testSameOriginAndDestination() {
        Assertions.assertThrows(InvalidSearchRequestException.class, () -> bookingService.findFlightPrices(sameOriginDest));
    }

    @Test
    public void testReturnDateBeforeDeparture() {
    	Assertions.assertThrows(InvalidSearchRequestException.class, () -> bookingService.findFlightPrices(returnBeforeDeparture));
    }

}
