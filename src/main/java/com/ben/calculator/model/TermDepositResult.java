package com.ben.calculator.model;

import java.math.BigDecimal;

/**
 * Immutable output of a term deposit calculation. All amounts are rounded to 2 decimal places.
 *
 * @param initialDeposit      the amount deposited at the start
 * @param totalInterestEarned interest earned over the whole term (finalBalance - initialDeposit)
 * @param finalBalance        the balance at the end of the term, including all interest
 */
public record TermDepositResult(
    BigDecimal initialDeposit,
    BigDecimal totalInterestEarned,
    BigDecimal finalBalance
) {}
