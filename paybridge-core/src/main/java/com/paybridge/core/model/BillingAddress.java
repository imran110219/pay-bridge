package com.paybridge.core.model;
/** Postal address used only for providers that require billing/customer data. */
public record BillingAddress(String line1, String city, String state, String postalCode, String country) { public BillingAddress { if(line1==null||city==null||state==null||postalCode==null||country==null) throw new IllegalArgumentException("billing address fields are required"); } public BillingAddress(String line1,String city,String postalCode,String country){this(line1,city,city,postalCode,country);} }
