# PayBridge

**One API. Multiple payment gateways.** PayBridge is a Java 21 payment-gateway abstraction designed around ports and adapters. Its temporary Maven group/package is `com.paybridge`; replace it with an owned namespace before Central publication.

```
Application -> PayBridge API -> PaymentGatewayRegistry
                              |--> Stripe --> Stripe API
                              |--> bKash URL Checkout --> bKash API
                              |--> SSLCommerz Hosted Checkout --> SSLCommerz API
                              `--> PortPos Hosted Invoice --> PortPos API
```

## Status and capabilities

| Provider | Payment | Query | Refund | Partial refund | Webhook | Sandbox |
|---|---:|---:|---:|---:|---:|---:|
| Stripe | ✅ | ✅ | ✅ | ✅ | 🚧 | 🧪 |
| bKash | ✅ | ✅ | ❌ | ❌ | ❌ | 🧪 |
| SSLCommerz | ✅ | ✅ | ❌ | ❌ | ❌ | 🧪 |
| PortWallet / PortPos | ✅ | ✅ | ✅ | ✅ | ❌ | 🧪 |

All implemented flows are WireMock-tested; live credentials are deliberately not required for CI. bKash supports its URL Checkout grant-token → create → customer approval → execute → query sequence. SSLCommerz supports hosted-session creation and transaction query. Neither Bangladesh adapter currently exposes refunds or verified webhook handling. Their refund APIs require a bank/provider transaction identifier that V0.1's normalized `RefundRequest` does not yet carry; SSLCommerz IPN must additionally be validated server-to-server before fulfilment.

PortWallet is now **PortPos**. The PortPos v2 adapter creates hosted invoices, retrieves invoice status, and submits full or partial refunds. It is configured with an application/secret key and generates the documented short-lived Bearer value for each request. It requires a customer name, email, phone, and billing address (including state). Its IPN validation endpoint is documented but not yet exposed through PayBridge’s webhook abstraction.

## Modules

| Module | Responsibility |
|---|---|
| `paybridge-core` | Framework-independent payment domain and normalized errors |
| `paybridge-spi` | Gateway port, capability model, and registry |
| `paybridge-webhooks` | Provider-neutral verified webhook contracts |
| `paybridge-testkit` | Deterministic in-memory gateway for application tests |
| `paybridge-stripe`, `paybridge-bkash`, `paybridge-sslcommerz`, `paybridge-portpos` | Isolated provider adapters |
| `paybridge-spring-boot-starter` | Spring Boot registration for Stripe, bKash, and SSLCommerz |

## Quick start

```java
var gateway = new StripePaymentGateway(new StripeConfiguration(System.getenv("STRIPE_SECRET_KEY"), null));
var payment = gateway.createPayment(new PaymentRequest(Money.of("12.50", "USD"), "order-42", "Order 42", new PaymentMethod(PaymentMethod.Type.PROVIDER_TOKEN, "pm_provider_token"), new IdempotencyKey("order-42-create"), Map.of()));
```

For bKash, create a hosted checkout payment and redirect the customer to `clientActionUrl`; after the customer returns to your callback, call `gateway.confirm(reference)`, then query if needed. SSLCommerz similarly returns a `clientActionUrl`; treat its return URL as informational and query/validate server-side before fulfilment.

In Spring Boot, include the starter, enable one or more configured gateways, inject `PaymentGatewayRegistry`, and select a gateway by provider. For example:

```yaml
paybridge:
  bkash:
    enabled: true
    app-key: ${BKASH_APP_KEY}
    app-secret: ${BKASH_APP_SECRET}
    username: ${BKASH_USERNAME}
    password: ${BKASH_PASSWORD}
    base-uri: https://checkout.sandbox.bka.sh
    callback-uri: https://merchant.example/payments/bkash/callback
  sslcommerz:
    enabled: true
    store-id: ${SSLCOMMERZ_STORE_ID}
    store-password: ${SSLCOMMERZ_STORE_PASSWORD}
    base-uri: https://sandbox.sslcommerz.com
    success-uri: https://merchant.example/payments/ssl/success
    failure-uri: https://merchant.example/payments/ssl/failure
    cancel-uri: https://merchant.example/payments/ssl/cancel
    ipn-uri: https://merchant.example/webhooks/sslcommerz
```

`PaymentRequest.customer` and its billing address are required for SSLCommerz session creation. bKash and SSLCommerz base URLs are configuration, not hardcoded API assumptions; use the current values assigned in merchant onboarding.

PortPos is available as a direct adapter today; its Spring Boot property binding/auto-registration is planned. Configure it from environment-backed values, for example:

```java
var portPos = new PortPosPaymentGateway(new PortPosConfiguration(
    System.getenv("PORTPOS_APP_KEY"), System.getenv("PORTPOS_SECRET_KEY"),
    java.net.URI.create(System.getenv("PORTPOS_BASE_URI")),
    java.net.URI.create("https://merchant.example/payments/portpos/return"),
    java.net.URI.create("https://merchant.example/webhooks/portpos")));
```

## Security and lifecycle

No raw card data belongs in PayBridge. Use a provider token or hosted checkout. Amounts are `BigDecimal`, normalized with `HALF_UP` to ISO currency fraction digits. `CREATED`, `PENDING`, `REQUIRES_ACTION`, `AUTHORIZED`, `CAPTURED`, `FAILED`, `CANCELLED`, `EXPIRED`, `PARTIALLY_REFUNDED`, and `REFUNDED` are normalized states; provider state is retained as safe metadata when mapping is lossy. Read [the security model](docs/SECURITY_MODEL.md) before integrating webhooks.

## Build, test, contribute

Run `mvn clean verify` with Java 21 (the published bytecode target). `paybridge-testkit` supplies a deterministic `FakePaymentGateway` for application tests. See [provider development](docs/PROVIDER_DEVELOPMENT.md), [architecture](docs/ARCHITECTURE.md), [roadmap](docs/ROADMAP.md), and [contributing](CONTRIBUTING.md).

Apache-2.0 is included without an ownership notice; add the project owner/copyright notice before release.
