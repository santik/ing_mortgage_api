package com.ing.service;

import com.ing.model.MortgageRateDb;
import com.ing.mortgage.model.Amount;
import com.ing.mortgage.model.MortgageCheckRequest;
import com.ing.repository.MortgageRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestRatesService {

    private final MortgageRateRepository mortgageRateRepository;

    /**
     * Retrieves all current mortgage interest rates from the repository.
     *
     * @return a list of current mortgage rates
     */
    public List<MortgageRateDb> getAllInterestRates() {
        return mortgageRateRepository.getCurrentMortgageRates();
    }

    /**
     * Gets the interest rate for a given mortgage check request based on its maturity period.
     *
     * @param mortgageCheckRequest the mortgage check request containing the maturity period
     * @return the interest rate for the specified maturity period
     */
    public BigDecimal getInterestRate(MortgageCheckRequest mortgageCheckRequest) {
        var currentMortgageRates = getMortgageRate(mortgageCheckRequest.getMaturityPeriod());
        return currentMortgageRates.interestRate();
    }

    // M = P × ((I × (1 + I)T) ÷ ((1 + I)T – 1))
    //M = Monthly payment: This is what you’re solving for.
    //P = Principal amount: This is the loan balance, or the amount you’re trying to pay off.
    //I = Interest rate(per month): Additionally, your mortgage interest rate is an annual interest rate that represents the interest that’s supposed to be paid monthly over the course of the year, so you’ll need to divide this by 12 to get the monthly interest rate.
    //T = Term (in months): This is the total number of payments in your loan repayment term
    /**
     * Calculates the monthly payment for a mortgage using the principal, interest rate, and term.
     *
     * @param mortgageCheckRequest the mortgage check request containing principal and term
     * @param interestRate the annual interest rate
     * @return the calculated monthly payment
     */
    public Amount calculateMonthlyCosts(MortgageCheckRequest mortgageCheckRequest, BigDecimal interestRate) {

        var principal = mortgageCheckRequest.getLoanValue().getAmount();
        int termMonths = mortgageCheckRequest.getMaturityPeriod();
        var monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);

        BigDecimal amount;
        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            amount = principal.divide(BigDecimal.valueOf(termMonths), RoundingMode.HALF_UP);
        } else {

            BigDecimal onePlusIToTheT = (BigDecimal.ONE.add(monthlyInterestRate)).pow(termMonths);
            BigDecimal numerator = monthlyInterestRate.multiply(onePlusIToTheT);
            BigDecimal denominator = onePlusIToTheT.subtract(BigDecimal.ONE);
            amount = principal
                    .multiply(numerator)
                    .divide(denominator, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return Amount.builder()
                .amount(amount)
                .currency(mortgageCheckRequest.getLoanValue().getCurrency())
                .build();
    }

    /**
     * Retrieves the mortgage rate for a specific maturity period.
     *
     * @param maturityPeriod the maturity period in months
     * @return the mortgage rate for the specified period
     */
    public MortgageRateDb getMortgageRate(Integer maturityPeriod) {
        List<MortgageRateDb> rates = mortgageRateRepository.getCurrentMortgageRates();
        if (rates.isEmpty()) {
            throw new IllegalStateException("No mortgage rates available");
        }
        rates.sort(Comparator.comparingInt(MortgageRateDb::maturityPeriod));
        for (int i = 0; i < rates.size(); i++) {
            MortgageRateDb current = rates.get(i);
            MortgageRateDb next = (i + 1 < rates.size()) ? rates.get(i + 1) : null;
            if (maturityPeriod <= current.maturityPeriod() ||
                    (next != null && maturityPeriod < next.maturityPeriod())) {
                return current;
            }
        }
        return rates.getLast();
    }
}
