package com.ing.businessrules.rules;

import com.ing.mortgage.model.Amount;
import com.ing.mortgage.model.MortgageCheckRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxMortgageForHomeValueTest {

    @Test
    void applyRule_shouldReturnTrue_whenLoanValueIsLessThanOrEqualToHomeValue() {
        var request = new MortgageCheckRequest(
                new Amount(BigDecimal.valueOf(50000.0), Amount.CurrencyEnum.EUR),
                10, 
                new Amount(BigDecimal.valueOf(50000.0), Amount.CurrencyEnum.EUR),
                new Amount(BigDecimal.valueOf(60000.0), Amount.CurrencyEnum.EUR));
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());

        request = new MortgageCheckRequest(
                new Amount(BigDecimal.valueOf(40000.0), Amount.CurrencyEnum.EUR),
                10,
                new Amount(BigDecimal.valueOf(50000.0), Amount.CurrencyEnum.EUR),
                new Amount(BigDecimal.valueOf(60000.0), Amount.CurrencyEnum.EUR)
        );
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnFalse_whenLoanValueIsGreaterThanHomeValue() {
        var request = new MortgageCheckRequest(
                new Amount(BigDecimal.valueOf(600000.0), Amount.CurrencyEnum.EUR),
                10,
                new Amount(BigDecimal.valueOf(500000.0), Amount.CurrencyEnum.EUR),
                new Amount(BigDecimal.valueOf(60000.0), Amount.CurrencyEnum.EUR)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertFalse(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnTrue_whenLoanValueEqualsHomeValue() {
        var request = new MortgageCheckRequest(
                new Amount(BigDecimal.valueOf(60000.0), Amount.CurrencyEnum.EUR),
                10,
                new Amount(BigDecimal.valueOf(10000.0), Amount.CurrencyEnum.EUR),
                new Amount(BigDecimal.valueOf(60000.0), Amount.CurrencyEnum.EUR)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnFalse_whenLoanValueIsNegative() {
        var request = new MortgageCheckRequest(
                new Amount(BigDecimal.valueOf(-1000.0), Amount.CurrencyEnum.EUR),
                10,
                new Amount(BigDecimal.valueOf(50000.0), Amount.CurrencyEnum.EUR),
                new Amount(BigDecimal.valueOf(60000.0), Amount.CurrencyEnum.EUR)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnTrue_whenHomeValueIsZeroAndLoanValueIsZero() {
        var request = new MortgageCheckRequest(
                new Amount(BigDecimal.valueOf(0.0), Amount.CurrencyEnum.EUR),
                10,
                new Amount(BigDecimal.valueOf(0.0), Amount.CurrencyEnum.EUR),
                new Amount(BigDecimal.valueOf(60000.0), Amount.CurrencyEnum.EUR)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());
    }
}