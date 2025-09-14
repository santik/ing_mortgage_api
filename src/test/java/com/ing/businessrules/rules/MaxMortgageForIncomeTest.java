package com.ing.businessrules.rules;

import com.ing.mortgage.model.MortgageCheckRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MaxMortgageForIncomeTest {

    @Test
    void shouldReturnTrueWhenLoanValueEqualsMaxMortgage() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        double income = 50000.0;
        double loanValue = income * 4.0;
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(BigDecimal.valueOf(income))
                .loanValue(BigDecimal.valueOf(loanValue))
                .homeValue(BigDecimal.valueOf(300000.0))
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnFalseWhenLoanValueExceedsMaxMortgage() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        double income = 50000.0;
        double loanValue = income * 4.0 + 1;
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(BigDecimal.valueOf(income))
                .loanValue(BigDecimal.valueOf(loanValue))
                .homeValue(BigDecimal.valueOf(300000.0))
                .maturityPeriod(20)
                .build();
        assertFalse(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnTrueForZeroLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(BigDecimal.valueOf(50000.0))
                .loanValue(BigDecimal.valueOf(0.0))
                .homeValue(BigDecimal.valueOf(300000.0))
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnTrueForNegativeLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(BigDecimal.valueOf(50000.0))
                .loanValue(BigDecimal.valueOf(-1000.0))
                .homeValue(BigDecimal.valueOf(300000.0))
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnTrueForZeroIncomeAndZeroLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(BigDecimal.valueOf(0.0))
                .loanValue(BigDecimal.valueOf(0.0))
                .homeValue(BigDecimal.valueOf(300000.0))
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnFalseForZeroIncomeAndPositiveLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(BigDecimal.valueOf(0.0))
                .loanValue(BigDecimal.valueOf(1000.0))
                .homeValue(BigDecimal.valueOf(300000.0))
                .maturityPeriod(20)
                .build();
        assertFalse(rule.applyRule(request).passed());
    }
}