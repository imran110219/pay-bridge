package com.paybridge.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/** Monetary value normalized to the default fraction digits of its ISO-4217 currency. */
public record Money(BigDecimal amount, Currency currency) {
  public Money { Objects.requireNonNull(amount, "amount"); Objects.requireNonNull(currency, "currency"); if (amount.signum() < 0) throw new IllegalArgumentException("amount must not be negative"); amount = amount.setScale(Math.max(0, currency.getDefaultFractionDigits()), RoundingMode.HALF_UP); }
  public static Money of(String amount, String currency) { return new Money(new BigDecimal(amount), Currency.getInstance(currency)); }
}
