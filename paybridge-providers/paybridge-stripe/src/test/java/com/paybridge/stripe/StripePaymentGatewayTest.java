package com.paybridge.stripe;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.paybridge.core.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import java.net.URI;
class StripePaymentGatewayTest { @RegisterExtension static WireMockExtension wireMock=WireMockExtension.newInstance().options(com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig().dynamicPort()).build(); @Test void creates_and_forwards_idempotency_key(){wireMock.stubFor(post("/v1/payment_intents").withHeader("Idempotency-Key",equalTo("order-1")).willReturn(okJson("{\"id\":\"pi_1\",\"status\":\"succeeded\",\"amount\":1250,\"currency\":\"usd\"}"))); var gateway=new StripePaymentGateway(new StripeConfiguration("sk_test_not_real",URI.create(wireMock.baseUrl()))); var result=gateway.createPayment(new PaymentRequest(Money.of("12.50","USD"),"merchant-1",null,null,new IdempotencyKey("order-1"),null)); assertThat(result.reference().value()).isEqualTo("pi_1"); assertThat(result.status()).isEqualTo(PaymentStatus.CAPTURED); } }
