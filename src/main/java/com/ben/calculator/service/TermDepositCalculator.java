package com.ben.calculator.service;

import com.ben.calculator.enums.InterestFrequency;
import com.ben.calculator.model.TermDepositRequest;
import com.ben.calculator.model.TermDepositResult;
import com.ben.calculator.validation.DepositValidator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TermDepositCalculator {

    private static final MathContext MC = MathContext.DECIMAL128;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public TermDepositResult calculate(TermDepositRequest request) {
        DepositValidator.validate(request);

        BigDecimal principal = request.startDeposit();
        BigDecimal annualRate = request.interestRatePercent().divide(HUNDRED, MC);
        int years = request.investmentTermYears();
        InterestFrequency frequency = request.interestFrequency();

        BigDecimal finalBalance;

        if (!frequency.isCompounding()) {
            BigDecimal simpleInterest = principal.multiply(annualRate, MC).multiply(BigDecimal.valueOf(years), MC);
            finalBalance = principal.add(simpleInterest);
        } else {
            BigDecimal n = BigDecimal.valueOf(frequency.getPeriodsPerYear());
            BigDecimal ratePerPeriod = annualRate.divide(n, MC);
            BigDecimal base = BigDecimal.ONE.add(ratePerPeriod);
            int totalPeriods = frequency.getPeriodsPerYear() * years;

            BigDecimal compoundedFactor = base.pow(totalPeriods, MC);
            finalBalance = principal.multiply(compoundedFactor, MC);
        }

        finalBalance = finalBalance.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalInterest = finalBalance.subtract(principal).setScale(2, RoundingMode.HALF_UP);

        return new TermDepositResult(
                principal.setScale(2, RoundingMode.HALF_UP),
                totalInterest,
                finalBalance
        );
    }
}
