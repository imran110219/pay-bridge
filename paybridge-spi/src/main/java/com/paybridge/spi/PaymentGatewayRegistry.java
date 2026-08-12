package com.paybridge.spi;
import com.paybridge.core.model.PaymentProvider;
import java.util.*;
/** Instance-scoped registry; applications may register third-party adapters. */
public final class PaymentGatewayRegistry { private final Map<PaymentProvider, PaymentGateway> gateways; public PaymentGatewayRegistry(Collection<? extends PaymentGateway> gateways) { Map<PaymentProvider, PaymentGateway> result = new HashMap<>(); for (PaymentGateway g: gateways) if (result.put(g.provider(), g) != null) throw new IllegalArgumentException("duplicate provider: " + g.provider()); this.gateways=Map.copyOf(result); } public PaymentGateway get(PaymentProvider provider) { PaymentGateway gateway=gateways.get(provider); if(gateway==null) throw new NoSuchElementException("provider not registered: "+provider.value()); return gateway; } public Set<PaymentProvider> providers(){return gateways.keySet();} }
