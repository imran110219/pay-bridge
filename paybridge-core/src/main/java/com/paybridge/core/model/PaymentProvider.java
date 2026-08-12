package com.paybridge.core.model;
import java.util.Locale;
public record PaymentProvider(String value) { public PaymentProvider { if (value == null || !value.matches("[a-z0-9-]{1,64}")) throw new IllegalArgumentException("provider must be lowercase kebab-case"); } public static PaymentProvider of(String value) { return new PaymentProvider(value.toLowerCase(Locale.ROOT)); } public static final PaymentProvider STRIPE = of("stripe"); public static final PaymentProvider BKASH = of("bkash"); public static final PaymentProvider SSLCOMMERZ = of("sslcommerz"); }
