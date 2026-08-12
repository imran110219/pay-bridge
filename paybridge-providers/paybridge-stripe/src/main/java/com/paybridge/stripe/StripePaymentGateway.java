package com.paybridge.stripe;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paybridge.core.error.*;
import com.paybridge.core.model.*;
import com.paybridge.spi.*;
import java.math.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/** Stripe Payment Intents adapter. It sends only provider tokens, never raw cardholder data. */
public final class StripePaymentGateway implements PaymentGateway {
  private final StripeConfiguration config; private final HttpClient client; private final ObjectMapper json;
  public StripePaymentGateway(StripeConfiguration config){this(config,HttpClient.newHttpClient(),new ObjectMapper());}
  StripePaymentGateway(StripeConfiguration config,HttpClient client,ObjectMapper json){this.config=config;this.client=client;this.json=json;}
  public PaymentProvider provider(){return PaymentProvider.STRIPE;}
  public Set<GatewayCapability> capabilities(){return Set.of(GatewayCapability.CREATE_PAYMENT,GatewayCapability.GET_PAYMENT,GatewayCapability.REFUND,GatewayCapability.PARTIAL_REFUND,GatewayCapability.CANCEL,GatewayCapability.AUTHORIZE,GatewayCapability.CAPTURE,GatewayCapability.WEBHOOKS,GatewayCapability.TOKENIZATION);}
  public Payment createPayment(PaymentRequest request){ Map<String,String> fields=new LinkedHashMap<>(); fields.put("amount",minor(request.amount()).toString()); fields.put("currency",request.amount().currency().getCurrencyCode().toLowerCase(Locale.ROOT)); fields.put("description",Optional.ofNullable(request.description()).orElse("")); fields.put("metadata[merchant_reference]",request.merchantReference()); if(request.paymentMethod()!=null&&request.paymentMethod().type()==PaymentMethod.Type.PROVIDER_TOKEN){fields.put("payment_method",request.paymentMethod().token());fields.put("confirm","true");} return payment(send("POST","/v1/payment_intents",fields,request.idempotencyKey().value())); }
  public Payment getPayment(PaymentReference reference){return payment(send("GET","/v1/payment_intents/"+encode(reference.value()),Map.of(),null));}
  public Payment cancel(PaymentReference reference){return payment(send("POST","/v1/payment_intents/"+encode(reference.value())+"/cancel",Map.of(),null));}
  public Payment capture(PaymentReference reference){return payment(send("POST","/v1/payment_intents/"+encode(reference.value())+"/capture",Map.of(),null));}
  public Refund refund(RefundRequest request){Map<String,String> f=Map.of("payment_intent",request.paymentReference().value(),"amount",minor(request.amount()).toString()); JsonNode n=send("POST","/v1/refunds",f,request.idempotencyKey().value()); return new Refund(n.path("id").asText(),request.paymentReference(),request.amount(),"succeeded".equals(n.path("status").asText())?Refund.Status.SUCCEEDED:Refund.Status.PENDING);}
  private JsonNode send(String method,String path,Map<String,String> fields,String key){try{HttpRequest.Builder b=HttpRequest.newBuilder(config.baseUri().resolve(path)).header("Authorization","Bearer "+config.secretKey()).header("Content-Type","application/x-www-form-urlencoded"); if(key!=null)b.header("Idempotency-Key",key); if("GET".equals(method)) b.GET(); else b.POST(HttpRequest.BodyPublishers.ofString(form(fields))); HttpResponse<String> r=client.send(b.build(),HttpResponse.BodyHandlers.ofString()); JsonNode body=json.readTree(r.body()); if(r.statusCode()>=400) throw mapped(r.statusCode(),body); return body;}catch(PaymentException e){throw e;}catch(java.net.http.HttpTimeoutException e){throw new PaymentException(PaymentErrorCode.PROVIDER_TIMEOUT,provider(),null,true,"Stripe request timed out");}catch(Exception e){throw new PaymentException(PaymentErrorCode.NETWORK_ERROR,provider(),null,true,"Stripe request failed");}}
  private Payment payment(JsonNode n){String status=n.path("status").asText(); PaymentStatus s=switch(status){case "succeeded"->PaymentStatus.CAPTURED;case "requires_action"->PaymentStatus.REQUIRES_ACTION;case "requires_capture"->PaymentStatus.AUTHORIZED;case "canceled"->PaymentStatus.CANCELLED;case "processing"->PaymentStatus.PENDING;case "requires_payment_method"->PaymentStatus.CREATED;default->PaymentStatus.PENDING;}; Money amount=new Money(BigDecimal.valueOf(n.path("amount").asLong()).movePointLeft(n.path("currency").asText("USD").equalsIgnoreCase("JPY")?0:2),Currency.getInstance(n.path("currency").asText("USD").toUpperCase(Locale.ROOT))); return new Payment(new PaymentReference(provider(),n.path("id").asText()),s,amount,null,Instant.now(),Map.of("stripe_status",status));}
  private PaymentException mapped(int status,JsonNode n){String code=n.path("error").path("code").asText(null); PaymentErrorCode c=status==401?PaymentErrorCode.AUTHENTICATION_FAILED:status==404?PaymentErrorCode.PAYMENT_NOT_FOUND:status==429?PaymentErrorCode.RATE_LIMITED:status>=500?PaymentErrorCode.PROVIDER_UNAVAILABLE:PaymentErrorCode.INVALID_REQUEST; return new PaymentException(c,provider(),code,status==429||status>=500,"Stripe returned HTTP "+status);}
  private static BigInteger minor(Money money){return money.amount().movePointRight(Math.max(0,money.currency().getDefaultFractionDigits())).toBigIntegerExact();} private static String form(Map<String,String> f){return f.entrySet().stream().map(e->encode(e.getKey())+"="+encode(e.getValue())).collect(java.util.stream.Collectors.joining("&"));} private static String encode(String v){return URLEncoder.encode(v,StandardCharsets.UTF_8);}
}
