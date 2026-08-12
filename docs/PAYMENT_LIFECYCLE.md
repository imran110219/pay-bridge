# Payment lifecycle

`CREATED → PENDING/REQUIRES_ACTION → AUTHORIZED → CAPTURED` is a conceptual path, not a universal state machine. Failure/cancellation/expiry are terminal payment outcomes. Refund outcomes are represented by `PARTIALLY_REFUNDED` and `REFUNDED` after reconciliation. Providers with finer states preserve a safe raw state key in metadata; applications must not infer unsupported semantics from normalization alone.
