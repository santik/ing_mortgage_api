package com.ing.service;

import com.ing.model.MortgageRateDb;
import com.ing.mortgage.model.MortgageCheckRequest;
import com.ing.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterestRatesServiceTest {
    private MortgageRateRepository mortgageRateRepository;
    private InterestRatesService interestRatesService;

    private final MortgageRateDb rate1 =
            new MortgageRateDb(5, BigDecimal.TEN, null);
    private final MortgageRateDb rate2 =
            new MortgageRateDb(10, BigDecimal.TEN, null);
    private final MortgageRateDb rate3 =
            new MortgageRateDb(15, BigDecimal.TEN, null);

    private List<MortgageRateDb> rates;

    @BeforeEach
    void setUp() {
        mortgageRateRepository = Mockito.mock(MortgageRateRepository.class);
        interestRatesService = new InterestRatesService(mortgageRateRepository);
        rates = new ArrayList<>();
        rates.add(rate1);
        rates.add(rate2);
        rates.add(rate3);
        Collections.shuffle(rates);
    }

    @Test
    void testGetMortgageRate_EmptyRates_ReturnsNull() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> interestRatesService.getMortgageRate(10));
    }

    @Test
    void testGetMortgageRate_LessThanLowest_ReturnsLowest() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(rates);
        MortgageRateDb result = interestRatesService.getMortgageRate(2);
        assertEquals(rate1, result);
    }

    @Test
    void testGetMortgageRate_ExactMatch_ReturnsMatchedRate() {

        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(rates);
        MortgageRateDb result = interestRatesService.getMortgageRate(10);
        assertEquals(rate2, result);
    }

    @Test
    void testGetMortgageRate_BetweenRates_ReturnsLowerBound() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(rates);
        MortgageRateDb result = interestRatesService.getMortgageRate(7);
        assertEquals(rate1, result);
    }

    @Test
    void testGetMortgageRate_GreaterThanAll_ReturnsLast() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(rates);
        MortgageRateDb result = interestRatesService.getMortgageRate(20);
        assertEquals(rate3, result);
    }

    @Test
    void testCalculateMonthlyCosts_ValidInput() {
        var amount = BigDecimal.valueOf(200000.0);
        var request = new MortgageCheckRequest(amount, 20, amount, amount);

        var interestRate = BigDecimal.valueOf(5.0);
        var result = interestRatesService.calculateMonthlyCosts(request, interestRate);

        var expected = BigDecimal.valueOf(10443.26);
        assertEquals(expected, result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testCalculateMonthlyCosts_ZeroInterestRate() {
        var amount = BigDecimal.valueOf(100000.0);
        var request = new MortgageCheckRequest(amount, 10, amount, amount);

        var interestRate = BigDecimal.ZERO;
        var result = interestRatesService.calculateMonthlyCosts(request, interestRate);

        // If interest rate is zero, monthly payment is principal divided by months
        var expected = 10000.0;
        assertEquals(BigDecimal.valueOf(expected).setScale(2, RoundingMode.HALF_UP), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testCalculateMonthlyCosts_NullInterestRate() {
        var amount = BigDecimal.valueOf(50000.0);
        var request = new MortgageCheckRequest(amount, 5, amount, amount);

        BigDecimal interestRate = null;
        // Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> interestRatesService.calculateMonthlyCosts(request, interestRate));
    }

    @Test
    void testGetAllInterestRates_ReturnsMappedRates() {
        List<MortgageRateDb> dbRates = List.of(
                new MortgageRateDb(5, BigDecimal.valueOf(2.5), null),
                new MortgageRateDb(10, BigDecimal.valueOf(3.0), null)
        );
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(dbRates);

        List<MortgageRateDb> result = interestRatesService.getAllInterestRates();

        assertEquals(2, result.size());
        assertEquals(BigDecimal.valueOf(2.5), result.get(0).interestRate());
        assertEquals(5, result.get(0).maturityPeriod());
        assertEquals(BigDecimal.valueOf(3.0), result.get(1).interestRate());
        assertEquals(10, result.get(1).maturityPeriod());
    }

    @Test
    void testGetAllInterestRates_EmptyRates_ReturnsEmptyList() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(List.of());

        List<MortgageRateDb> result = interestRatesService.getAllInterestRates();

        assertEquals(0, result.size());
    }

    @Test
    void testGetInterestRate_ValidRequest_ReturnsCorrectRate() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(rates);
        MortgageCheckRequest request = new MortgageCheckRequest(BigDecimal.valueOf(100000), 10, BigDecimal.valueOf(100000), BigDecimal.valueOf(100000));
        BigDecimal result = interestRatesService.getInterestRate(request);
        assertEquals(rate2.interestRate(), result);
    }

    @Test
    void testGetInterestRate_LessThanLowest_ReturnsLowestRate() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(rates);
        MortgageCheckRequest request = new MortgageCheckRequest(BigDecimal.valueOf(100000), 2, BigDecimal.valueOf(100000), BigDecimal.valueOf(100000));
        BigDecimal result = interestRatesService.getInterestRate(request);
        assertEquals(rate1.interestRate(), result);
    }

    @Test
    void testGetInterestRate_GreaterThanAll_ReturnsLastRate() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(rates);
        MortgageCheckRequest request = new MortgageCheckRequest(BigDecimal.valueOf(100000), 20, BigDecimal.valueOf(100000), BigDecimal.valueOf(100000));
        BigDecimal result = interestRatesService.getInterestRate(request);
        assertEquals(rate3.interestRate(), result);
    }

    @Test
    void testGetInterestRate_EmptyRates_ThrowsException() {
        Mockito.when(mortgageRateRepository.getCurrentMortgageRates()).thenReturn(List.of());
        MortgageCheckRequest request = new MortgageCheckRequest(BigDecimal.valueOf(100000), 10, BigDecimal.valueOf(100000), BigDecimal.valueOf(100000));
        assertThrows(IllegalStateException.class, () -> interestRatesService.getInterestRate(request));
    }
}