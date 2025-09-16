package com.ing.mortgage.repository;

import com.ing.mortgage.model.MortgageRateDb;

import java.util.List;

public interface MortgageRateRepository {
    List<MortgageRateDb> getCurrentMortgageRates();
}
