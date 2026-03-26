# Development Notes

## Overall Approach

Built a layered Spring Boot REST service to evaluate loan applications and generate a single offer based on the requested tenure. The architecture follows a strict separation of concerns — Controller handles HTTP, Service handles business logic, Repository handles persistence, and Domain holds the core entities.

## Key Design Decisions

- **Embedded entities over separate tables**: `Applicant` and `LoanRequest` are modelled as `@Embeddable` and embedded into `LoanApplication`. This avoids unnecessary joins for a single-aggregate domain and keeps audit storage simple.
- **BigDecimal throughout financial calculations**: All EMI, interest rate, and income comparisons use `BigDecimal` with `scale = 2` and `RoundingMode.HALF_UP` to avoid floating-point precision errors.
- **Separation of eligibility vs offer limit**: Eligibility uses a 60% EMI-to-income threshold as a hard rejection gate. Offer generation applies a stricter 50% threshold. Both are evaluated independently and clearly named.
- **Static factory methods on response DTOs**: `LoanApplicationResponse.approved()` and `.rejected()` make intent explicit at the call site and avoid partially constructed objects.
- **H2 in-memory database**: Chosen for simplicity and zero infrastructure setup. Sufficient for audit storage in a take-home context.

## Trade-offs Considered

- **No `@Service` abstraction interface**: Skipped interfaces for service classes since there is only one implementation. Adding them would add indirection without benefit here.
- **No pagination on repository**: Only a single POST endpoint is required. Query endpoints are out of scope.
- **Enums validated via `@NotNull`**: Invalid enum string values (e.g. `"UNKNOWN"`) will result in a deserialization error from Jackson, which surfaces as a 400 naturally without extra handling.

## Assumptions Made

- Tenure in years for the age + tenure rule is calculated as `tenureMonths / 12.0` (decimal division, not ceiling).
- The 60% EMI check and the 50% offer limit are evaluated separately — an application can fail eligibility on 60% before reaching the offer stage.
- `purpose` field on the loan is stored but does not influence interest rate or eligibility in the current ruleset.
- Application IDs are generated server-side using UUID v4 via `@PrePersist`.

## Improvements With More Time

- Add integration tests using `@SpringBootTest` and `MockMvc` covering the full request-response cycle.
- Add `GET /applications/{id}` endpoint for audit retrieval.
- Externalise business rule constants (base rate, thresholds) into `application.properties` for configurability.
- Add structured logging with correlation IDs per request.
- Introduce `@ControllerAdvice` handling for `HttpMessageNotReadableException` to return clean 400s for invalid enum values.
- Replace H2 with PostgreSQL for a production-ready setup with Flyway migrations.