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
import static org.practicum.user.UserGenerator.randomUser;
import static org.practicum.utils.Utils.deleteUserAfterTestExecution;


public class UserLoginTest {
    private static  final String BASE_URL = "https://stellarburgers.nomoreparties.site";
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
    @DisplayName("Логин пользователя")
    @Description("Логин пользователя со случайными данными, проверка ответа и его кода")
    public void userLoginExpects200()  {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        response = userClientClient.create(user);

        ValidatableResponse response2 = userClientClient.login(user.getEmail(), user.getPassword());
        response2.assertThat().statusCode(200)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", equalTo(user.getEmail()))
                .assertThat().body("user.name", equalTo(user.getName()))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин пользователя c неверным логином")
    @Description("Логин пользователя c неверным логином, проверка ответа и его кода")
    public void loginUserWithWrongLoginExpects401() {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        userClientClient.create(user);

        ValidatableResponse response = userClientClient.login(user.getEmail(), "wrongPass");
        response.assertThat().statusCode(401)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя c неверным паролем")
    @Description("Логин пользователя c неверным паролем, проверка ответа и его кода")
    public void loginUserWithWrongPasswordExpects401()  {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        userClientClient.create(user);

        ValidatableResponse response = userClientClient.login("wrongLogin", user.getPassword());
        response.assertThat().statusCode(401)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

}
