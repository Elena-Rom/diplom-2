package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.user.UserCreate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static user.UserGenerator.randomUser;
import static utils.Utils.randomString;

public class UserChangeTests {

    private String accessToken;
    private String password;
    private String name;
    private String email;

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
    @DisplayName("Изменение данных не авторизованного пользователя")
    public void changeUserNotAuth() {
        UserCreate user = randomUser();
        password = randomString(16);
        name = randomString(6);
        email = randomString(5 ) + "@gmail.com";
        accessToken = userClient.create(user)
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        userClient.change(new UserCreate(email, password, name), "")
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    public void changeUserAuth() {
        UserCreate user = randomUser();
        password = randomString(16);
        name = randomString(6);
        email = randomString(5 ) + "@gmail.com";
        accessToken = userClient.create(user)
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        userClient.change(new UserCreate(email, password, name), accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }
}
