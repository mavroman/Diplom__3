package client;

import io.restassured.response.Response;
import model.User;


import static io.restassured.RestAssured.given;

public class ApiClient {

    public static final String BASE_URL = "https://stellarburgers.education-services.ru/";

    // Создание нового пользователя
    public Response createUser(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(user)
                .when()
                .post("api/auth/register");
    }

    // Удаление пользователя
    public Response deleteUser(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete("api/auth/user");
    }

    // Залогинить пользователя
    public Response loginUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(user)
                .when()
                .post("auth/login");
    }



}
