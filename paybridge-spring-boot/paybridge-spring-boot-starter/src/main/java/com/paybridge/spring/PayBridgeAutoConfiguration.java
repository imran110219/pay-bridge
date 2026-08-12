package com.paybridge.spring;
import com.paybridge.spi.*;
import com.paybridge.stripe.*;
import java.util.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
@AutoConfiguration @EnableConfigurationProperties(PayBridgeProperties.class) public class PayBridgeAutoConfiguration { @Bean @ConditionalOnMissingBean PaymentGatewayRegistry paymentGatewayRegistry(PayBridgeProperties p){List<PaymentGateway> gateways=new ArrayList<>(); if(p.getStripe().isEnabled()) gateways.add(new StripePaymentGateway(new StripeConfiguration(p.getStripe().getSecretKey(),null))); return new PaymentGatewayRegistry(gateways);} }
