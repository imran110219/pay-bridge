# Provider development

1. Add a provider module and safe configuration.
2. Keep HTTP DTOs/client/mapper internal.
3. Map PayBridge requests and responses without provider DTO leakage.
4. Map provider errors to `PaymentErrorCode` with retryability.
5. Advertise only tested `GatewayCapability` values.
6. Implement signature verification before webhook mapping.
7. Add reusable contract/unit tests plus WireMock HTTP cases (2xx, 4xx, 5xx, timeout, malformed body).
8. Document official sandbox prerequisites and update the README matrix.

The current provider implementations are examples of distinct patterns: Stripe is an API-first Payment Intent adapter; bKash uses a token grant and explicit confirmation after hosted approval; SSLCommerz creates a hosted session and requires customer/billing data; PortPos creates a hosted invoice and signs every server request with a time-bound credential. Do not flatten these into a common flow where that would lose security or lifecycle semantics.
