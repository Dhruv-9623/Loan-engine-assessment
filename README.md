# Loan Engine — RBIH Backend Assignment

A Spring Boot REST service that evaluates loan applications and determines whether a single loan offer can be approved based on the applicant's profile and requested tenure.

Built as part of the **Reserve Bank Innovation Hub (RBIH)** Java Backend Take-Home Assignment.

---

## Tech Stack

- Java 17
- Spring Boot 3.3.x
- Spring Data JPA
- H2 In-Memory Database
- Bean Validation (Jakarta)
- JUnit 5 + Mockito
- Maven

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run the application
```bash
./mvnw spring-boot:run
```

The server starts at `http://localhost:8080`

### Run tests
```bash
./mvnw clean test
```

---

## API Reference

### POST /applications

Submits a loan application for evaluation.

**Request Body**
```json
{
  "applicant": {
    "name": "Dhruv Patel",
    "age": 30,
    "monthlyIncome": 75000,
    "employmentType": "SALARIED",
    "creditScore": 720
  },
  "loan": {
    "amount": 500000,
    "tenureMonths": 36,
    "purpose": "PERSONAL"
  }
}
```

**Approved Response**
```json
{
  "applicationId": "uuid",
  "status": "APPROVED",
  "riskBand": "MEDIUM",
  "offer": {
    "interestRate": 13.5,
    "tenureMonths": 36,
    "emi": 16607.15,
    "totalPayable": 597857.40
  }
}
```

**Rejected Response**
```json
{
  "applicationId": "uuid",
  "status": "REJECTED",
  "riskBand": null,
  "rejectionReasons": [
    "CREDIT_SCORE_TOO_LOW"
  ]
}
```

---

## Validation Rules

| Field | Rule |
|---|---|
| Age | Between 21 and 60 |
| Credit Score | Between 300 and 900 |
| Loan Amount | Between 10,000 and 50,00,000 |
| Tenure | Between 6 and 360 months |
| Monthly Income | Greater than 0 |

Invalid requests return HTTP `400` with descriptive error messages.

---

## Business Rules

### Eligibility

| Rule | Rejection Reason |
|---|---|
| Credit score < 600 | `CREDIT_SCORE_TOO_LOW` |
| Age + tenure (years) > 65 | `AGE_TENURE_LIMIT_EXCEEDED` |
| EMI > 60% of monthly income | `EMI_EXCEEDS_60_PERCENT` |
| EMI > 50% of monthly income | `EMI_EXCEEDS_50_PERCENT` |

### Risk Band

| Credit Score | Risk Band |
|---|---|
| 750+ | LOW |
| 650 – 749 | MEDIUM |
| 600 – 649 | HIGH |

### Interest Rate
```
Final Rate = 12% (base)
           + Risk Premium   (LOW: 0%, MEDIUM: 1.5%, HIGH: 3%)
           + Employment     (SALARIED: 0%, SELF_EMPLOYED: 1%)
           + Loan Size      (> 10,00,000: 0.5%, otherwise: 0%)
```

### EMI Formula
```
EMI = P * r * (1+r)^n / ((1+r)^n - 1)

P = Principal
r = Monthly interest rate (annual rate / 12 / 100)
n = Tenure in months
```

All financial calculations use `BigDecimal` with `scale = 2` and `RoundingMode.HALF_UP`.

---

## Project Structure
```
src/
├── main/java/com/rbih/loanengine/
│   ├── controller/
│   │   └── LoanApplicationController.java
│   ├── service/
│   │   ├── LoanApplicationService.java
│   │   ├── EmiCalculatorService.java
│   │   ├── RiskBandService.java
│   │   ├── InterestRateService.java
│   │   └── EligibilityService.java
│   ├── domain/
│   │   ├── entity/
│   │   │   ├── LoanApplication.java
│   │   │   ├── Applicant.java
│   │   │   └── LoanRequest.java
│   │   └── enums/
│   │       ├── ApplicationStatus.java
│   │       ├── EmploymentType.java
│   │       ├── LoanPurpose.java
│   │       └── RiskBand.java
│   ├── dto/
│   │   ├── request/
│   │   │   ├── LoanApplicationRequest.java
│   │   │   ├── ApplicantRequest.java
│   │   │   └── LoanRequestDto.java
│   │   └── response/
│   │       ├── LoanApplicationResponse.java
│   │       ├── OfferResponse.java
│   │       └── ErrorResponse.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   └── repository/
│       └── LoanApplicationRepository.java
└── test/java/com/rbih/loanengine/
    └── service/
        ├── EmiCalculatorServiceTest.java
        ├── RiskBandServiceTest.java
        └── EligibilityServiceTest.java
```

---

## Sample Test Cases

| Scenario | Credit Score | Age | Income | Amount | Tenure | Expected |
|---|---|---|---|---|---|---|
| Standard approval | 720 | 30 | 75,000 | 5,00,000 | 36 | APPROVED |
| Low credit score | 550 | 30 | 75,000 | 5,00,000 | 36 | REJECTED |
| Age + tenure breach | 720 | 58 | 75,000 | 5,00,000 | 120 | REJECTED |
| EMI too high | 720 | 30 | 20,000 | 5,00,000 | 36 | REJECTED |
| High risk self employed | 620 | 30 | 2,00,000 | 15,00,000 | 60 | APPROVED |

---

## Development Notes

See [DEVELOPMENT_NOTES.md](./DEVELOPMENT_NOTES.md) for detailed information on design decisions, trade-offs, assumptions, and planned improvements.


**Dhruv Patel**
