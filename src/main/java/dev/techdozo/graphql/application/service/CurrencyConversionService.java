package dev.techdozo.graphql.application.service;

import org.springframework.stereotype.Service;

@Service
public class CurrencyConversionService {
    public double convert(String fromCcy, String toCcy) {
        //Hardcoded value for example purpose
        return 80;
    }
}
