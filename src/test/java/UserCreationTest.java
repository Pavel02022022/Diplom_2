import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.practicum.user.UserClient;
import org.practicum.model.User;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.practicum.user.UserGenerator.*;
import static org.practicum.utils.Utils.deleteUserAfterTestExecution;


public class UserCreationTest {
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
    @DisplayName("Создание пользователя")
    @Description("Создание пользователя со случайными данными, проверка ответа и его кода")
    public void userCreationExpects200(){
        user = randomUser();
        UserClient userClient = new UserClient();
        response = userClient.create(user);

        response.assertThat().statusCode(200)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", equalTo(user.getEmail()))
                .assertThat().body("user.name", equalTo(user.getName()))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Создание пользователя, который уже зарегистрирован, проверка ответа и его кода")
    public void doubleUserCrationExpects403(){
        user = randomUser();
        UserClient userClientClient = new UserClient();
        userClientClient.create(user);
        response = userClientClient.create(user);

        response.assertThat().statusCode(403)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Создание пользователя без email, проверка ответа и его кода")
    public void userCreationWithoutEmailExpects403(){
        user = userWithoutEmail();
        UserClient userClientClient = new UserClient();
        response = userClientClient.create(user);

        response.assertThat().statusCode(403)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Создание пользователя без пароля, проверка ответа и его кода")
    public void userCreationWithoutPasswordExpects403(){
        user = userWithoutPassword();
        UserClient userClientClient = new UserClient();
        response = userClientClient.create(user);

        response.assertThat().statusCode(403)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Создание пользователя без имени, проверка ответа и его кода")
    public void userCreationWithoutNameExpects403(){
        user = userWithoutName();
        UserClient userClientClient = new UserClient();
        response = userClientClient.create(user);

        response.assertThat().statusCode(403)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

}
