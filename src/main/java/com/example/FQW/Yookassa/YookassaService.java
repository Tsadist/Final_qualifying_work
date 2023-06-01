package com.example.FQW.Yookassa;

public class YookassaService {

    private final static String sum = "%s .00";
    private final static String currency = "RUB";
    private final static String description = "Оплата заказа № %d для %s";

    public YookassaModel createPayment(Integer value, Long orderId, String emailCustomer) {
        YookassaModel.Amount amount = YookassaModel
                .Amount
                .builder()
                .value(String.format(sum, value))
                .currency(currency)
                .build();

        return YookassaModel
                .builder()
                .amount(amount)
                .description(String.format(description, orderId, emailCustomer))
                .capture(true)
                .merchant_customer_id(emailCustomer)
                .build();
    }
}
