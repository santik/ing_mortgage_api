package com.ing.model;

import java.math.BigDecimal;
import java.time.Instant;

public record MortgageRateDb(Integer maturityPeriod, BigDecimal interestRate, Instant lastUpdate) { }