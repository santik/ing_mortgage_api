package com.ing.mortgage.businessrules.rules;

import com.ing.mortgage.businessrules.MortgageBusinessRule;
import com.ing.mortgage.businessrules.RuleResult;
import com.ing.mortgage.model.MortgageCheckRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.ing.mortgage.model.MortgageCheckResponse.ErrorCodesEnum.INSUFFICIENT_INCOME;

@Component
public class MaxMortgageForIncome implements MortgageBusinessRule {
    private static final double MAX_INCOME_MULTIPLIER = 4.0;

    /**
     * Applies the maximum mortgage for income rule.
     * Checks if the requested loan value does not exceed four times the applicant's income.
     *
     * @param mortgageCheckRequest the mortgage check request to evaluate
     * @return the result of the rule evaluation
     */
    @Override
    public RuleResult applyRule(MortgageCheckRequest mortgageCheckRequest) {
        var maxMortgageAmount = mortgageCheckRequest.getIncome().getAmount().multiply(BigDecimal.valueOf(MAX_INCOME_MULTIPLIER));
        boolean passed = mortgageCheckRequest.getLoanValue().getAmount().compareTo(maxMortgageAmount) <= 0;
        if (passed) {
            return new RuleResult(true, null);
        }
        return new RuleResult(false, INSUFFICIENT_INCOME);
    }
}
