package com.ben.calculator.service;

import com.ben.calculator.enums.InterestFrequency;
import com.ben.calculator.model.TermDepositRequest;
import com.ben.calculator.model.TermDepositResult;
import com.ben.calculator.validation.DepositValidator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Calculates term deposit interest and final balance.
 *
 * <p>Uses {@link BigDecimal} instead of double so there are no floating point errors,
 * which matters for money. Intermediate values keep full precision (DECIMAL128);
 * rounding to 2 decimal places happens once, at the very end.
 */

public class TermDepositCalculator {

    /** 34 significant digits, far more precision than any realistic deposit needs. */
    private static final MathContext MC = MathContext.DECIMAL128;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    /**
     * Calculates the final balance and interest earned for a term deposit.
     *
     * @param request the deposit details
     * @return the initial deposit, total interest and final balance, all rounded to 2 decimals
     * @throws IllegalArgumentException if the request is invalid
     */
    public TermDepositResult calculate(TermDepositRequest request) {
        DepositValidator.validate(request);

        BigDecimal principal = request.startDeposit();
        // Convert percentage to a fraction, e.g. 1.10 -> 0.011
        BigDecimal annualRate = request.interestRatePercent().divide(HUNDRED, MC);
        int years = request.investmentTermYears();
        InterestFrequency frequency = request.interestFrequency();

        BigDecimal finalBalance = frequency.isCompounding()
            ? compoundBalance(principal, annualRate, years, frequency)
            : simpleBalance(principal, annualRate, years);

        // Round only at the end so rounding errors never build up.
        finalBalance = finalBalance.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalInterest = finalBalance.subtract(principal).setScale(2, RoundingMode.HALF_UP);

        return new TermDepositResult(
            principal.setScale(2, RoundingMode.HALF_UP),
            totalInterest,
            finalBalance
        );
    }

    /**
     * Simple interest, used when interest is paid at maturity: A = P * (1 + r * t).
     * Example: 10000 at 1.10% for 3 years = 10000 * (1 + 0.011 * 3) = 10330.
     */
    private BigDecimal simpleBalance(BigDecimal principal, BigDecimal annualRate, int years) {
        BigDecimal simpleInterest = principal
            .multiply(annualRate, MC)
            .multiply(BigDecimal.valueOf(years), MC);
        return principal.add(simpleInterest);
    }

    /**
     * Compound interest, used when interest is paid monthly, quarterly or annually:
     * A = P * (1 + r / n) ^ (n * t), where n is the number of periods per year.
     * Uses a single time unit (periods), as suggested in the exercise hint.
     */
    private BigDecimal compoundBalance(BigDecimal principal, BigDecimal annualRate, int years,
                                       InterestFrequency frequency) {
        int periodsPerYear = frequency.getPeriodsPerYear();
        BigDecimal ratePerPeriod = annualRate.divide(BigDecimal.valueOf(periodsPerYear), MC);
        BigDecimal growthPerPeriod = BigDecimal.ONE.add(ratePerPeriod);
        int totalPeriods = periodsPerYear * years;

        return principal.multiply(growthPerPeriod.pow(totalPeriods, MC), MC);
    }
}
