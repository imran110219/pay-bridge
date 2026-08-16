# PayBridge

**One API. Multiple payment gateways.**

PayBridge is a Java 21 payment-gateway abstraction for applications that need to integrate more than one provider without exposing provider DTOs throughout their codebase. It uses a framework-independent core, a small provider SPI, isolated adapters, and a deterministic test gateway.

> Status: pre-release foundation. The adapters are locally HTTP-tested with WireMock; no provider has credential-backed sandbox validation yet.

## Choose your path

- [Understand provider support and readiness](docs/PROVIDER_SUPPORT.md)
- [Use the core architecture and SPI](docs/ARCHITECTURE.md)
- [Understand payment lifecycle mapping](docs/PAYMENT_LIFECYCLE.md)
- [Integrate PortPos / former PortWallet](docs/PORTPOS.md)
- [Add a payment provider](docs/PROVIDER_DEVELOPMENT.md)
- [Review security boundaries](docs/SECURITY_MODEL.md)
- [See planned milestones](docs/ROADMAP.md)

## What it solves

```text
Application
    |
    v
PayBridge domain + SPI
    |
    v
PaymentGatewayRegistry
    |-------------------|----------------------|-------------------|
    v                   v                      v                   v
Stripe              bKash URL Checkout   SSLCommerz Hosted   PortPos Hosted Invoice
```

Applications use `PaymentRequest`, `Payment`, `RefundRequest`, normalized statuses, and normalized errors. Provider-specific HTTP payloads stay inside provider modules.

## Current support

| Provider | Create | Confirm | Query | Refund | Partial refund | Hosted checkout | Verified webhooks |
|---|---:|---:|---:|---:|---:|---:|---:|
| Stripe | ✅ | ❌ | ✅ | ✅ | ✅ | ❌ | ❌ |
| bKash URL Checkout | ✅ | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| SSLCommerz Hosted Checkout | ✅ | ❌ | ✅ | ❌ | ❌ | ✅ | ❌ |
| PortPos (formerly PortWallet) | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ | ❌ |

`✅` means implemented and locally automated-tested. `❌` means unavailable. See the [canonical support matrix](docs/PROVIDER_SUPPORT.md) for capability declarations, evidence, sandbox posture, and known gaps.

## Modules

| Module | Purpose |
|---|---|
| `paybridge-core` | Framework-independent immutable domain model and normalized errors |
| `paybridge-spi` | `PaymentGateway`, capabilities, and instance-scoped registry |
| `paybridge-webhooks` | Provider-neutral webhook contracts; no production provider processor yet |
| `paybridge-testkit` | Deterministic in-memory fake gateway |
| `paybridge-providers/*` | Isolated Stripe, bKash, SSLCommerz, and PortPos adapters |
| `paybridge-spring-boot-starter` | Work-in-progress Spring Boot integration |
| `examples/spring-boot-basic` | Minimal Spring Boot application shell |

## Quick start: direct Java integration

Add the relevant provider module to your Maven build, then create an adapter and register it. Package coordinates are temporary (`com.paybridge`) and must be replaced with an owned namespace before publication.

```java
var stripe = new StripePaymentGateway(
    new StripeConfiguration(System.getenv("STRIPE_SECRET_KEY"), null));

var registry = new PaymentGatewayRegistry(List.of(stripe));
var gateway = registry.get(PaymentProvider.STRIPE);

var payment = gateway.createPayment(new PaymentRequest(
    Money.of("12.50", "USD"),
    "order-42",
    "Order 42",
    new PaymentMethod(PaymentMethod.Type.PROVIDER_TOKEN, "pm_provider_token"),
    new IdempotencyKey("order-42-create"),
    Map.of()));
```

Use only provider-generated tokens or hosted checkout. PayBridge V0.1 does not accept raw card numbers, CVV/CVC, magnetic-stripe data, or equivalent cardholder authentication data.

## Hosted checkout flows

- **bKash:** create a payment, redirect the customer to `Payment.clientActionUrl`, call `gateway.confirm(reference)` after the provider callback, then query/reconcile as appropriate.
- **SSLCommerz:** create a hosted session, redirect to `clientActionUrl`, and query/validate server-side before fulfilment. A browser return URL is not payment confirmation.
- **PortPos:** create a hosted invoice, redirect to `clientActionUrl`, then retrieve the invoice server-side before fulfilment. See [PortPos details](docs/PORTPOS.md).

## Spring Boot status

The starter contains configuration classes for Stripe, bKash, and SSLCommerz, but it is **not yet auto-discovered by Spring Boot** because the auto-configuration metadata file and integration test are pending. For now, construct adapters and `PaymentGatewayRegistry` explicitly in application configuration. PortPos currently has no Spring property binding or auto-registration.

## Testing

Use `paybridge-testkit` for deterministic application tests:

```java
var gateway = FakePaymentGateway.builder()
    .createPaymentSucceeds()
    .build();
```

The testkit also simulates declined, timeout, provider-error, and invalid-request create-payment outcomes. Provider adapters have local WireMock tests; live credentials are never needed for normal CI.

## Security

- Do not hardcode provider credentials.
- Do not log secrets, access tokens, raw payment payloads, or sensitive customer data.
- Treat hosted return URLs and unverified webhook/IPN payloads as untrusted.
- No verified provider webhook processor is available yet.

Read the [security model](docs/SECURITY_MODEL.md) and [security policy](SECURITY.md) before integrating payments.

## Build

Use Java 21 and Maven:

```bash
mvn clean verify
```

## Roadmap and contribution

PayBridge is building an SDK foundation first; routing, failover, analytics, dashboard, hosted API, and multi-language SDKs are future work, not V0.1 features. See the [roadmap](docs/ROADMAP.md), [contribution guide](CONTRIBUTING.md), and [provider development guide](docs/PROVIDER_DEVELOPMENT.md).

Apache-2.0 is included without an ownership notice. Add the project owner/copyright notice before release.
