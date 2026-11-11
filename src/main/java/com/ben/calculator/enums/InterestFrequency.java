package com.ben.calculator.enums;

/**
 * How often interest is paid on a term deposit.
 *
 * <p>Because all proceeds are reinvested for the duration of the term, paying interest
 * more often means interest starts earning interest sooner (compounding).
 * {@link #AT_MATURITY} is the exception: interest is paid once at the end of the term,
 * so nothing is reinvested and simple interest applies.
 */

public enum InterestFrequency {
    /** Interest paid and reinvested 12 times a year. */
    MONTHLY(12),
    /** Interest paid and reinvested 4 times a year. */
    QUARTERLY(4),
    /** Interest paid and reinvested once a year. */
    ANNUALLY(1),
    /** Interest paid once at the end of the term (simple interest, no compounding). */
    AT_MATURITY(0);

    private final int periodsPerYear;

    InterestFrequency(int periodsPerYear) {
        this.periodsPerYear = periodsPerYear;
    }

    /** @return number of compounding periods per year; 0 for {@link #AT_MATURITY} */
    public int getPeriodsPerYear() {
        return periodsPerYear;
    }

    /**
     * Tells the calculator which formula to use.
     *
     * @return true if interest compounds during the term, false for {@link #AT_MATURITY}
     */
    public boolean isCompounding() {
        return periodsPerYear > 0;
    }
}
