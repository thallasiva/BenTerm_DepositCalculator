# Term Deposit Calculator

A Java command line application that calculates term deposit balances and interest earned using exact decimal arithmetic.

## Features
- Exact financial arithmetic with `BigDecimal` and `MathContext.DECIMAL128`, rounded to 2 decimals (HALF_UP).
- Compounding options: Monthly, Quarterly, Annually, or simple interest At Maturity.
- Input validation with clear error messages.
- Unit tests with JUnit 5.

## Requirements
- Java 17+
- Maven 3.8+

## Build and Test
```bash
mvn clean test
```

## Run
```bash
# Sample calculation
mvn compile exec:java

# Custom input: <deposit> <ratePercent> <years> <frequency>
# Rate is in percent: 1.10 means 1.10%. Frequency: MONTHLY, QUARTERLY, ANNUALLY or AT_MATURITY
mvn compile exec:java -Dexec.args="10000 1.10 3 AT_MATURITY"
```

## Assumptions
- All interest is reinvested for the duration of the term.
- Annual interest rate is given as a percentage; term is in whole years.
- Results are rounded to 2 decimal places (HALF_UP).

## Formulas
- Compound: `A = P * (1 + r/n)^(n*t)`
- At maturity (simple): `A = P * (1 + r*t)`
