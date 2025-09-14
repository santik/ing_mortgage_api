package com.ing.businessrules.rules;

import com.ing.businessrules.MortgageBusinessRule;
import com.ing.businessrules.RuleResult;
import com.ing.mortgage.model.MortgageCheckRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.ing.mortgage.model.MortgageCheckResponse.ErrorCodesEnum.HIGH_LOAN_TO_VALUE;

@Component
public class MaxMortgageForHomeValue implements MortgageBusinessRule {

    /**
     * Applies the maximum mortgage for home value rule.
     * Checks if the requested loan value does not exceed the home value.
     *
     * @param mortgageCheckRequest the mortgage check request to evaluate
     * @return the result of the rule evaluation
     */
    @Override
    public RuleResult applyRule(MortgageCheckRequest mortgageCheckRequest) {
        var maxMortgageAmount = mortgageCheckRequest.getHomeValue();
        BigDecimal loanValue = mortgageCheckRequest.getLoanValue();
        boolean passed = loanValue.compareTo(maxMortgageAmount) <= 0;
        if (passed) {
            return new RuleResult(true, null);
        }
        return new RuleResult(false, HIGH_LOAN_TO_VALUE);
    }
}
