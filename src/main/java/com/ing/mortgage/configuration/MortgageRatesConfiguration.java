package com.ing.mortgage.configuration;

import com.ing.mortgage.model.MortgageRateDb;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mortgage")
@Getter
@Setter
public class MortgageRatesConfiguration {
    private List<MortgageRateDb> rates;
}
