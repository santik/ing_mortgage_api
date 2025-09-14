package com.ing.businessrules;

import com.ing.mortgage.model.MortgageCheckRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RulesEngineTest {
    private MortgageBusinessRule ruleMock1;
    private MortgageBusinessRule ruleMock2;
    private RulesEngine rulesEngine;

    @BeforeEach
    void setUp() {
        ruleMock1 = Mockito.mock(MortgageBusinessRule.class);
        ruleMock2 = Mockito.mock(MortgageBusinessRule.class);
        rulesEngine = new RulesEngine(List.of(ruleMock1, ruleMock2));
    }

    @Test
    void evaluateRules_returnsResultsForAllRules() {
        MortgageCheckRequest request = Mockito.mock(MortgageCheckRequest.class);
        RuleResult result1 = new RuleResult(true, null);
        RuleResult result2 = new RuleResult(false, null);
        Mockito.when(ruleMock1.applyRule(request)).thenReturn(result1);
        Mockito.when(ruleMock2.applyRule(request)).thenReturn(result2);

        List<RuleResult> results = rulesEngine.evaluateRules(request);

        assertEquals(2, results.size());
        assertEquals(result1.passed(), results.get(0).passed());
        assertEquals(result2.passed(), results.get(1).passed());
    }

    @Test
    void evaluateRules_withEmptyRules_returnsEmptyList() {
        RulesEngine emptyEngine = new RulesEngine(List.of());
        MortgageCheckRequest request = Mockito.mock(MortgageCheckRequest.class);

        List<RuleResult> results = emptyEngine.evaluateRules(request);

        assertTrue(results.isEmpty());
    }
}