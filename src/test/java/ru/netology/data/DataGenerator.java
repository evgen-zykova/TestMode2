package ru.netology.data;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(UserInfo userInfo) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(userInfo) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo getActiveUser() {
            UserInfo userInfo = new UserInfo(getRandomLogin(), getRandomPassword(), "active");
            sendRequest(userInfo);
            return userInfo;
        }

        public static UserInfo getBlockedUser() {
            UserInfo userInfo = new UserInfo(getRandomLogin(), getRandomPassword(), "blocked");
            sendRequest(userInfo);
            return userInfo;
        }

        public static UserInfo getInvalidPasswordUser(String status) {
            String login = getRandomLogin();
            sendRequest(new UserInfo(login, getRandomPassword(), status));
            return new UserInfo(login, getRandomPassword(), status);
        }

        public static UserInfo getInvalidLoginUser(String status) {
            String password = getRandomPassword();
            sendRequest(new UserInfo(getRandomLogin(), password, status));
            return new UserInfo(getRandomLogin(), password, status);
        }
    }
}


