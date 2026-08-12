package com.paybridge.core.model;
import java.time.Instant;
import java.util.Map;
public record Payment(PaymentReference reference, PaymentStatus status, Money amount, String clientActionUrl, Instant createdAt, Map<String, String> providerMetadata) { public Payment { providerMetadata = providerMetadata == null ? Map.of() : Map.copyOf(providerMetadata); } }
