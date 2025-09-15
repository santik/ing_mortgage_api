# ING Mortgage API

A Spring Boot application that provides RESTful endpoints for mortgage calculations and interest rate information. The API is designed to help users check mortgage feasibility and retrieve current interest rates, using configurable business rules and in-memory data storage.

## Features
- **Mortgage Feasibility Check**: Submit a request to determine if a mortgage is feasible based on provided parameters.
- **Interest Rates Endpoint**: Retrieve current mortgage interest rates.
- **Business Rules Engine**: Modular business rules for mortgage validation.
- **Trace ID Support**: Each request can be traced using a UUID for debugging and logging.
- **Exception Handling**: Centralized error handling for API responses.

## Business Rules

The mortgage feasibility check applies the following business rules:

### MaxMortgageForHomeValue
- **Rule:** The requested loan value must not exceed the home value.
- **Error Code:** `HIGH_LOAN_TO_VALUE` if violated.
- **Purpose:** Prevents mortgages that exceed the property value.

### MaxMortgageForIncome
- **Rule:** The requested loan value must not exceed four times the applicant's income.
- **Error Code:** `INSUFFICIENT_INCOME` if violated.
- **Purpose:** Ensures the mortgage is affordable based on income.

### Same Currency
- **Rule:** The currency of the loan must match the currency of the home value and income.
- **Error Code:** `CURRENCY_MISMATCH` if violated.
- **Purpose:** Avoids complications from currency conversion.

These rules are modular and can be extended for additional business requirements.

## Project Structure
- `src/main/java/com/ing/` - Main application code
  - `controller/` - REST controllers
  - `service/` - Business logic and services
  - `businessrules/` - Mortgage business rules engine
  - `model/` - Data models
  - `repository/` - Data access layer (in-memory)
  - `configuration/` - Application configuration
  - `filter/` - Request filters (e.g., trace ID)
- `src/main/resources/` - Configuration files
- `src/test/java/com/ing/` - Integration and unit tests

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.6+

### Build & Run
```bash
./mvnw clean install
./mvnw spring-boot:run
```
The application will start on the default port (usually 8080).

### API Endpoints
- `GET /v1/interest-rates` - Retrieve current interest rates
- `POST /v1/mortgage-check` - Check mortgage feasibility

See `src/main/resources/api/producer/MortgageAPIv1.yaml` for the OpenAPI specification.

## Testing
Run all tests with:
```bash
./mvnw test
```
Or look at [github actions](https://github.com/santik/ing_mortgage_api/actions) for CI results.


## Configuration
- Application settings: `src/main/resources/application.yaml`

## License
This project is for demonstration purposes and may not be licensed for production use.


