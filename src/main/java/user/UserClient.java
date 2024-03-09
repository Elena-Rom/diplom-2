package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.user.UserCreate;
import models.user.UserLogin;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String USER_CREATE_URL = "api/auth/register";
    private static final String USER_LOGIN_URL = "api/auth/login";
    private static final String USER_DELETE_CHANGE_URL = "api/auth/user";

    @Step("Создание пользователя")
    public Response create(UserCreate userCreate){
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .body(userCreate)
                .when()
                .post(USER_CREATE_URL)
                .then()
                .extract();
//                .path("accessToken");
    }

    @Step("Авторизация пользователя")
    public Response login(UserLogin userLogin){
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .body(userLogin)
                .when()
                .post(USER_LOGIN_URL)
                .then()
                .extract();
    }

    @Step("Удаления пользователя")
    public Response deleteUser(String  accessToken){
        return  given()
                .header("authorization", "bearer " + accessToken)
                .delete(USER_DELETE_CHANGE_URL);
    }

    @Step("Изменение данных пользователя")
    public Response change(UserCreate userCreate, String  accessToken){
        return given()
                .header( "authorization", "bearer " + accessToken)
                .and()
                .body(userCreate)
                .when()
                .patch(USER_DELETE_CHANGE_URL);

    }
}
