package com.example.FQW.Yookassa;

import com.example.FQW.ex.RequestException;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Configuration
public class YookassaClient {

    private final static String APIEndpoint = "https://api.yookassa.ru/v3/";
    private final static String authorization = "Basic " + Base64.getEncoder()
            .encodeToString((AuthDate.shopId + ":" + AuthDate.secretKey)
                    .getBytes(StandardCharsets.UTF_8));

    public YookassaModel create(YookassaModel yookassaModel) {
        try {
            HttpResponse<String> response = Unirest
                    .post(APIEndpoint + "/payments")
                    .header("Authorization", YookassaClient.authorization)
                    .header("Idempotence-Key", UUID.randomUUID().toString())
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(yookassaModel, YookassaModel.class))
                    .asString();
            if (response.getStatus() == 200) {
                return new Gson().fromJson(response.getBody(), YookassaModel.class);
            } else {
                throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Возникла проблема при выполнении запроса на создание платежа");
            }
        } catch (UnirestException e) {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, e.getMessage());
        }
    }

    public YookassaModel requestOrder(String id) {
        try {
            HttpResponse<YookassaModel> response = Unirest
                    .get(APIEndpoint + "/payments" + id)
                    .header("Content-Type", "application/json")
                    .asObject(YookassaModel.class);

            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Возникла проблема при выполнении запроса по получению информации о платеже");
            }
        } catch (UnirestException e) {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, e.getMessage());
        }
    }
}
