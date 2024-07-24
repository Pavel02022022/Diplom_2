import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.practicum.user.UserClient;
import org.practicum.model.User;

import static org.hamcrest.Matchers.equalTo;
import static org.practicum.user.UserGenerator.randomUser;
import static org.practicum.utils.Utils.deleteUserAfterTestExecution;
import static org.practicum.utils.Utils.getTokenAtUserCreation;

public class UserInfoTest {
    private static  final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private ValidatableResponse response;
    private ValidatableResponse response2;

    @Before
    public void setUP(){
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void tearDown() {
        deleteUserAfterTestExecution(response);
        deleteUserAfterTestExecution(response2);
    }


    @Test
    @DisplayName("Изменение информации о пользователе c авторизацией")
    @Description("Изменение информации о пользователе c авторизацией, проверка ответа и его кода")
    public void changeUserInfoWithAuthExpects200()  {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        response = userClientClient.create(user);

        User changedUser = randomUser();

        ValidatableResponse response2 = userClientClient.changeUserInfo(getTokenAtUserCreation(response),changedUser );

        response2.assertThat().statusCode(200)
                 .assertThat().body("success", equalTo(true))
                 .assertThat().body("user.email", equalTo(changedUser.getEmail()))
                 .assertThat().body("user.name", equalTo(changedUser.getName()));
    }

    @Test
    @DisplayName("Изменение информации о пользователе без авторизации")
    @Description("Изменение информации о пользователе авторизации, проверка ответа и его кода")
    public void changeUserInfoWithoutAuthorizationExpects401()  {
        User user = randomUser();
        UserClient userClientClient = new UserClient();
        response = userClientClient.create(user);

        User changedUser = randomUser();

        ValidatableResponse response2 = userClientClient.changeUserInfo("wrongToken",changedUser);

        response2.assertThat().statusCode(401)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"));
    }


    @Test
    @DisplayName("Изменение почты пользователе на почту, которая уже используется")
    @Description("Изменение почты пользователе на почту, которая уже используется, проверка ответа и его кода")
    public void changeUserInfoWithInUseEmailExpects403()  {
        User user = randomUser();
        User user2 = randomUser();
        UserClient userClientClient = new UserClient();
        response = userClientClient.create(user);
        response2 = userClientClient.create(user2);

        User changedUser = new User().withEmail(user2.getEmail());

        ValidatableResponse response2 = userClientClient.changeUserInfo(getTokenAtUserCreation(response), changedUser);

        response2.assertThat().statusCode(403)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("User with such email already exists"));
    }

}
