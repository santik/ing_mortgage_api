package com.ing.controller;

import com.ing.mortgage.api.DefaultApi;
import com.ing.mortgage.model.MortgageCheckRequest;
import com.ing.mortgage.model.MortgageCheckResponse;
import com.ing.mortgage.model.MortgageRatesResponse;
import com.ing.service.MortgageCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("${server.servletPath}")
@RequiredArgsConstructor
@Slf4j
public class MortgageController implements DefaultApi {

  private final MortgageCheckService mortgageCheckService;

  /**
   * Handles GET requests for current mortgage interest rates.
   *
   * @param xTraceId the trace ID for request tracking
   * @return a response entity containing all mortgage rates
   */
  @Override
  public ResponseEntity<MortgageRatesResponse> getInterestRates(UUID xTraceId) {
    log.info("Received request to get interest rates, x-trace-id: {}", xTraceId);
    return ResponseEntity.ok(mortgageCheckService.getAllInterestRates());
  }

  /**
   * Handles POST requests to check mortgage feasibility.
   *
   * @param mortgageCheckRequest the mortgage check request payload
   * @param xTraceId the trace ID for request tracking
   * @return a response entity containing the mortgage check result
   */
  @Override
  public ResponseEntity<MortgageCheckResponse> checkMortgage(@Valid MortgageCheckRequest mortgageCheckRequest, UUID xTraceId) {
    log.info("Received mortgage check request: {}, x-trace-id: {}", mortgageCheckRequest, xTraceId);
    return ResponseEntity.ok(mortgageCheckService.checkMortgage(mortgageCheckRequest));
  }
}
