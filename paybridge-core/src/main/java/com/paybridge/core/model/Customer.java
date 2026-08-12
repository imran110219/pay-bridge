package com.paybridge.core.model;
public record Customer(String name, String email, BillingAddress billingAddress) { public Customer { if(name==null||name.isBlank()||email==null||email.isBlank()) throw new IllegalArgumentException("customer name and email are required"); } }
