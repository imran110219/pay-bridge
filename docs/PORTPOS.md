# PortPos (formerly PortWallet)

PortWallet has rebranded to PortPos. PayBridge implements the current PortPos v2 hosted-invoice API, not PortWallet's deprecated v1 API.

Implemented operations are invoice creation, retrieval by invoice ID, and full/partial refund submission. A created `Payment` reference is the PortPos invoice ID and its `clientActionUrl` is the hosted payment URL. Query that invoice from the server before fulfilling an order.

PortPos requires an app key, secret key, API base URI, redirect URI, and optionally an IPN URI. Supply credentials through environment/configuration; never expose them to a browser. Creating an invoice requires customer name, email, phone, street address, city, state, postal code, and country.

The adapter uses PortPos's documented `Bearer base64(appKey:md5(secretKey + Unix timestamp))` authentication for each request. PortPos IPN verification is not implemented yet, so the provider's webhook capability is deliberately not advertised. The next implementation must call the documented authenticated IPN-validation endpoint, reject failures, and deduplicate delivery IDs before exposing an event.
