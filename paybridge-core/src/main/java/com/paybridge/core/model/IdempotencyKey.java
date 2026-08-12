package com.paybridge.core.model;
public record IdempotencyKey(String value) { public IdempotencyKey { if (value == null || !value.matches("[A-Za-z0-9_-]{1,255}")) throw new IllegalArgumentException("idempotency key must be 1-255 URL-safe characters"); } }
