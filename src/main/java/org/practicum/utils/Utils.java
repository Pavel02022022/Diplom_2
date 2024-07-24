package org.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.practicum.order.OrderClient;
import org.practicum.user.UserClient;
import org.practicum.model.Order;

import java.util.concurrent.ThreadLocalRandom;

import static org.practicum.order.OrderGenerator.randomOrder;

public class Utils {
    @Step("Get token at user registraton")
    public static String getTokenAtUserCreation(ValidatableResponse response){
        return response.extract().path("accessToken");
    }
    @Step("Delete user after test execution")
    public static void deleteUserAfterTestExecution(ValidatableResponse response){
        UserClient userClient = new UserClient();
        if (response!=null&&response.extract().statusCode()==200) {
            userClient.delete(getTokenAtUserCreation(response));
            System.out.println("Удаляем пользователя " +  response.extract().path("user.name") + " после выполнения теста.");
        }
    }

    // Создаем определенное количество заказов
    @Step("Create some orders")
    public static void createSomeOrders(ValidatableResponse response, int orders){
        OrderClient orderClient = new OrderClient();
        for (int i = 0; i<orders; i++){
            Order order = randomOrder();
            orderClient.create(order, getTokenAtUserCreation(response));
        }
    }
    @Step("Random bun")
    public static String randomBun() {
        String[] buns = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6c"};
        return buns[ThreadLocalRandom.current().nextInt(0, buns.length)];
    }
    @Step("Random main")
    public static String randomMain() {
        String[] mains = {"61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa6e",
                "61c0c5a71d1f82001bdaaa76", "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa78", "61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa7a"};
        return mains[ThreadLocalRandom.current().nextInt(0, mains.length)];
    }
    @Step("Random sauce")
    public static String randomSauce() {
        String[] sauces = {"61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa74", "61c0c5a71d1f82001bdaaa75"};
        return sauces[ThreadLocalRandom.current().nextInt(0, sauces.length)];
    }






}
