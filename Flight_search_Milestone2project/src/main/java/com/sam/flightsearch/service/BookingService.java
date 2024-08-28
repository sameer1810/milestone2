package com.sam.flightsearch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sam.flightsearch.error.ErrorCode;
import com.sam.flightsearch.error.InvalidSearchRequestException;
import com.sam.flightsearch.model.AirlinePrice;
import com.sam.flightsearch.model.BookingQuotation;
import com.sam.flightsearch.model.SearchRequest;

@Service
public class BookingService {

    @Autowired
    private BookingCache bookingCache;

    @Autowired
    private AirlineService airlineService;

    public List<AirlinePrice> findFlightPrices(SearchRequest request) {
        validateRequest(request);

        // Generate key for cache lookup
        String key = bookingCache.generateKey(request);

        // Check if the quotation is cached
        Optional<BookingQuotation> cachedQuotation = bookingCache.findCachedQuotation(key);

        List<AirlinePrice> allPrices = new ArrayList<>();

        if (cachedQuotation.isPresent()) {
            allPrices.addAll(cachedQuotation.get().getPrices());
        }

        // Query airlines only if the cached data is not complete
        List<String> cachedAirlines = allPrices.stream()
                .map(AirlinePrice::getAirlineName)
                .collect(Collectors.toList());

        // Query only those airlines that are not in the cache
        List<AirlinePrice> newPrices = airlineService.queryAirlines(request).stream()
                .filter(price -> !cachedAirlines.contains(price.getAirlineName()))
                .collect(Collectors.toList());

        // Update the cache with the new prices
        if (!newPrices.isEmpty()) {
            allPrices.addAll(newPrices);
            bookingCache.saveQuotation(key, allPrices);
        }

        return allPrices;
    }

    private void validateRequest(SearchRequest request) {
        if (request.getReturnDate().isBefore(request.getDepartureDate())) {
            throw new InvalidSearchRequestException("Return date cannot be earlier than departure date.", ErrorCode.INVALID_REQUEST);
        }

        if (request.getOrigin().equalsIgnoreCase(request.getDestination())) {
            throw new InvalidSearchRequestException("Origin and destination cannot be the same.", ErrorCode.INVALID_REQUEST);
        }
    }
}
