package com.example.booking.service;

public class PaymentService {

    public String processPayment(int amountInRupees) {
        String txnId = "TXN-" + System.nanoTime();
        System.out.println("[Payment] Charged Rs. " + amountInRupees + " | txn: " + txnId);
        return txnId;
    }

    public String processRefund(String originalTxnId, int amountInRupees) {
        String refundId = "REFUND-" + System.nanoTime();
        System.out.println("[Payment] Refunded Rs. " + amountInRupees
                + " against " + originalTxnId + " | refund: " + refundId);
        return refundId;
    }
}
