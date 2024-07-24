package org.practicum.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCreds {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    public UserCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
