# Architecture

The Maven modules enforce dependency direction: `core` contains immutable domain types and normalized errors; `spi` declares the provider port/registry; `webhooks` declares verified-event contracts; provider modules adapt internal HTTP models; Spring is an outer adapter. `testkit` is an in-memory adapter.

Provider adapters follow domain → mapper/client → provider API. They expose no provider DTOs. Unsupported capability is explicit, rather than a pretend common feature. An application creates an instance-scoped `PaymentGatewayRegistry`; no global registry exists.

The core has no persistence or distributed idempotency dependency. `IdempotencyKey` always travels with create/refund calls, while a future application adapter can persist request/result pairs and provider adapters forward the key where supported.
