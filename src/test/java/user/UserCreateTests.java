package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.user.UserCreate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static user.UserGenerator.randomUser;
import static user.UserGenerator.randomUserNotEmail;

public class UserCreateTests {
    private String accessToken;

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    UserClient userClient = new UserClient();


    @Test
    @DisplayName("Создание пользователя")
    public void createUser() {
        UserCreate user = randomUser();
        userClient.create(user)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("accessToken");

    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    public void createDoubleUser() {
        UserCreate user = randomUser();
        userClient.create(user);
        userClient.create(user)
                .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("User already exists"))
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserNotEmail() {
        UserCreate user = randomUserNotEmail();
        userClient.create(user)
                .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"))
                .extract()
                .path("accessToken");
    }

}
