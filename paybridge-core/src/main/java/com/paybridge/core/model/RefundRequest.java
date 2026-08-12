package com.paybridge.core.model;
import java.util.Objects;
public record RefundRequest(PaymentReference paymentReference, Money amount, IdempotencyKey idempotencyKey) { public RefundRequest { Objects.requireNonNull(paymentReference); Objects.requireNonNull(amount); Objects.requireNonNull(idempotencyKey); } }
