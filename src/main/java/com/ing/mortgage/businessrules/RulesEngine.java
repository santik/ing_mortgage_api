package com.ing.mortgage.businessrules;

import com.ing.mortgage.model.MortgageCheckRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RulesEngine {

    private final List<MortgageBusinessRule> mortgageRules;

    public List<RuleResult> evaluateRules(MortgageCheckRequest mortgageCheckRequest) {
        return mortgageRules.stream()
                .map(rule -> rule.applyRule(mortgageCheckRequest))
                .toList();
    }
}
