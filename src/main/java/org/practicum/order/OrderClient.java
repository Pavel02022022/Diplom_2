package org.practicum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step("Create order with auth")
    public ValidatableResponse create(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post("/api/orders")
                .then();
    }
    @Step("Create order without auth")
    public ValidatableResponse create(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/orders")
                .then();
    }

    @Step("Get orders without auth")
    public ValidatableResponse getUsersOrders() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/orders")
                .then();
    }
    @Step("Get orders with auth")
    public ValidatableResponse getUsersOrders(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .get("/api/orders")
                .then();
    }

}
