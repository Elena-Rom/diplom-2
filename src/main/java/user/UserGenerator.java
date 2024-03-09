package user;

import models.user.UserCreate;

import static utils.Utils.randomString;

public class UserGenerator {
    public static UserCreate randomUser() {
        return new UserCreate()
                .withName(randomString(8))
                .withPassword(randomString(16))
                .withEmail(randomString(5)+"@gmail.com");
    }

    public static UserCreate randomUserNotEmail() {
        return new UserCreate()
                .withName(randomString(8))
                .withPassword(randomString(16));
    }
}
