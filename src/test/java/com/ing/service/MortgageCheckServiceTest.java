package com.ing.service;

import com.ing.businessrules.RuleResult;
import com.ing.businessrules.RulesEngine;
import com.ing.mortgage.model.Amount;
import com.ing.mortgage.model.MortgageCheckRequest;
import com.ing.mortgage.model.MortgageCheckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MortgageCheckServiceTest {

    private InterestRatesService interestRatesService;
    private RulesEngine rulesEngine;
    private MortgageCheckService mortgageCheckService;

    @BeforeEach
    void setUp() {
        interestRatesService = mock(InterestRatesService.class);
        rulesEngine = mock(RulesEngine.class);
        mortgageCheckService = new MortgageCheckService(interestRatesService, rulesEngine);
    }

    @Test
    void testCheckMortgage_AllRulesPassed() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        when(rulesEngine.evaluateRules(request)).thenReturn(
                List.of(new RuleResult(true, null), new RuleResult(true, null)));
        when(interestRatesService.getInterestRate(request)).thenReturn(BigDecimal.valueOf(2.5));
        when(interestRatesService.calculateMonthlyCosts(request, BigDecimal.valueOf(2.5)))
                .thenReturn(Amount.builder().amount(BigDecimal.valueOf(1000.0)).currency(Amount.CurrencyEnum.EUR).build());

        MortgageCheckResponse response = mortgageCheckService.checkMortgage(request);

        assertTrue(response.getFeasible());
        assertEquals(BigDecimal.valueOf(1000.0), response.getMonthlyCosts().getAmount());
    }

    @Test
    void testCheckMortgage_RulesFailed() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        when(rulesEngine.evaluateRules(request)).thenReturn(
                List.of(new RuleResult(false, null), new RuleResult(true, null)));
        MortgageCheckResponse response = mortgageCheckService.checkMortgage(request);

        assertFalse(response.getFeasible());
        assertNull(response.getMonthlyCosts());
    }
}