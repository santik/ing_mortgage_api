package com.ing.mortgage.service;

import com.ing.mortgage.businessrules.RulesEngine;
import com.ing.mortgage.businessrules.RuleResult;
import com.ing.mortgage.model.MortgageCheckRequest;
import com.ing.mortgage.model.MortgageCheckResponse;
import com.ing.mortgage.model.MortgageRate;
import com.ing.mortgage.model.MortgageRatesResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ing.mortgage.filter.TraceIdFilter.X_TRACE_ID;

@Service
@RequiredArgsConstructor
public class MortgageCheckService {

    private final InterestRatesService interestRatesService;
    private final RulesEngine rulesEngine;

    /**
     * Retrieves all current mortgage interest rates and maps them to the API response format.
     *
     * @return a response containing all mortgage rates and the trace ID
     */
    public MortgageRatesResponse getAllInterestRates() {
        var allRates = interestRatesService.getAllInterestRates();

        var rates = allRates.stream().map(rate -> MortgageRate.builder()
                .maturityPeriod(rate.maturityPeriod())
                .interestRate(rate.interestRate())
                .build()).toList();

        return MortgageRatesResponse.builder().rates((List<MortgageRate>) rates).traceId(UUID.fromString(MDC.get(X_TRACE_ID))).build();
    }

    /**
     * Checks mortgage feasibility by evaluating business rules and calculating costs.
     *
     * @param mortgageCheckRequest the mortgage check request to evaluate
     * @return a response indicating feasibility, errors, and costs
     */
    public MortgageCheckResponse checkMortgage(MortgageCheckRequest mortgageCheckRequest) {

        List<RuleResult> ruleResults = rulesEngine.evaluateRules(mortgageCheckRequest);
        boolean allRulesPassed = ruleResults.stream().allMatch(RuleResult::passed);

        String stringTraceId = MDC.get(X_TRACE_ID);
        var traceId = stringTraceId != null ? UUID.fromString(stringTraceId) : null;

        if (!allRulesPassed) {
            return getFailedMortgageCheckResponse(ruleResults, traceId);
        }

        var interestRate = interestRatesService.getInterestRate(mortgageCheckRequest);
        return MortgageCheckResponse.builder()
                .feasible(true)
                .monthlyCosts(interestRatesService.calculateMonthlyCosts(mortgageCheckRequest, interestRate))
                .traceId(traceId)
                .build();
    }

    private static MortgageCheckResponse getFailedMortgageCheckResponse(List<RuleResult> ruleResults, UUID traceId) {
        return MortgageCheckResponse.builder()
                .feasible(false)
                .errorCodes(ruleResults.stream()
                        .filter(result -> !result.passed())
                        .map(RuleResult::errorCode)
                        .toList())
                .traceId(traceId)
                .build();
    }
}
