# PayBridge

**One API. Multiple payment gateways.** PayBridge is a Java 21 payment-gateway abstraction designed around ports and adapters. Its temporary Maven group/package is `com.paybridge`; replace it with an owned namespace before Central publication.

```
Application -> PayBridge API -> PaymentGatewayRegistry
                              |--> Stripe --> Stripe API
                              |--> bKash (scaffold)
                              `--> SSLCommerz (scaffold)
```

## Status and capabilities

| Provider | Payment | Query | Refund | Partial refund | Webhook | Sandbox |
|---|---:|---:|---:|---:|---:|---:|
| Stripe | ✅ | ✅ | ✅ | ✅ | 🚧 | 🧪 |
| bKash | 🚧 | 🚧 | 🚧 | 🚧 | 🚧 | 🚧 |
| SSLCommerz | 🚧 | 🚧 | 🚧 | 🚧 | 🚧 | 🧪 |

Stripe uses the documented Payment Intents REST endpoints and is HTTP-tested locally; live credentials are deliberately not required for CI. Bangladesh provider configurations are boundaries only, not invented integrations.

## Quick start

```java
var gateway = new StripePaymentGateway(new StripeConfiguration(System.getenv("STRIPE_SECRET_KEY"), null));
var payment = gateway.createPayment(new PaymentRequest(Money.of("12.50", "USD"), "order-42", "Order 42", new PaymentMethod(PaymentMethod.Type.PROVIDER_TOKEN, "pm_provider_token"), new IdempotencyKey("order-42-create"), Map.of()));
```

In Spring Boot, include the starter and configure `paybridge.stripe.enabled=true` and `paybridge.stripe.secret-key=${STRIPE_SECRET_KEY}`; inject `PaymentGatewayRegistry` and select `registry.get(PaymentProvider.STRIPE)`.

## Security and lifecycle

No raw card data belongs in PayBridge. Use a provider token or hosted checkout. Amounts are `BigDecimal`, normalized with `HALF_UP` to ISO currency fraction digits. `CREATED`, `PENDING`, `REQUIRES_ACTION`, `AUTHORIZED`, `CAPTURED`, `FAILED`, `CANCELLED`, `EXPIRED`, `PARTIALLY_REFUNDED`, and `REFUNDED` are normalized states; provider state is retained as safe metadata when mapping is lossy. Read [the security model](docs/SECURITY_MODEL.md) before integrating webhooks.

## Build, test, contribute

Run `mvn clean verify` with Java 21. `paybridge-testkit` supplies a deterministic `FakePaymentGateway` for application tests. See [provider development](docs/PROVIDER_DEVELOPMENT.md), [architecture](docs/ARCHITECTURE.md), [roadmap](docs/ROADMAP.md), and [contributing](CONTRIBUTING.md).

Apache-2.0 is included without an ownership notice; add the project owner/copyright notice before release.
