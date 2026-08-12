package com.paybridge.testkit;
import static org.assertj.core.api.Assertions.*;
import com.paybridge.core.model.*;
import org.junit.jupiter.api.Test;
class FakePaymentGatewayTest { @Test void produces_deterministic_payment(){var gateway=FakePaymentGateway.builder().createPaymentSucceeds().build(); var payment=gateway.createPayment(new PaymentRequest(Money.of("10.00","USD"),"order-1",null,null,new IdempotencyKey("order-1"),null)); assertThat(payment.reference().value()).isEqualTo("fake_1"); assertThat(gateway.getPayment(payment.reference()).status()).isEqualTo(PaymentStatus.CAPTURED);} @Test void simulates_decline(){var gateway=FakePaymentGateway.builder().scenario(FakePaymentGateway.Scenario.DECLINED).build(); var p=gateway.createPayment(new PaymentRequest(Money.of("10","USD"),"x",null,null,new IdempotencyKey("x"),null)); assertThat(p.status()).isEqualTo(PaymentStatus.FAILED);} }
