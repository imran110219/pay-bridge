package com.paybridge.spi;
import com.paybridge.core.model.*;
import java.util.Set;
/** Provider-neutral port. Operations absent from capabilities must fail with UNSUPPORTED_OPERATION. */
public interface PaymentGateway { PaymentProvider provider(); Set<GatewayCapability> capabilities(); Payment createPayment(PaymentRequest request); Payment getPayment(PaymentReference reference); default Refund refund(RefundRequest request) { throw new UnsupportedOperationException("refund unsupported"); } default Payment cancel(PaymentReference reference) { throw new UnsupportedOperationException("cancel unsupported"); } default Payment capture(PaymentReference reference) { throw new UnsupportedOperationException("capture unsupported"); } }
