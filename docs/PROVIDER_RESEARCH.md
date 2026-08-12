# Provider research (2026-08-12)

Official Stripe Payment Intents documentation describes create, retrieve, cancel, capture, refund, asynchronous states and `Idempotency-Key`; its adapter is implemented here. Stripe states map `succeeded→CAPTURED`, `requires_action→REQUIRES_ACTION`, `requires_capture→AUTHORIZED`, `processing→PENDING`, and `canceled→CANCELLED`.

Official SSLCommerz documentation describes a server-created payment session (`/gwprocess/v4/api.php`), customer redirect, IPN, mandatory order validation, transaction query, and refund APIs. Its adapter implements session creation and transaction query. Its IPN must be validated remotely before fulfilment. bKash's developer material identifies URL Checkout grant-token, create, execute, and query operations; its adapter implements that sequence with short-lived cached tokens and configuration-supplied base URL/callback URI. Live provider tests require merchant sandbox credentials and are not run in CI.

Sources: Stripe Payment Intents and idempotency API reference; SSLCommerz Developer Arena / v4 integration documentation. Do not substitute tutorials for these sources.

PortWallet’s official developer site states that it is now PortPos. PortPos v2 documents hosted invoice creation at `/payment/v2/invoice`, retrieve by invoice ID, authenticated IPN validation, and partial/full refunds at `/payment/v2/invoice/refund/{invoice_id}`. Its credentials use a Base64 Bearer value built from app key and `md5(secret key + Unix timestamp)`. The `paybridge-portpos` adapter implements invoice create/retrieve/refund and retains IPN verification as follow-up work.
