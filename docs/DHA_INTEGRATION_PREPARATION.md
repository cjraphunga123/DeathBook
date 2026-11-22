# DeathBook — DHA integration preparation

**Status:** Preparation only. No DHA access, accreditation, data-sharing agreement, API credentials, or production connection has been obtained.

## Operating model

The founder currently operates as an individual. DeathBook is intended to serve:
- members of the public who submit a request or ask for help with a record; and
- verified institutional users with a lawful, approved purpose.

Before applying for production access, ask DHA whether an individual/sole proprietor may apply or whether a registered juristic person is required for this service. Obtain South African legal advice on the suitable operating structure and accreditation route; do not assume incorporation alone grants eligibility. Do not represent DeathBook as a DHA partner, agent, or approved verification provider.

## Official route

1. Contact the Department of Home Affairs verification service at **verifications@dha.gov.za** to request the current third-party onboarding pack and written requirements for the intended use case.
2. Ask DHA to confirm whether DeathBook can apply as a private-sector applicant, what entity form is required, whether it can verify death/life-event status (not merely identity), what evidence of lawful purpose and consent is required, the applicable tariffs, technical specifications, security assessment, test environment, and accreditation process.
3. Obtain written approval, any required data-sharing agreement, technical documentation, test credentials, and production authorisation before connecting.
4. Confirm the current legal status and applicable provisions of the DHA regulations and Gazette 54610 dated 4 May 2026 with DHA or South African legal counsel before relying on them.

DHA public notices describe its Online Verification System (OVS) and direct prospective users to the email above. Published notices describe fees for verification transactions; request the current fee schedule and billing terms directly from DHA.

## Product access model

### Public users
- Public users may create a request and provide information only after a clear privacy notice and a lawful basis have been established.
- Public users must not be able to search the National Population Register or look up another person's death status at will.
- Do not expose certificates, identity numbers, or underlying register data to unrelated users.
- Provide a request reference and explain that a request is pending until checked by an authorised process.

### Institutional users
- Onboard each institution separately and verify its legal identity, authorised purpose, named users, and approval.
- Apply least-privilege roles, MFA, per-user attribution, periodic access reviews, and immediate revocation when access ends.
- Permit a query only when the institution's approved purpose and legal basis are recorded.
- Never let an institution use DeathBook to conduct bulk or speculative searches unless DHA has explicitly approved that use.

## Proposed architecture

Android app -> DeathBook backend -> DHA-approved gateway/API.

The Android app must never contain DHA credentials or call DHA directly. Credentials belong in a managed server-side secret store. The backend must enforce authentication, authorisation, rate limits, purpose limitation, input validation, encryption in transit and at rest, and audit logging.

No endpoint, request schema, token flow, or API URL is assumed here. Implement the DHA adapter only from DHA-issued technical specifications. Until then, use a clearly labelled mock/demo adapter that cannot return a real-world "officially verified" result.

## Result semantics

Use explicit states:
- `PENDING`: no authoritative result yet.
- `CONFIRMED_REGISTERED`: only when returned by an approved DHA interface and mapped exactly as DHA specifies.
- `NOT_CONFIRMED`: no match or no authoritative confirmation; this must not be described as proof that the person is alive or that no death occurred.
- `UNAVAILABLE`: DHA service unavailable or request failed.
- `REVIEW_REQUIRED`: ambiguous or inconsistent response requiring an authorised human review.

Store the source, request reference, timestamp, requesting institution/user, approved purpose, and response code needed for audit. Do not store more source data than is necessary and authorised.

## Privacy, security and governance gate

Before collecting real records:
- Obtain South African privacy/legal advice on POPIA roles, lawful basis, transparency notices, data-subject rights, retention, cross-border hosting, and whether prior authorisation is required for the proposed processing.
- Register the responsible party's Information Officer with the Information Regulator where required.
- Complete a privacy impact assessment and threat model.
- Put written operator agreements in place where service providers process information for DeathBook.
- Define incident response, breach notification, access correction, deletion/retention, backups, and secure disposal procedures.
- Use synthetic test data only until approvals and safeguards are in place.

DHA Gazette 54610 (4 May 2026) describes real-time verification for accredited trusted entities through secure APIs or other approved connectivity, subject to applicable data-sharing agreements, accreditation, technical/security standards, and logging. It also describes a minimum seven-year retention period for the specified access/query logs. Confirm applicability with DHA and counsel before implementation.

## Immediate action checklist

- [ ] Ask DHA whether an individual/sole proprietor is eligible or whether a registered company/other juristic person is required; then choose the appropriate operating structure.
- [ ] Obtain legal advice on the appropriate applicant and POPIA role.
- [ ] Email DHA at verifications@dha.gov.za requesting current onboarding requirements for death/life-event verification.
- [ ] Ask for the exact accreditation criteria, data-sharing agreement, permitted user groups, technical specification, fees, test access, and production approval process.
- [ ] Register the Information Officer and complete privacy/security governance as applicable.
- [ ] Build and test the backend adapter only after receiving official specifications.
- [ ] Complete DHA testing and receive written production authorisation.

## Important limitation

This document is an engineering and governance preparation plan, not legal advice or evidence of DHA approval. DeathBook currently has no official DHA connection and must not claim to register, certify, or verify deaths.
