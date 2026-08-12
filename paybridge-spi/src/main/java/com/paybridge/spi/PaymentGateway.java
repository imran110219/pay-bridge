package com.paybridge.spi;
import com.paybridge.core.model.*;
import java.util.Set;
/** Provider-neutral port. Operations absent from capabilities must fail with UNSUPPORTED_OPERATION. */
public interface PaymentGateway { PaymentProvider provider(); Set<GatewayCapability> capabilities(); Payment createPayment(PaymentRequest request); Payment getPayment(PaymentReference reference); /** Completes a provider-hosted approval flow where the provider requires an explicit execution call. */ default Payment confirm(PaymentReference reference) { throw new UnsupportedOperationException("confirmation unsupported"); } default Refund refund(RefundRequest request) { throw new UnsupportedOperationException("refund unsupported"); } default Payment cancel(PaymentReference reference) { throw new UnsupportedOperationException("cancel unsupported"); } default Payment capture(PaymentReference reference) { throw new UnsupportedOperationException("capture unsupported"); } }
