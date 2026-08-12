package com.paybridge.stripe;
import java.net.URI;
import java.util.Objects;
/** Credentials are supplied by the application (usually environment-backed) and never logged. */
public record StripeConfiguration(String secretKey, URI baseUri) { public StripeConfiguration { if(secretKey==null||secretKey.isBlank()) throw new IllegalArgumentException("secretKey is required"); baseUri=Objects.requireNonNullElse(baseUri,URI.create("https://api.stripe.com")); } @Override public String toString(){return "StripeConfiguration[secretKey=REDACTED, baseUri="+baseUri+"]";} }
