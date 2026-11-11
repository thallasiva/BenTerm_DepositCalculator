package com.ben.calculator.validation;

import com.ben.calculator.model.TermDepositRequest;

import java.math.BigDecimal;

/**
 * Checks that a {@link TermDepositRequest} is sensible before any calculation happens.
 * Keeping validation here means the calculator only contains maths.
 */
public final class DepositValidator {

    private DepositValidator() {
        // utility class, not meant to be instantiated
    }

    /**
     * @param request the request to check
     * @throws IllegalArgumentException with a user-friendly message if any field is invalid
     */
    public static void validate(TermDepositRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.startDeposit() == null || request.startDeposit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Initial deposit must be greater than zero");
        }
        // A rate of exactly 0 is allowed: the balance simply stays equal to the deposit.
        if (request.interestRatePercent() == null || request.interestRatePercent().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        if (request.investmentTermYears() <= 0) {
            throw new IllegalArgumentException("Investment term must be at least 1 year");
        }
        if (request.interestFrequency() == null) {
            throw new IllegalArgumentException("Interest frequency must be specified");
        }
    }
}
