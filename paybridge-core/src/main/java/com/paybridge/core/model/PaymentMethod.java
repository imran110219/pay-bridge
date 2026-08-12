package com.paybridge.core.model;
/** A provider-generated token or hosted checkout request; never raw card data. */
public record PaymentMethod(Type type, String token) { public enum Type { PROVIDER_TOKEN, HOSTED_CHECKOUT } public PaymentMethod { if (type == null || token == null || token.isBlank()) throw new IllegalArgumentException("type and token are required"); } }
