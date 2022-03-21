package com.fleetmanagement.platform.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class PaymentProcessFailedCommand{

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;

    public PaymentProcessFailedCommand(String paymentId, String orderId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
    }
}
