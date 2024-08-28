package com.sam.flightsearch.service;

import org.springframework.stereotype.Service;

import com.sam.flightsearch.model.AirlinePrice;
import com.sam.flightsearch.model.SearchRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirlineService {

    public List<AirlinePrice> queryAirlines(SearchRequest request) {
        List<AirlinePrice> prices = new ArrayList<>();
        prices.add(new AirlinePrice("TATA Airlines", 28200.0));
        prices.add(new AirlinePrice("Emirates Airlines", 1250.0));
        prices.add(new AirlinePrice("Alaska Airlines", 1000.0));
        return prices;
    }
}