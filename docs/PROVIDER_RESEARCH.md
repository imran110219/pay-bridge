# Provider research (2026-08-12)

Official Stripe Payment Intents documentation describes create, retrieve, cancel, capture, refund, asynchronous states and `Idempotency-Key`; its adapter is implemented here. Stripe states map `succeeded→CAPTURED`, `requires_action→REQUIRES_ACTION`, `requires_capture→AUTHORIZED`, `processing→PENDING`, and `canceled→CANCELLED`.

Official SSLCommerz documentation describes a server-created payment session, customer redirect, IPN, and mandatory order validation after notification. It reports `VALID`, `FAILED`, and `CANCELLED`, but full production implementation requires merchant sandbox credentials and validation against its current contract. bKash public search access was insufficient to validate its current tokenized-checkout contract; the module is intentionally only scaffolded.

Sources: Stripe Payment Intents and idempotency API reference; SSLCommerz Developer Arena / v4 integration documentation. Do not substitute tutorials for these sources.
