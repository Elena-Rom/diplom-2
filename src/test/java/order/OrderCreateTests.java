package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.order.Ingredients;
import models.order.IngredientsResponsed;
import models.order.OrderData;
import models.user.UserCreate;
import orders.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static user.UserGenerator.randomUser;


public class OrderCreateTests {
    private String accessToken;

    private OrderData orderData;

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
    OrderClient orderClient = new OrderClient();
    private List<Ingredients> ingredientsData = new ArrayList<>();


    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    public void createOrderWithAuthNotIngredients() {
        UserCreate user = randomUser();
        accessToken = userClient.create(user)
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        orderClient.orderCreate(new OrderData(List.of()), accessToken)
                .then()
                .assertThat()
                .statusCode(400)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с ингредиентами")
    public void createOrderWithAuthWithIngredients() {
        UserCreate user = randomUser();
        accessToken = userClient.create(user)
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        Response response = orderClient.ingredientsGetAll();
        ingredientsData = response.body().as(IngredientsResponsed.class).getData();
        orderClient.orderCreate(new OrderData(List.of(ingredientsData.get(0).get_id(), ingredientsData.get(ingredientsData.size() - 1).get_id())), accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиeнтами")
    public void createOrderNoAuthWithIngredients() {
        Response response = orderClient.ingredientsGetAll();
        ingredientsData = response.body().as(IngredientsResponsed.class).getData();
        orderClient.orderCreate(new OrderData(List.of(ingredientsData.get(0).get_id(), ingredientsData.get(ingredientsData.size() - 1).get_id())), "")
                .then()
                .assertThat()
                .statusCode(400)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов")
    public void createOrderNotCorrectHash() {
        UserCreate user = randomUser();
        accessToken = userClient.create(user)
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        Response response = orderClient.ingredientsGetAll();
        ingredientsData = response.body().as(IngredientsResponsed.class).getData();
        orderClient.orderCreate(new OrderData(List.of(ingredientsData.get(0).get_id(), UUID.randomUUID().toString())), accessToken)
                .then()
                .assertThat()
                .statusCode(500);
    }
}

