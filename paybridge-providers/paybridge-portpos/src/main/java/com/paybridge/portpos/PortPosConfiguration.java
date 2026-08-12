package com.paybridge.portpos;
import java.net.URI;import java.util.Objects;
/** PortWallet is now PortPos; credentials must be generated in the PortPos merchant/sandbox panel. */
public record PortPosConfiguration(String appKey,String secretKey,URI baseUri,URI redirectUri,URI ipnUri){public PortPosConfiguration{if(appKey==null||appKey.isBlank()||secretKey==null||secretKey.isBlank())throw new IllegalArgumentException("PortPos credentials are required");Objects.requireNonNull(baseUri);Objects.requireNonNull(redirectUri);}@Override public String toString(){return "PortPosConfiguration[appKey=REDACTED, secretKey=REDACTED, baseUri="+baseUri+", redirectUri="+redirectUri+", ipnUri="+ipnUri+"]";}}
