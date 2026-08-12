package com.paybridge.core.model;
public record Refund(String reference, PaymentReference paymentReference, Money amount, Status status) { public enum Status { PENDING, SUCCEEDED, FAILED } }
