# Payment lifecycle

`CREATED → PENDING/REQUIRES_ACTION → AUTHORIZED → CAPTURED` is a conceptual path, not a universal state machine. Failure/cancellation/expiry are terminal payment outcomes. Refund outcomes are represented by `PARTIALLY_REFUNDED` and `REFUNDED` after reconciliation. Providers with finer states preserve a safe raw state key in metadata; applications must not infer unsupported semantics from normalization alone.

| Adapter | Implemented flow | Normalized mapping |
|---|---|---|
| Stripe | Payment Intent lifecycle | `succeeded→CAPTURED`, `requires_action→REQUIRES_ACTION`, `requires_capture→AUTHORIZED`, `processing→PENDING`, `canceled→CANCELLED` |
| bKash URL Checkout | Create → hosted customer approval → `confirm`/execute → query | Created checkout is `REQUIRES_ACTION`; `Completed→CAPTURED`; `Initiated→REQUIRES_ACTION`; `Cancelled→CANCELLED`; `Failed→FAILED` |
| SSLCommerz Hosted Checkout | Create session → hosted customer payment → server-side query/IPN validation | Session creation is `PENDING`; `VALID`/`VALIDATED→CAPTURED`; `FAILED→FAILED`; `CANCELLED→CANCELLED`; `EXPIRED→EXPIRED`; `UNATTEMPTED→CREATED` |
| PortPos Hosted Invoice | Create invoice → hosted customer payment → retrieve/IPN validation → optional refund | `PENDING→PENDING`; `ACCEPTED→CAPTURED`; `REJECTED→FAILED`; `CANCELLED→CANCELLED`; `EXPIRED→EXPIRED`; `REFUNDED→REFUNDED`; `PARTIALLY_REFUNDED→PARTIALLY_REFUNDED` |

For SSLCommerz, never use a browser return URL alone to mark a payment captured. Validate the IPN/server-side transaction before fulfilling an order.
