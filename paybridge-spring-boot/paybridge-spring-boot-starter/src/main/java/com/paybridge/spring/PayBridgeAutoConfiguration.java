package com.paybridge.spring;
import com.paybridge.spi.*;
import com.paybridge.stripe.*; import com.paybridge.bkash.*; import com.paybridge.sslcommerz.*; import java.net.URI;
import java.util.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
@AutoConfiguration @EnableConfigurationProperties(PayBridgeProperties.class) public class PayBridgeAutoConfiguration { @Bean @ConditionalOnMissingBean PaymentGatewayRegistry paymentGatewayRegistry(PayBridgeProperties p){List<PaymentGateway> gateways=new ArrayList<>(); if(p.getStripe().isEnabled()) gateways.add(new StripePaymentGateway(new StripeConfiguration(p.getStripe().getSecretKey(),null))); if(p.getBkash().isEnabled()){var b=p.getBkash();gateways.add(new BkashPaymentGateway(new BkashConfiguration(b.getAppKey(),b.getAppSecret(),b.getUsername(),b.getPassword(),URI.create(b.getBaseUri()),URI.create(b.getCallbackUri()))));}if(p.getSslcommerz().isEnabled()){var s=p.getSslcommerz();gateways.add(new SslCommerzPaymentGateway(new SslCommerzConfiguration(s.getStoreId(),s.getStorePassword(),URI.create(s.getBaseUri()),URI.create(s.getSuccessUri()),URI.create(s.getFailureUri()),URI.create(s.getCancelUri()),URI.create(s.getIpnUri()))));} return new PaymentGatewayRegistry(gateways);} }
