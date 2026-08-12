package com.paybridge.sslcommerz;
/** Boundary only; live API/IPN implementation awaits merchant sandbox validation. */
public record SslCommerzConfiguration(String storeId, String storePassword, boolean sandbox) { @Override public String toString(){return "SslCommerzConfiguration[storeId="+storeId+", storePassword=REDACTED, sandbox="+sandbox+"]";} }
