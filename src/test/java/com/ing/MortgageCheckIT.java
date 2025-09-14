package com.ing;

import com.ing.mortgage.model.ErrorResponse;
import com.ing.mortgage.model.MortgageCheckRequest;
import com.ing.mortgage.model.MortgageCheckResponse;
import com.ing.mortgage.model.MortgageRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.ing.filter.TraceIdFilter.X_TRACE_ID;
import static com.ing.mortgage.model.MortgageCheckResponse.ErrorCodesEnum.HIGH_LOAN_TO_VALUE;
import static com.ing.mortgage.model.MortgageCheckResponse.ErrorCodesEnum.INSUFFICIENT_INCOME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class MortgageCheckIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setInterceptors(
                List.of((request1, body, execution) -> {
                    request1.getHeaders().add(X_TRACE_ID, UUID.randomUUID().toString());
                    return execution.execute(request1, body);
                })
        );
    }

    @Test
    void apiMortgageCheckPost_shouldReturnPositiveResult() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .income(java.math.BigDecimal.valueOf(50000))
                .loanValue(java.math.BigDecimal.valueOf(150000))
                .homeValue(java.math.BigDecimal.valueOf(200000))
                .build();


        // Act
        ResponseEntity<MortgageCheckResponse> response = restTemplate.postForEntity(url, request, MortgageCheckResponse.class);
        log.info("Response: {}", response);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().getFeasible());
    }

    @Test
    void apiMortgageCheckPost_shouldReturnNegativeResultWhenLoanIsBiggerThenHome() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .income(java.math.BigDecimal.valueOf(150000))
                .loanValue(java.math.BigDecimal.valueOf(25000))
                .homeValue(java.math.BigDecimal.valueOf(20000))
                .build();

        // Act
        ResponseEntity<MortgageCheckResponse> response = restTemplate.postForEntity(url, request, MortgageCheckResponse.class);
        log.info("Response: {}", response);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().getFeasible());
        assertEquals(1, response.getBody().getErrorCodes().size());
        assertEquals(HIGH_LOAN_TO_VALUE, response.getBody().getErrorCodes().getFirst());
    }

    @Test
    void apiMortgageCheckPost_shouldReturnNegativeResultWhenLoanIsBiggerThenIncome() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .income(java.math.BigDecimal.valueOf(1000))
                .loanValue(java.math.BigDecimal.valueOf(20000))
                .homeValue(java.math.BigDecimal.valueOf(20000))
                .build();

        // Act
        ResponseEntity<MortgageCheckResponse> response = restTemplate.postForEntity(url, request, MortgageCheckResponse.class);
        log.info("Response: {}", response);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().getFeasible());
        assertEquals(1, response.getBody().getErrorCodes().size());
        assertEquals(INSUFFICIENT_INCOME, response.getBody().getErrorCodes().getFirst());
    }

    @Test
    void apiMortgageCheckPost_shouldReturnError() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .loanValue(java.math.BigDecimal.valueOf(20000))
                .homeValue(java.math.BigDecimal.valueOf(20000))
                .build();

        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, request, ErrorResponse.class);
        log.info("Response: {}", response);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void apiInterestRatesGet_shouldReturnInterestRates() {
        // Arrange
        String url = "/api/v1/interest-rates";

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("Interest Rates Response: {}", response);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("rates"));
    }

    @Test
    void apiMortgageCheckPost_shouldReturnHighLoanToValueError() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .income(java.math.BigDecimal.valueOf(50000))
                .loanValue(java.math.BigDecimal.valueOf(250000))
                .homeValue(java.math.BigDecimal.valueOf(200000))
                .build();
        // Act
        ResponseEntity<MortgageCheckResponse> response = restTemplate.postForEntity(url, request, MortgageCheckResponse.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && !response.getBody().getFeasible());
        assertTrue(response.getBody().getErrorCodes().contains(HIGH_LOAN_TO_VALUE));
    }

    @Test
    void apiMortgageCheckPost_shouldReturnInsufficientIncomeError() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .income(java.math.BigDecimal.valueOf(10000))
                .loanValue(java.math.BigDecimal.valueOf(50000))
                .homeValue(java.math.BigDecimal.valueOf(100000))
                .build();
        // Act
        ResponseEntity<MortgageCheckResponse> response = restTemplate.postForEntity(url, request, MortgageCheckResponse.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && !response.getBody().getFeasible());
        assertTrue(response.getBody().getErrorCodes().contains(INSUFFICIENT_INCOME));
    }

    @Test
    void apiMortgageCheckPost_shouldReturnValidationErrorForNegativeIncome() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .income(java.math.BigDecimal.valueOf(-10000))
                .loanValue(java.math.BigDecimal.valueOf(50000))
                .homeValue(java.math.BigDecimal.valueOf(100000))
                .build();
        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, request, ErrorResponse.class);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().getErrorCode().equals("INVALID_REQUEST"));
    }

    @Test
    void apiMortgageCheckPost_shouldReturnValidationErrorForMissingFields() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = new java.util.HashMap<>(); // empty map, missing all fields
        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, request, ErrorResponse.class);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().getErrorCode().equals("INVALID_REQUEST"));
    }

    @Test
    void apiMortgageCheckPost_shouldReturnTraceIdInResponse() {
        // Arrange
        String url = "/api/v1/mortgage-check";
        var request = MortgageCheckRequest.builder()
                .maturityPeriod(20)
                .income(java.math.BigDecimal.valueOf(50000))
                .loanValue(java.math.BigDecimal.valueOf(150000))
                .homeValue(java.math.BigDecimal.valueOf(200000))
                .build();
        // Act
        ResponseEntity<MortgageCheckResponse> response = restTemplate.postForEntity(url, request, MortgageCheckResponse.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().getTraceId() != null);
    }
}
