package com.paybridge.core.model;
import java.util.Objects;
public record PaymentReference(PaymentProvider provider, String value) { public PaymentReference { Objects.requireNonNull(provider); if (value == null || value.isBlank()) throw new IllegalArgumentException("reference is required"); } }
