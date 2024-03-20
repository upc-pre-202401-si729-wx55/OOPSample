package com.acme.sales.domain.model.aggregates;

import com.acme.shared.domain.model.valueobjects.Address;

import java.util.UUID;

public class SalesOrder {
    private final UUID internalId;

    private final Address shippingAddress;

    private SalesOrderStatus status;

    public SalesOrder(Address shippingAddress) {
        this.internalId = UUID.randomUUID();
        this.shippingAddress = shippingAddress;
        this.status = SalesOrderStatus.CREATED;
    }

    public void cancel() {
        if (status == SalesOrderStatus.CREATED) {
            status = SalesOrderStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Only orders in CREATED status can be cancelled");
        }
    }

    public void dispatch() {
        if (status == SalesOrderStatus.APPROVED) {
            status = SalesOrderStatus.IN_PROGRESS;
        } else {
            throw new IllegalStateException("Only orders in APPROVED status can be dispatched");
        }
    }

    public boolean isInProgress() {
        return status == SalesOrderStatus.IN_PROGRESS;
    }

    public UUID getInternalId() {
        return internalId;
    }

}
