package com.ing.mortgage.businessrules.rules;

import com.ing.mortgage.model.Amount;
import com.ing.mortgage.model.MortgageCheckRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MaxMortgageForIncomeTest {

    @Test
    void shouldReturnTrueWhenLoanValueEqualsMaxMortgage() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        var income = Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.EUR).build();
        var loanValue = income.getAmount().multiply(BigDecimal.valueOf(4.0));
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(income)
                .loanValue(Amount.builder().amount(loanValue).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnFalseWhenLoanValueExceedsMaxMortgage() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        var income = Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.EUR).build();
        var loanValue = income.getAmount().multiply(BigDecimal.valueOf(4.0)).add(BigDecimal.valueOf(1.0));

        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(income)
                .loanValue(Amount.builder().amount(loanValue).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertFalse(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnTrueForZeroLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(0.0)).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnTrueForNegativeLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(-1000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnTrueForZeroIncomeAndZeroLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(0.0)).currency(Amount.CurrencyEnum.EUR).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(0.0)).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnFalseForZeroIncomeAndPositiveLoanValue() {
        MaxMortgageForIncome rule = new MaxMortgageForIncome();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(0.0)).currency(Amount.CurrencyEnum.EUR).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(1000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertFalse(rule.applyRule(request).passed());
    }
}