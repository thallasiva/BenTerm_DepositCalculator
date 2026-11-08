package com.ben.calculator.service;

import com.ben.calculator.enums.InterestFrequency;
import com.ben.calculator.model.TermDepositRequest;
import com.ben.calculator.model.TermDepositResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TermDepositCalculatorTest {

    private TermDepositCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new TermDepositCalculator();
    }

    private TermDepositRequest request(String deposit, String rate, int years, InterestFrequency freq) {
        return new TermDepositRequest(new BigDecimal(deposit), new BigDecimal(rate), years, freq);
    }

    @Test
    void testExerciseExampleAtMaturity() {
        // Example from the exercise brief: $10,000 at 1.10% for 3 years, paid at maturity -> $10,330
        TermDepositResult result = calculator.calculate(request("10000", "1.10", 3, InterestFrequency.AT_MATURITY));

        assertEquals(new BigDecimal("10330.00"), result.finalBalance());
        assertEquals(new BigDecimal("330.00"), result.totalInterestEarned());
    }

    @Test
    void testMonthlyCompounding() {
        TermDepositResult result = calculator.calculate(request("10000.00", "5.0", 3, InterestFrequency.MONTHLY));

        assertEquals(new BigDecimal("10000.00"), result.initialDeposit());
        assertEquals(new BigDecimal("11614.72"), result.finalBalance());
        assertEquals(new BigDecimal("1614.72"), result.totalInterestEarned());
    }

    @Test
    void testQuarterlyCompounding() {
        TermDepositResult result = calculator.calculate(request("10000.00", "5.0", 3, InterestFrequency.QUARTERLY));

        assertEquals(new BigDecimal("11607.55"), result.finalBalance());
        assertEquals(new BigDecimal("1607.55"), result.totalInterestEarned());
    }

    @Test
    void testAnnualCompounding() {
        TermDepositResult result = calculator.calculate(request("10000.00", "5.0", 3, InterestFrequency.ANNUALLY));

        assertEquals(new BigDecimal("11576.25"), result.finalBalance());
        assertEquals(new BigDecimal("1576.25"), result.totalInterestEarned());
    }

    @Test
    void testAtMaturityCalculation() {
        TermDepositResult result = calculator.calculate(request("10000.00", "5.0", 3, InterestFrequency.AT_MATURITY));

        assertEquals(new BigDecimal("11500.00"), result.finalBalance());
        assertEquals(new BigDecimal("1500.00"), result.totalInterestEarned());
    }

    @Test
    void testZeroInterestRateReturnsPrincipal() {
        TermDepositResult result = calculator.calculate(request("10000.00", "0", 3, InterestFrequency.MONTHLY));

        assertEquals(new BigDecimal("10000.00"), result.finalBalance());
        assertEquals(new BigDecimal("0.00"), result.totalInterestEarned());
    }

    @Test
    void testInvalidDepositThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(request("-100", "5.0", 1, InterestFrequency.ANNUALLY)));
    }

    @Test
    void testZeroDepositThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(request("0", "5.0", 1, InterestFrequency.ANNUALLY)));
    }

    @Test
    void testNegativeRateThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(request("1000", "-1", 1, InterestFrequency.ANNUALLY)));
    }

    @Test
    void testInvalidTermThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(request("1000", "5.0", 0, InterestFrequency.ANNUALLY)));
    }

    @Test
    void testNullFrequencyThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(request("1000", "5.0", 1, null)));
    }

    @Test
    void testNullRequestThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(null));
    }
}
