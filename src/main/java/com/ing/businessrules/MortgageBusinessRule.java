package com.ing.businessrules;

import com.ing.mortgage.model.MortgageCheckRequest;

/**
 * Interface for mortgage business rules.
 * Implementations should provide logic to evaluate a mortgage check request.
 */
public interface MortgageBusinessRule {
    /**
     * Applies the business rule to the given mortgage check request.
     *
     * @param mortgageCheckRequest the mortgage check request to evaluate
     * @return the result of the rule evaluation
     */
    RuleResult applyRule(MortgageCheckRequest mortgageCheckRequest);
}
