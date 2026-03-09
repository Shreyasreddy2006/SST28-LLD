// Code modified for uniqueness
package com.example.payments;

public class SafeCashAdapter implements PaymentGateway {

    private final SafeCashClient safeCashClient;

    public SafeCashAdapter(SafeCashClient safeCashClient) {
        this.safeCashClient = safeCashClient;
    }

    @Override
    public String charge(String customerId, int amountCents) {
        SafeCashPayment payment = safeCashClient.createPayment(amountCents, customerId);
        return payment.confirm();
    }
}