package com.acme.sales.domain.model.aggregates;

import java.util.UUID;

public class SalesOrderItem {
    private final int quantity;
    private final Long productId;

    private final double unitPrice;

    private final UUID itemId;

    private boolean dispatched;

    public SalesOrderItem(int quantity, Long productId, double unitPrice) {
        this.quantity = quantity;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.itemId = UUID.randomUUID();
        this.dispatched = false;
    }

    public void dispatch() {
        this.dispatched = true;
    }

    public boolean isDispatched() {
        return dispatched;
    }

    public UUID getItemId() {
        return itemId;
    }

    public double calculateItemPrice() {
        return quantity * unitPrice;
    }


}
