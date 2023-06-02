package com.example.FQW.service;

import com.example.FQW.Yookassa.YookassaClient;
import com.example.FQW.Yookassa.YookassaModel;
import com.example.FQW.models.DB.Order;
import com.example.FQW.models.DB.Payment;
import com.example.FQW.repository.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final static String sum = "%s .00";
    private final static String currency = "RUB";
    private final static String description = "Оплата заказа № %d для %s";
    private final static String returnURL = "http://locakalhost:8020/order";

    private final YookassaClient yookassaClient;
    private final PaymentRepo paymentRepo;

    public Payment createPayment(Integer value, Order order, String emailCustomer) {

        YookassaModel requestModel = getYookassaModel(value, order.getId(), emailCustomer);
        YookassaModel responseModel = yookassaClient.create(requestModel);

        Payment payment = new Payment();
        payment.setIdPayment(responseModel.getId());
        payment.setStatusPayment(responseModel.getStatus().toString());
        payment.setLinkForPayment(responseModel.getConfirmation().getConfirmation_url());
        payment.setOrder(order);
        payment.setSum(responseModel.getAmount().getValue());
        payment.setTime(responseModel.getCreated_at());

        return paymentRepo.save(payment);
    }

    public Payment getPaymentForOrderId (Order order) {
        return paymentRepo.findByOrderId(order.getId());
    }

    private YookassaModel getYookassaModel (Integer value, Long orderId, String emailCustomer) {

        YookassaModel.Confirmation confirmation = new YookassaModel.Confirmation();
        confirmation.setReturn_url(returnURL);

        YookassaModel.Amount amount = new YookassaModel.Amount();
        amount.setCurrency(currency);
        amount.setValue(String.format(sum, value));

        return YookassaModel
                .builder()
                .amount(amount)
                .description(String.format(description, orderId, emailCustomer))
                .capture(true)
                .merchant_customer_id(emailCustomer)
                .confirmation(confirmation)
                .build();
    }
}
