package orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.order.OrderData;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDER_CREATE_GET_URL = "api/orders";
    private static final String INGREDIENTS = "api/ingredients";

    @Step("Создание заказа")
    public Response orderCreate(OrderData orderData, String accessToken) {
        return (Response) given()
                .headers("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .body(orderData)
                .when()
                .post(ORDER_CREATE_GET_URL)
                .then()
                .extract();
    }

    @Step("Получение всех заказов")
    public Response orderGetAll(String accessToken) {
        return (Response) given()
                .headers("authorization", "bearer " + accessToken)
                .when()
                .get(ORDER_CREATE_GET_URL)
                .then()
                .extract();
    }
    @Step("Получение ингредиентов")
    public Response ingredientsGetAll() {
        return (Response) given()
                .headers("Content-type", "application/json")
                .when()
                .get(INGREDIENTS);

    }
}
