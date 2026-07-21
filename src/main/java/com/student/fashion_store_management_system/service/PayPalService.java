package com.student.fashion_store_management_system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayPalService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secret;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String createOrder(Order order, Payment payment) {
        try {
            String accessToken = getAccessToken();

            Map<String, Object> body = Map.of(
                    "intent", "CAPTURE",
                    "purchase_units", List.of(
                            Map.of(
                                    "reference_id", String.valueOf(order.getOrderId()),
                                    "description", "Fashion Store Order #" + order.getOrderId(),
                                    "amount", Map.of(
                                            "currency_code", "USD",
                                            "value", formatMoney(payment.getTotalAmount())
                                    )
                            )
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v2/checkout/orders"))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("PayPal create order failed: " + response.body());
            }

            JsonNode json = objectMapper.readTree(response.body());
            return json.get("id").asText();

        } catch (Exception e) {
            throw new RuntimeException("Error creating PayPal order: " + e.getMessage(), e);
        }
    }

    public JsonNode captureOrder(String paypalOrderId) {
        try {
            String accessToken = getAccessToken();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v2/checkout/orders/" + paypalOrderId + "/capture"))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("PayPal capture order failed: " + response.body());
            }

            return objectMapper.readTree(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Error capturing PayPal order: " + e.getMessage(), e);
        }
    }

    private String getAccessToken() {
        try {
            String auth = clientId + ":" + secret;
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/oauth2/token"))
                    .timeout(Duration.ofSeconds(20))
                    .header("Authorization", "Basic " + encodedAuth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("PayPal token failed: " + response.body());
            }

            JsonNode json = objectMapper.readTree(response.body());
            return json.get("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Error getting PayPal access token: " + e.getMessage(), e);
        }
    }

    private String formatMoney(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
