package com.ben.calculator;

import com.ben.calculator.enums.InterestFrequency;
import com.ben.calculator.model.TermDepositRequest;
import com.ben.calculator.model.TermDepositResult;
import com.ben.calculator.service.TermDepositCalculator;

import java.math.BigDecimal;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        TermDepositRequest request;
        try {
            // Accept either no arguments (sample run) or exactly four.
            if (args.length != 0 && args.length != 4) {
                throw new IllegalArgumentException("Expected 4 arguments but got " + args.length);
            }
            request = args.length == 4 ? fromArgs(args) : sampleRequest();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            System.err.println("Usage: Main <deposit> <ratePercent> <years> <MONTHLY|QUARTERLY|ANNUALLY|AT_MATURITY>");
            System.exit(1);
            return;
        }
        try {
            // Validation errors from the calculator are shown to the user, not as a stack trace.
            TermDepositResult result = new TermDepositCalculator().calculate(request);
            System.out.println("=== Term Deposit Calculation Summary ===");
            System.out.println("Initial Investment : $" + result.initialDeposit());
            System.out.println("Interest Earned    : $" + result.totalInterestEarned());
            System.out.println("Final Balance      : $" + result.finalBalance());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            System.exit(1);
        }
    }

    /** Converts the raw command line text into a request, with a clear message for each bad value. */
    private static TermDepositRequest fromArgs(String[] args) {
        return new TermDepositRequest(
            parseDecimal(args[0], "Deposit"),
            parseDecimal(args[1], "Interest rate"),
            parseWholeNumber(args[2], "Term (years)"),
            parseFrequency(args[3])
        );
    }

    private static BigDecimal parseDecimal(String value, String name) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " must be a number, but was '" + value + "'");
        }
    }

    private static int parseWholeNumber(String value, String name) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " must be a whole number, but was '" + value + "'");
        }
    }

    private static InterestFrequency parseFrequency(String value) {
        try {
            return InterestFrequency.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown interest frequency '" + value
                + "'. Valid options: " + Arrays.toString(InterestFrequency.values()));
        }
    }

    /** The example from the exercise brief: expected final balance is $10,330.00. */
    private static TermDepositRequest sampleRequest() {
        return new TermDepositRequest(
            new BigDecimal("10000.00"),
            new BigDecimal("1.10"),
            3,
            InterestFrequency.AT_MATURITY
        );
    }
}
