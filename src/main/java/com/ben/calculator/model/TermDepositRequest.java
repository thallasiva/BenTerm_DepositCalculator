package com.ben.calculator.model;

import com.ben.calculator.enums.InterestFrequency;

import java.math.BigDecimal;

/**
 * Immutable input for a term deposit calculation.
 *
 * @param startDeposit        amount deposited at the start, e.g. 10000.00
 * @param interestRatePercent annual interest rate as a percentage, e.g. 1.10 means 1.10%
 * @param investmentTermYears length of the deposit in whole years, e.g. 3
 * @param interestFrequency   how often interest is paid (monthly, quarterly, annually, at maturity)
 */
public record TermDepositRequest(
    BigDecimal startDeposit,
    BigDecimal interestRatePercent,
    int investmentTermYears,
    InterestFrequency interestFrequency
) {}
