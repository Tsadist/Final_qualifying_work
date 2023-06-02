package com.example.FQW.Yookassa;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class YookassaModel {

    public enum Status{
        pending, waiting_for_capture, succeeded, canceled
    }

    public enum StatusReceiptRegistration{
        pending, succeeded, canceled
    }

    private String id;
    private Status status;
    private Boolean paid;
    private Amount amount;
    private String created_at;
    private String expires_at;
    private Boolean capture;
    private String captured_at;
    private String description;
    private String merchant_customer_id;
    private PaymentMethod payment_method_data;
    private Recipient recipient;
    private StatusReceiptRegistration receipt_registration;
    private Boolean refundable;
    private Boolean test;
    private IncomeAmount income_amount;
    private Confirmation confirmation;
    private Receipt receipt;

    @Setter
    @Getter
    public static class Receipt{
        private Customer customer;
        private Item[] items;
    }

    @Setter
    @Getter
    public static class Item{
        private String description;
        private Amount amount;
        private Integer vat_code = 1;
        private Integer quantity = 1;
    }

    @Setter
    @Getter
    public static class Customer{
        private String full_name;
        private String inn;
        private String email;
        private String phone;
    }

    @Setter
    @Getter
    public static class Confirmation{
        private String type = "redirect";
        private String confirmation_token;
        private String return_url;
        private String confirmation_url;
    }

    @Setter
    @Getter
    public static class IncomeAmount {
        private String value;
        private String currency;
    }

    @Setter
    @Getter
    public static class Recipient {
        private String account_id;
        private String gateway_id;
    }

    @Setter
    @Getter
    public static class PaymentMethod {
        private String type;
        private String id;
        private Boolean saved;
        private String title;
    }

    @Setter
    @Getter
    public static class Card {
        private String first6;
        private String last4;
        private String expiry_month;
        private String expiry_year;
        private String card_type;
        private String issuer_country;
        private String issuer_name;
    }

    @Setter
    @Getter
    public static class Amount {
        private String value;
        private String currency;
    }
}

