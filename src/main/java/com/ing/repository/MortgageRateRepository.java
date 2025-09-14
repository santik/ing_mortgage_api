package com.ing.repository;

import com.ing.model.MortgageRateDb;

import java.util.List;

public interface MortgageRateRepository {
    List<MortgageRateDb> getCurrentMortgageRates();
}
