package com.fleetmanagement.platform.events;

public class PaymentProcessFailedEvent  {

    public final String paymentId;

    public final String orderId;

    public PaymentProcessFailedEvent(String paymentId, String orderId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
    }

	@Override
	public String toString() {
		return "InvoiceCreatedEvent [paymentId=" + paymentId + ", orderId=" + orderId + "]";
	}
    
    
}
