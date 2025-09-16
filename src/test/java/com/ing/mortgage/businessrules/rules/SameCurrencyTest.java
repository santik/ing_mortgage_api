package com.ing.mortgage.businessrules.rules;

import com.ing.mortgage.model.Amount;
import com.ing.mortgage.model.MortgageCheckRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SameCurrencyTest {

    @Test
    void shouldReturnTrueWhenUseTheSameCurrency() {
        SameCurrency rule = new SameCurrency();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(200000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertTrue(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnFalseWhenIncomeCurrencyIsDifferent() {
        SameCurrency rule = new SameCurrency();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.USD).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(200000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertFalse(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnFalseWhenLoanCurrencyIsDifferent() {
        SameCurrency rule = new SameCurrency();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(200000.0)).currency(Amount.CurrencyEnum.USD).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .maturityPeriod(20)
                .build();
        assertFalse(rule.applyRule(request).passed());
    }

    @Test
    void shouldReturnFalseWhenHomeCurrencyIsDifferent() {
        SameCurrency rule = new SameCurrency();
        MortgageCheckRequest request = MortgageCheckRequest.builder()
                .income(Amount.builder().amount(BigDecimal.valueOf(50000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .loanValue(Amount.builder().amount(BigDecimal.valueOf(200000.0)).currency(Amount.CurrencyEnum.EUR).build())
                .homeValue(Amount.builder().amount(BigDecimal.valueOf(300000.0)).currency(Amount.CurrencyEnum.USD).build())
                .maturityPeriod(20)
                .build();
        assertFalse(rule.applyRule(request).passed());
    }
}