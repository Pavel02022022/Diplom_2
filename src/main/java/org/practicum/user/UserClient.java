package org.practicum.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.practicum.model.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    @Step("Create user")
    public ValidatableResponse create(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register")
                .then();

    }

    @Step("Login user")
    public ValidatableResponse login(String email, String password) {
        UserCreds creds = new UserCreds(email, password);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post("/api/auth/login")
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse delete(String token) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("api/auth/user")
                .then();

    }

    @Step("Change info about user")
    public ValidatableResponse changeUserInfo(String token, User changedUser) {

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(changedUser)
                .when()
                .patch("/api/auth/user")
                .then();

    }

}
