package com.ben.calculator.enums;


public enum InterestFrequency {
    MONTHLY(12),
    QUARTERLY(4),
    ANNUALLY(1),
    AT_MATURITY(0);

    private final int periodsPerYear;

    InterestFrequency(int periodsPerYear) {

        this.periodsPerYear = periodsPerYear;
    }

    public int getPeriodsPerYear() {

        return periodsPerYear;
    }

    public boolean isCompounding() {

        return periodsPerYear > 0;
    }
}
