package com.ing.businessrules.rules;

import com.ing.mortgage.model.MortgageCheckRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxMortgageForHomeValueTest {

    @Test
    void applyRule_shouldReturnTrue_whenLoanValueIsLessThanOrEqualToHomeValue() {
        var request = new MortgageCheckRequest(BigDecimal.valueOf(50000.0), 10, BigDecimal.valueOf(50000.0), BigDecimal.valueOf(60000.0));
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());

        request = new MortgageCheckRequest(
                BigDecimal.valueOf(40000.0),
                10,
                BigDecimal.valueOf(50000.0),
                BigDecimal.valueOf(60000.0)
        );
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnFalse_whenLoanValueIsGreaterThanHomeValue() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(600000.0),
                10,
                BigDecimal.valueOf(500000.0),
                BigDecimal.valueOf(60000.0)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertFalse(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnTrue_whenLoanValueEqualsHomeValue() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(60000.0),
                10,
                BigDecimal.valueOf(10000.0),
                BigDecimal.valueOf(60000.0)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnFalse_whenLoanValueIsNegative() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(-1000.0),
                10,
                BigDecimal.valueOf(50000.0),
                BigDecimal.valueOf(60000.0)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void applyRule_shouldReturnTrue_whenHomeValueIsZeroAndLoanValueIsZero() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(0.0),
                10,
                BigDecimal.valueOf(0.0),
                BigDecimal.valueOf(60000.0)
        );
        MaxMortgageForHomeValue rule = new MaxMortgageForHomeValue();
        assertTrue(rule.applyRule(request).passed());
    }
}