package com.ing;

import com.ing.configuration.MortgageRatesConfiguration;
import com.ing.model.MortgageRateDb;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class ApplicationIT {

	@Autowired
	MortgageRatesConfiguration mortgageRatesConfiguration;

	@Test
	void contextLoads() {
	}

	@Test
	void configurationTest() {
		List<MortgageRateDb> rates = mortgageRatesConfiguration.getRates();
		assert(!rates.isEmpty());
	}

}
