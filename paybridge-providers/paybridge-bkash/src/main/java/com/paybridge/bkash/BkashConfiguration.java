package com.paybridge.bkash;

import java.net.URI;
import java.util.Objects;

/** bKash Checkout credentials and merchant callback. Configure baseUri from current merchant onboarding material. */
public record BkashConfiguration(String appKey, String appSecret, String username, String password, URI baseUri, URI callbackUri) {
  public BkashConfiguration { for (String value : new String[]{appKey, appSecret, username, password}) if(value == null || value.isBlank()) throw new IllegalArgumentException("bKash credentials are required"); baseUri=Objects.requireNonNull(baseUri,"baseUri"); callbackUri=Objects.requireNonNull(callbackUri,"callbackUri"); }
  @Override public String toString(){return "BkashConfiguration[appKey=REDACTED, appSecret=REDACTED, username=REDACTED, password=REDACTED, baseUri="+baseUri+", callbackUri="+callbackUri+"]";}
}
