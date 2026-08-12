# Security model

PayBridge never receives raw card numbers, CVV/CVC, magnetic stripe, or authentication data. The public API accepts only provider-generated tokens or hosted-checkout instructions. Credentials are injected at runtime and safe `toString()` methods redact secrets. Telemetry/logging must include only payment/provider references and safe error codes.

Webhook adapters must verify signatures before parsing/dispatching, enforce provider timestamp windows, deduplicate event IDs in an application-owned store, tolerate reordering, and reject malformed/unknown authenticity data. A failed signature is never accepted.

PortPos sends IPN data containing invoice, amount, status, and merchant reference. PayBridge does not yet expose PortPos IPN processing; applications must not trust an IPN or browser return alone until the adapter calls PortPos's authenticated IPN-validation endpoint and the event is deduplicated.
