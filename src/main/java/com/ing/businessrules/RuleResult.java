package com.ing.businessrules;

import com.ing.mortgage.model.MortgageCheckResponse;

public record RuleResult(boolean passed, MortgageCheckResponse.ErrorCodesEnum errorCode) { }
