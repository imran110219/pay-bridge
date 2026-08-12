# Provider development

1. Add a provider module and safe configuration.
2. Keep HTTP DTOs/client/mapper internal.
3. Map PayBridge requests and responses without provider DTO leakage.
4. Map provider errors to `PaymentErrorCode` with retryability.
5. Advertise only tested `GatewayCapability` values.
6. Implement signature verification before webhook mapping.
7. Add reusable contract/unit tests plus WireMock HTTP cases (2xx, 4xx, 5xx, timeout, malformed body).
8. Document official sandbox prerequisites and update the README matrix.
