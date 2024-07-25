package org.practicum.user;


import com.github.javafaker.Faker;
import org.practicum.model.User;



public class UserGenerator {
    public static Faker faker = new Faker();

    public static User randomUser() {
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .withName(faker.name().firstName());
    }
    public static User userWithoutEmail() {
        return new User()
                .withPassword(faker.internet().password())
                .withName(faker.name().firstName());
    }

    public static User userWithoutPassword() {
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().firstName());
    }

    public static User userWithoutName() {
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password());
    }

}
