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

import static org.hamcrest.CoreMatchers.equalTo;
import static user.UserGenerator.randomUser;

public class OrderGetTests {
    private String accessToken;

    private OrderData orderData;
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    private List<Ingredients> ingredientsData = new ArrayList<>();

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        UserCreate user = randomUser();
        accessToken = userClient.create(user)
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        Response response = orderClient.ingredientsGetAll();
        ingredientsData = response.body().as(IngredientsResponsed.class).getData();
        orderClient.orderCreate(new OrderData(List.of(ingredientsData.get(0).get_id(), ingredientsData.get(ingredientsData.size() - 1).get_id())), accessToken);
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }


    @Test
    @DisplayName("Получение заказов с авторизацией")
    public void orderGetAllWithAuth() {
            orderClient.orderGetAll(accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    public void orderGetAllWithNotAuth() {
        orderClient.orderGetAll("")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }
}
