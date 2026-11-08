package com.ben.calculator.model;

import com.ben.calculator.enums.InterestFrequency;

import java.math.BigDecimal;

public record TermDepositRequest(
        BigDecimal startDeposit,
        BigDecimal interestRatePercent,
        int investmentTermYears,
        InterestFrequency interestFrequency
) {
}
