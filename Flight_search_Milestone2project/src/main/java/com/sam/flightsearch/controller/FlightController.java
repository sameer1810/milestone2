package com.sam.flightsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sam.flightsearch.error.InvalidSearchRequestException;
import com.sam.flightsearch.model.AirlinePrice;
import com.sam.flightsearch.model.SearchRequest;
import com.sam.flightsearch.service.BookingService;

import java.util.List;


@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/save")
    public ResponseEntity<List<AirlinePrice>> searchFlights(@RequestBody SearchRequest request) {
        try {
            List<AirlinePrice> prices = bookingService.findFlightPrices(request);
            return new ResponseEntity<>(prices, HttpStatus.OK);
        } catch (InvalidSearchRequestException e) {
            throw e; // This will be handled by the global exception handler
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<String> welcomeMessage() {
        return new ResponseEntity<>("Welcome to the Flight Search API!", HttpStatus.OK);
    }
}
