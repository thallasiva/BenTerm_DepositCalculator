# Term Deposit Calculator

A command line term deposit calculator written in Java 17 with Maven, built for the BEN take home code exercise.

**Inputs:** start deposit, annual interest rate (%), term in years, interest paid (monthly, quarterly, annually, at maturity).
**Output:** initial investment, interest earned and **final balance**. All interest is reinvested for the duration of the term.

## Quick start

**Requirements:** Java 17 or newer, Maven 3.8 or newer. Check with `java -version` and `mvn -version`.

```bash
# 1. Run the tests
mvn clean test

# 2. Run the exercise example (no arguments = $10,000 at 1.10% for 3 years, at maturity)
mvn compile exec:java

# 3. Run with your own values: <deposit> <ratePercent> <years> <frequency>
mvn compile exec:java -Dexec.args="10000 1.10 3 AT_MATURITY"
```

| Argument | Meaning | Example |
|---|---|---|
| `deposit` | Start deposit amount, greater than 0 | `10000` |
| `ratePercent` | Annual rate **as a percentage**: `1.10` means 1.10%, not 110% and not 0.011 | `1.10` |
| `years` | Term in whole years, at least 1 | `3` |
| `frequency` | `MONTHLY`, `QUARTERLY`, `ANNUALLY` or `AT_MATURITY` (not case sensitive) | `AT_MATURITY` |

Running from IntelliJ IDEA: open the project as a Maven project, then run `Main`. To pass values, use
*Run > Edit Configurations > Program arguments* (for example `10000 1.10 3 AT_MATURITY`).
If results look out of date after changing code, use *Build > Rebuild Project*.

## Expected output

### The example from the exercise brief

Input: $10,000, 1.10%, 3 years, interest paid at maturity. The brief says the final balance is $10,330.

```
$ mvn compile exec:java -Dexec.args="10000 1.10 3 AT_MATURITY"

=== Term Deposit Calculation Summary ===
Initial Investment : $10000.00
Interest Earned    : $330.00
Final Balance      : $10330.00
```

### Same deposit with different payment frequencies

$10,000, 1.10%, 3 years:

| Frequency | Final balance | Interest earned |
|---|---|---|
| `AT_MATURITY` | $10,330.00 | $330.00 |
| `ANNUALLY` | $10,333.64 | $333.64 |
| `QUARTERLY` | $10,335.04 | $335.04 |
| `MONTHLY` | $10,335.35 | $335.35 |

More frequent payments give a slightly higher balance because earlier interest also earns interest.

### More examples

| Deposit | Rate | Years | Frequency | Final balance |
|---|---|---|---|---|
| 10000 | 5.10% | 3 | `AT_MATURITY` | $11,530.00 |
| 10000 | 5.10% | 3 | `MONTHLY` | $11,649.47 |
| 10000 | 5.50% | 3 | `MONTHLY` | $11,789.49 |
| 10000 | 1.50% | 3 | `MONTHLY` | $10,459.98 |
| 10000 | 5.00% | 3 | `ANNUALLY` | $11,576.25 |
| 10000 | 0% | 3 | `MONTHLY` | $10,000.00 |

You can verify any of these with an online compound interest calculator.

## Error handling

Bad input prints a short message to stderr and exits with code 1. No stack traces. Argument errors (wrong count, not a number, unknown frequency) also print a usage line, which is omitted from the examples below.

```
$ mvn compile exec:java -Dexec.args="-500 1.10 3 MONTHLY"
Invalid input: Initial deposit must be greater than zero

$ mvn compile exec:java -Dexec.args="10000 1.10 3 monthy"
Invalid input: Unknown interest frequency 'monthy'. Valid options: [MONTHLY, QUARTERLY, ANNUALLY, AT_MATURITY]

$ mvn compile exec:java -Dexec.args="abc 1.10 3 MONTHLY"
Invalid input: Deposit must be a number, but was 'abc'

$ mvn compile exec:java -Dexec.args="10000 1.10"
Invalid input: Expected 4 arguments but got 2
```

Rules checked: deposit > 0, rate >= 0, term >= 1 year, frequency must be one of the four options, numbers must be valid.

## How it works

Two formulas, both using a single time unit as suggested in the exercise hint:

- **Compound** (monthly, quarterly, annually): `A = P * (1 + r/n)^(n*t)`
  where P = deposit, r = annual rate as a fraction, n = payments per year (12, 4 or 1), t = years.
- **At maturity** (simple interest): `A = P * (1 + r*t)`

Worked example (the brief): `10000 * (1 + 0.011 * 3) = 10330`.

`BigDecimal` is used instead of `double` to avoid floating point errors with money. Calculations keep full precision
and the result is rounded to 2 decimal places once at the end (half up).

## Project structure

```
src/main/java/com/ben/calculator/
  Main.java                      CLI only: reads arguments, prints results or error messages
  model/TermDepositRequest.java  Immutable input (record)
  model/TermDepositResult.java   Immutable output (record)
  enums/InterestFrequency.java   Payment frequencies and periods per year
  validation/DepositValidator    Input rules, throws IllegalArgumentException
  service/TermDepositCalculator  The maths only, no I/O
src/test/java/com/ben/calculator/service/
  TermDepositCalculatorTest      12 JUnit 5 tests
```

Separation of concerns: `Main` handles input and output, `DepositValidator` handles rules, `TermDepositCalculator` handles
maths. The calculator can be reused from a UI or web service without changes.

## Tests

`mvn clean test` runs 12 JUnit 5 tests covering:

- The exercise example ($10,330.00)
- Monthly, quarterly, annual and at maturity calculations
- Zero interest rate
- Invalid input: zero or negative deposit, negative rate, zero term, missing frequency, null request

Expected values were calculated independently of the Java code, not copied from its output.

## Assumptions and limitations

- Interest rate is annual and fixed for the whole term.
- Term is in whole years (no months or days).
- All interest is reinvested, as the exercise states.
- Currency is not specified; amounts are shown with a `$` sign.