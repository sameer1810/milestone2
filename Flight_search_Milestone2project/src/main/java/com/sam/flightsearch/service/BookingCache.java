package com.sam.flightsearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.sam.flightsearch.model.AirlinePrice;
import com.sam.flightsearch.model.BookingQuotation;
import com.sam.flightsearch.model.SearchRequest;

import java.util.List;
import java.util.Optional;

@Service
public class BookingCache {

	@Autowired
    private MongoTemplate mongoTemplate;

    public String generateKey(SearchRequest request) {
        return request.getOrigin() + ":" + request.getDestination() + ":" + request.getDepartureDate() + ":" + request.getReturnDate();
    }

    @Cacheable(value = "flightPrices", key = "#key")
    public Optional<BookingQuotation> findCachedQuotation(String key) {
        return Optional.ofNullable(mongoTemplate.findById(key, BookingQuotation.class));
    }

    @CachePut(value = "flightPrices", key = "#key")
    public BookingQuotation saveQuotation(String key, List<AirlinePrice> prices) {
        BookingQuotation quotation = new BookingQuotation(key, null, null, null, null, prices); 
        mongoTemplate.save(quotation);
        return quotation; // Return the saved entity so it's cached as well
    }
}
