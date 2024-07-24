import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.practicum.order.OrderClient;
import org.practicum.user.UserClient;
import org.practicum.model.Order;
import org.practicum.model.User;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.practicum.order.OrderGenerator.*;
import static org.practicum.user.UserGenerator.randomUser;
import static org.practicum.utils.Utils.deleteUserAfterTestExecution;
import static org.practicum.utils.Utils.getTokenAtUserCreation;


public class OrderCreationTest {

    private static  final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private ValidatableResponse response;
    private User user;

    @Before
    public void setUP(){
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void tearDown() {
        deleteUserAfterTestExecution(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Создание заказа без авторизации, проверка ответа и его кода")
    public void createOrderWithoutAuthExpects200()  {
        OrderClient orderClient = new OrderClient();

        Order order = randomOrder();
        ValidatableResponse response = orderClient.create(order);
        response.assertThat().statusCode(200)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("name",notNullValue())
                .assertThat().body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Создание заказа с авторизацией, проверка ответа и его кода")
    public void createOrderWithAuthExpects200()  {
        user = randomUser();
        UserClient userClientClient = new UserClient();
        OrderClient orderClient = new OrderClient();
        response = userClientClient.create(user);

        Order order = randomOrder();
        ValidatableResponse response2 = orderClient.create(order, getTokenAtUserCreation(response));
        response2.assertThat().statusCode(200)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("name", notNullValue())
                .assertThat().body("order.ingredients", notNullValue())
                .assertThat().body("order._id", notNullValue())
                .assertThat().body("order.owner.name",notNullValue())
                .assertThat().body("order.owner.email",notNullValue())
                .assertThat().body("order.owner.createdAt",notNullValue())
                .assertThat().body("order.owner.updatedAt",notNullValue())
                .assertThat().body("order.status",equalTo("done"))
                .assertThat().body("order.name",notNullValue())
                .assertThat().body("order.createdAt",notNullValue())
                .assertThat().body("order.updatedAt",notNullValue())
                .assertThat().body("order.number",notNullValue())
                .assertThat().body("order.price",notNullValue())
                .assertThat().body(matchesJsonSchemaInClasspath("orderWithAuthResponseSchema.json"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, с авторизацией")
    @Description("Создание заказа без ингредиентов, с авторизацией, проверка ответа и его кода")
    public void createOrderWithAuthNoIngredientsExpects400()  {
        user = randomUser();
        UserClient userClientClient = new UserClient();
        OrderClient orderClient = new OrderClient();
        response = userClientClient.create(user);

        Order order = orderWithoutIngridients();
        ValidatableResponse response2 = orderClient.create(order, getTokenAtUserCreation(response));
        response2.assertThat().statusCode(400)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, без авторизации")
    @Description("Создание заказа без ингредиентов, без  авторизации, проверка ответа и его кода")
    public void createOrderWithoutAuthNoIngredientsExpects400()  {
        OrderClient orderClient = new OrderClient();
        Order order = orderWithoutIngridients();
        ValidatableResponse response2 = orderClient.create(order);

        response2.assertThat().statusCode(400)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов, без авторизации")
    @Description("Создание заказа с неверным хешем ингредиентов, без  авторизации, проверка ответа и его кода")
    public void createOrderWithoutAuthWithWrongIngredientsExpects500()  {
        OrderClient orderClient = new OrderClient();
        Order order = orderWithWrongIngredient();
        ValidatableResponse response2 = orderClient.create(order);

        response2.assertThat().statusCode(500);
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов, c авторизацией")
    @Description("Создание заказа с неверным хешем ингредиентов, c  авторизацией, проверка ответа и его кода")
    public void createOrderWithAuthWithWrongIngredientsExpects500()  {
        user = randomUser();
        UserClient userClientClient = new UserClient();
        OrderClient orderClient = new OrderClient();
        response = userClientClient.create(user);

        Order order = orderWithWrongIngredient();
        ValidatableResponse response2 = orderClient.create(order, getTokenAtUserCreation(response));

        response2.assertThat().statusCode(500);
    }
}
