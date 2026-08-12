# Architecture

The Maven modules enforce dependency direction: `core` contains immutable domain types and normalized errors; `spi` declares the provider port/registry; `webhooks` declares verified-event contracts; provider modules adapt internal HTTP models; Spring is an outer adapter. `testkit` is an in-memory adapter.

Provider adapters follow domain → mapper/client → provider API. They expose no provider DTOs. Unsupported capability is explicit, rather than a pretend common feature. An application creates an instance-scoped `PaymentGatewayRegistry`; no global registry exists.

The core has no persistence or distributed idempotency dependency. `IdempotencyKey` always travels with create/refund calls, while a future application adapter can persist request/result pairs and provider adapters forward the key where supported. The SPI includes `CONFIRM_PAYMENT` for hosted flows where a customer approves externally and the provider requires a separate execution call; bKash is the first use of this capability.

`PaymentRequest` may contain an immutable `Customer` and `BillingAddress`. They are optional in the core but required by the SSLCommerz and PortPos hosted-session adapters because their public session contracts require customer data. PortPos additionally requires `Customer.phone` and `BillingAddress.state`. Provider metadata holds only safe operational references, such as an SSLCommerz session key or bKash transaction identifier; it is not a substitute for a durable merchant payment record.
