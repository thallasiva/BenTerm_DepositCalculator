package com.ben.calculator.validation;

import com.ben.calculator.model.TermDepositRequest;

import java.math.BigDecimal;

public final class DepositValidator {

    private DepositValidator() {
    }

    public static void validate(TermDepositRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }
        if (request.startDeposit() == null || request.startDeposit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Please enter a valid deposit amount");
        }
        if (request.interestRatePercent() == null || request.interestRatePercent().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Please enter a valid interest rate");
        }
        if (request.investmentTermYears() <= 0) {
            throw new IllegalArgumentException("Please enter a valid investment term");
        }
        if (request.interestFrequency() == null) {
            throw new IllegalArgumentException("Please select an interest frequency");
        }
    }
}
