package com.paybridge.webhooks;
import com.paybridge.core.model.PaymentProvider;
/** Adapters must verify authenticity before mapping or handing an event to applications. */
public interface WebhookProcessor { PaymentProvider provider(); WebhookEvent verifyAndParse(byte[] rawPayload, java.util.Map<String,String> headers); }
