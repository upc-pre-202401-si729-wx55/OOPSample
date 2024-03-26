package com.acme.sales.domain.model.aggregates;

import com.acme.shared.domain.model.valueobjects.Address;

import java.util.List;
import java.util.UUID;

public class SalesOrder {
    private final UUID internalId;

    private final Address shippingAddress;

    private SalesOrderStatus status;

    private List<SalesOrderItem> items;

    public SalesOrder(Address shippingAddress) {
        this.internalId = UUID.randomUUID();
        this.shippingAddress = shippingAddress;
        this.status = SalesOrderStatus.CREATED;
        this.items = List.of();
    }

    public void cancel() {
        if (status == SalesOrderStatus.CREATED) {
            status = SalesOrderStatus.CANCELLED;
            clearItems();
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


    public void addItem(int quantity, Long productId, double unitPrice) {
        items.add(new SalesOrderItem(quantity, productId, unitPrice));
    }

    public void clearItems() {
        this.items.clear();
    }

    public double calculateTotalPrice() {
        return items.stream()
                .mapToDouble(SalesOrderItem::calculateItemPrice)
                .sum();
    }
}
