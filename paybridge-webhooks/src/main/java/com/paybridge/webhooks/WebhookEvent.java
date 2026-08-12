package com.paybridge.webhooks;
import com.paybridge.core.model.*;
import java.time.Instant;
import java.util.Map;
public record WebhookEvent(String id, PaymentProvider provider, WebhookEventType type, PaymentReference paymentReference, Instant occurredAt, Map<String,String> metadata) { public WebhookEvent { metadata=metadata==null?Map.of():Map.copyOf(metadata); } }
