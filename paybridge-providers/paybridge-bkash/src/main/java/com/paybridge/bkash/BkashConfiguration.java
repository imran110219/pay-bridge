package com.paybridge.bkash;
/** Boundary only: populate from official merchant onboarding material, never source control. */
public record BkashConfiguration(String appKey, String appSecret, String username, String password) { @Override public String toString(){return "BkashConfiguration[REDACTED]";} }
