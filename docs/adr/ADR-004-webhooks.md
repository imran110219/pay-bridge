# ADR-004: Verified webhooks

Providers own parsing and signature verification behind `WebhookProcessor`. Applications own delivery deduplication/persistence. Accepting unverified payloads was rejected.
