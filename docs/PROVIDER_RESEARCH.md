# Provider research (2026-08-12)

Official Stripe Payment Intents documentation describes create, retrieve, cancel, capture, refund, asynchronous states and `Idempotency-Key`; its adapter is implemented here. Stripe states map `succeeded→CAPTURED`, `requires_action→REQUIRES_ACTION`, `requires_capture→AUTHORIZED`, `processing→PENDING`, and `canceled→CANCELLED`.

Official SSLCommerz documentation describes a server-created payment session, customer redirect, IPN, mandatory order validation, transaction query, and refund APIs. Its adapter implements session creation and transaction query. Its IPN must be validated remotely before fulfilment. bKash's developer material identifies URL Checkout grant-token, create, execute, and query operations; its adapter implements that sequence with short-lived cached tokens. Live provider tests require merchant sandbox credentials and are not run in CI.

Sources: Stripe Payment Intents and idempotency API reference; SSLCommerz Developer Arena / v4 integration documentation. Do not substitute tutorials for these sources.
