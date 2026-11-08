package com.ben.calculator.model;

import java.math.BigDecimal;

public record TermDepositResult(
        BigDecimal initialDeposit,
        BigDecimal totalInterestEarned,
        BigDecimal finalBalance
) {
}
