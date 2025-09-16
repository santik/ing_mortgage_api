package com.ing.mortgage.businessrules.rules;

import com.ing.mortgage.businessrules.MortgageBusinessRule;
import com.ing.mortgage.businessrules.RuleResult;
import com.ing.mortgage.model.MortgageCheckRequest;
import org.springframework.stereotype.Component;

import static com.ing.mortgage.model.MortgageCheckResponse.ErrorCodesEnum.CURRENCY_MISMATCH;

@Component
public class SameCurrency implements MortgageBusinessRule {

    @Override
    public RuleResult applyRule(MortgageCheckRequest mortgageCheckRequest) {
        var incomeCurrency = mortgageCheckRequest.getIncome().getCurrency();
        var loanCurrency = mortgageCheckRequest.getLoanValue().getCurrency();
        var homeCurrency = mortgageCheckRequest.getHomeValue().getCurrency();

        boolean passed = incomeCurrency.equals(loanCurrency) && incomeCurrency.equals(homeCurrency);
        if (passed) {
            return new RuleResult(true, null);
        }
        return new RuleResult(false, CURRENCY_MISMATCH);
    }
}
