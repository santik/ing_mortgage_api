package com.ing.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ing.mortgage.model.MortgageCheckRequest;
import com.ing.mortgage.model.MortgageCheckResponse;
import com.ing.mortgage.model.MortgageRatesResponse;
import com.ing.service.MortgageCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

class MortgageControllerTest {

    @Mock
    private MortgageCheckService mortgageCheckService;

    private MortgageController mortgageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mortgageController = new MortgageController(mortgageCheckService);
    }

    @Test
    void testInterestRatesGet_returnsRatesResponse() {
        UUID xTraceId = UUID.randomUUID();
        MortgageRatesResponse mockResponse = MortgageRatesResponse.builder().build();
        when(mortgageCheckService.getAllInterestRates()).thenReturn(mockResponse);

        ResponseEntity<MortgageRatesResponse> response = mortgageController.getInterestRates(xTraceId);

        assertEquals(mockResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mortgageCheckService).getAllInterestRates();
    }

    @Test
    void testMortgageCheckPost_returnsCheckResponse() {
        UUID xTraceId = UUID.randomUUID();
        MortgageCheckRequest request = new MortgageCheckRequest();
        MortgageCheckResponse mockResponse = new MortgageCheckResponse();
        when(mortgageCheckService.checkMortgage(request)).thenReturn(mockResponse);

        ResponseEntity<MortgageCheckResponse> response = mortgageController.checkMortgage(request, xTraceId);

        assertEquals(mockResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mortgageCheckService).checkMortgage(request);
    }

}