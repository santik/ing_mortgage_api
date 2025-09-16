package com.ing.mortgage.repository;

import com.ing.mortgage.configuration.MortgageRatesConfiguration;
import com.ing.mortgage.model.MortgageRateDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InMemoryMortgageRateRepository implements MortgageRateRepository {

    private final MortgageRatesConfiguration mortgageRatesConfiguration;

    /**
     * Retrieves the current list of mortgage rates from configuration.
     *
     * @return a list of current mortgage rates
     */
    @Override
    public List<MortgageRateDb> getCurrentMortgageRates() {
        return mortgageRatesConfiguration.getRates();
    }
}
