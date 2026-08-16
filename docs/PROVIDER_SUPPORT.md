# Provider support and readiness

This is PayBridge's canonical support matrix. README and provider guides must agree with it. A capability is **supported** only when it is exposed through the public SPI, implemented by the adapter, and covered by an automated adapter test. Provider sandbox validation is a separate, stronger status.

## Readiness terms

| Mark | Meaning |
|---|---|
| ✅ | Implemented and covered by local automated tests. |
| 🧪 | Implemented but only mock/contract tested; no provider sandbox validation is recorded. |
| 🚧 | Planned or partially designed; not available to applications. |
| ❌ | Not implemented or intentionally unavailable. |

No provider currently has a recorded credential-backed sandbox test. Therefore no provider is sandbox-validated yet, even where a provider offers a sandbox.

## Provider capabilities

| Provider | Create | Confirm | Query | Refund | Partial refund | Cancel | Capture | Hosted checkout | Verified webhooks | Automated evidence |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| Stripe | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ | ✅ | ❌ | ❌ | WireMock create-payment/idempotency test |
| bKash URL Checkout | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ | ✅ | ❌ | WireMock token-grant and hosted-create test |
| SSLCommerz Hosted Checkout | ✅ | ❌ | ✅ | ❌ | ❌ | ❌ | ❌ | ✅ | ❌ | WireMock session-create and transaction-query tests |
| PortPos (formerly PortWallet) | ✅ | ❌ | ✅ | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ | WireMock invoice-create and refund-submission tests |

### Important notes

- Stripe has `WEBHOOKS` in its declared capability set and SSLCommerz has `WEBHOOKS` in its declared capability set, but neither module provides a `WebhookProcessor` nor webhook verification tests. Treat this as an implementation defect to resolve before advertising webhook support. The documentation intentionally marks webhooks unsupported until then.
- “Refund” means a refund request can be submitted. The current adapters do not provide refund-status reconciliation; PortPos returns `PENDING` unless its immediate response is `REFUNDED`.
- bKash is a hosted approval flow. After the customer returns to the merchant callback, call `PaymentGateway.confirm(reference)` and then query/reconcile as appropriate.
- SSLCommerz browser callbacks are not proof of payment. Query/validate server-side before fulfilment. A PayBridge webhook/IPN processor is not yet available.
- PortPos IPN validation is documented by the provider but not implemented in `paybridge-webhooks`.

## Configuration and registration

| Provider | Direct adapter | Spring Boot property binding / auto-registration |
|---|---:|---:|
| Stripe | ✅ | 🚧 — auto-configuration class exists but is not registered through Spring Boot metadata |
| bKash | ✅ | 🚧 — same registration limitation |
| SSLCommerz | ✅ | 🚧 — same registration limitation |
| PortPos | ✅ | ❌ — no properties or registry wiring yet |

The Spring Boot starter must add `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` and an integration test before the Spring Boot column may be marked supported.
