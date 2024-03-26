package com.acme.sales.domain.model.aggregates;

import com.acme.shared.domain.model.valueobjects.Address;

import java.util.List;
import java.util.UUID;

public class SalesOrder {
    private final UUID internalId;

    private Address shippingAddress;

    private SalesOrderStatus status;

    private final List<SalesOrderItem> items;

    private double paymentAmount;

    public SalesOrder() {
        this.internalId = UUID.randomUUID();
        this.status = SalesOrderStatus.CREATED;
        this.items = List.of();
        this.paymentAmount = 0;
    }

    public void cancel() {
        if (status == SalesOrderStatus.CREATED) {
            status = SalesOrderStatus.CANCELLED;
            clearItems();
        } else {
            throw new IllegalStateException("Only orders in CREATED status can be cancelled");
        }
    }

    public void dispatch(String street, String city, String state, String zipCode, String country) {
        verifyIfReadyForDispatch();
        this.shippingAddress = new Address(street, city, state, zipCode, country);
        status = SalesOrderStatus.IN_PROGRESS;
    }

    public boolean isInProgress() {
        return status == SalesOrderStatus.IN_PROGRESS;
    }

    public UUID getInternalId() {
        return internalId;
    }


    public void addItem(int quantity, Long productId, double unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (unitPrice <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than 0");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must be provided");
        }
        if (this.status == SalesOrderStatus.APPROVED) {
            throw new IllegalStateException("Items cannot be added to an order in APPROVED status");
        }
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

    public String getShippingAddress() {
        return shippingAddress.getAddressAsString();
    }

    public void addPayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        if (amount > this.calculateTotalPrice() - this.paymentAmount) {
            throw new IllegalArgumentException("Payment amount exceeds the total price of the order");
        }
        this.paymentAmount += amount;
    }

    private void verifyIfReadyForDispatch() {
        if (status == SalesOrderStatus.APPROVED) return;
        if (paymentAmount == calculateTotalPrice()) {
            status = SalesOrderStatus.APPROVED;
        } else {
            throw new IllegalStateException("Order is not ready for dispatch");
        }
    }
}
