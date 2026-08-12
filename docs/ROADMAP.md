# Roadmap

Completed foundation: Stripe Payment Intents; bKash URL Checkout create/confirm/query; SSLCommerz hosted-session create/query; PortPos (formerly PortWallet) hosted invoice create/query/refund; Spring provider dependencies; and WireMock adapter tests.

Next V0.1 milestone: add a safe provider-transaction reference to the refund contract, then implement and sandbox-validate bKash/SSLCommerz refunds; add verified provider webhook/IPN processors, including SSLCommerz and PortPos server-side validation; register PortPos through Spring Boot configuration; add Spring MVC demonstration endpoints; and add provider contract fixtures and vendor-neutral observability hooks. Later candidates: more SDKs, hosted API, routing/failover, analytics, and dashboard.
