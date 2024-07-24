import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.practicum.order.OrderClient;
import org.practicum.user.UserClient;
import org.practicum.model.Order;
import org.practicum.model.User;

import java.util.Collections;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.practicum.order.OrderGenerator.randomOrder;
import static org.practicum.user.UserGenerator.randomUser;
import static org.practicum.utils.Utils.*;

public class GetOrdersTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static ValidatableResponse response;

    @Before
    public void setUP(){
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void tearDown() {
        deleteUserAfterTestExecution(response);
    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    @Description("Получение заказов пользователя с авторизацией, проверка ответа и его кода")
    public void getOrdersWithAuthExpects200()  {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        OrderClient orderClient = new OrderClient();
        response = userClientClient.create(user);

        Order order = randomOrder();
        orderClient.create(order, getTokenAtUserCreation(response));

        ValidatableResponse response2 = orderClient.getUsersOrders(getTokenAtUserCreation(response));

        response2.assertThat().statusCode(200)
                .assertThat().body("success", equalTo(true))
                .assertThat().body(matchesJsonSchemaInClasspath("getOrdersResponseSchema.json"));
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    @Description("Получение заказов пользователя без авторизации, проверка ответа и его кода")
    public void getOrdersWithoutAuthExpects401() {
        OrderClient orderClient = new OrderClient();
        Order order = randomOrder();
        orderClient.create(order);

        ValidatableResponse response2 = orderClient.getUsersOrders();

        response2.assertThat().statusCode(401)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Проверка, что в ответе вернётся максимум 50 последних заказов пользователя")
    @Description("Проверка, что в ответе GET /api/orders вернётся максимум 50 последних заказов пользователя")
    public void getFirst50OrdersExpects50()  {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        OrderClient orderClient = new OrderClient();
        response = userClientClient.create(user);

        createSomeOrders(response,50);

        ValidatableResponse response2 = orderClient.getUsersOrders(getTokenAtUserCreation(response));

        response2
                .assertThat().body("orders", hasSize(equalTo(50)));
    }

    @Test
    @DisplayName("Проверка, что в ответе заказы сортируются по времени обновления")
    @Description("Проверка, что в ответе GET /api/orders заказы сортируются по времени обновления")
    public void ordersSortedByUpdatedAtExpectsTrue()  {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        OrderClient orderClient = new OrderClient();
        response = userClientClient.create(user);

        createSomeOrders(response,5);

        ValidatableResponse response2 = orderClient.getUsersOrders(getTokenAtUserCreation(response));

        List<String> ordersUpdatedAt = response2.extract().path("orders.updatedAt");
        List<String> sortedOrdersUpdatedAt = ordersUpdatedAt;
        Collections.sort(sortedOrdersUpdatedAt);

        Assert.assertEquals("Не сооответствует сортировка по времени обновления", sortedOrdersUpdatedAt, ordersUpdatedAt);
    }

}
