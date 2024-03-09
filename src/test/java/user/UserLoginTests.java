package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.user.UserCreate;
import models.user.UserLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static user.UserGenerator.randomUser;
import static utils.Utils.randomString;

public class UserLoginTests {
    private String login;

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
    @DisplayName("Авторизация пользователя")
    public void loginUser() {
        UserCreate user = randomUser();
        userClient.create(user);
        userClient.login(UserLogin.from(user))
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");

    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    public void loginUserNotPassword() {
        UserCreate user = randomUser();
        login = randomString(8);
        userClient.create(user);
        userClient.login(new UserLogin(login, user.getPassword()))
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"))
                .extract()
                .path("accessToken");

    }
}
