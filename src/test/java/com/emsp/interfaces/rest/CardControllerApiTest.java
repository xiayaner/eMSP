package com.emsp.interfaces.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CardControllerApiTest {

    @LocalServerPort
    private int port;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7")
            .withDatabaseName("emsp_test")
            .withUsername("test")
            .withPassword("test")
            .withCommand("--default-authentication-plugin=mysql_native_password");

    @DynamicPropertySource
    static void registerDbProperties(DynamicPropertyRegistry registry) {
        // 添加必要的连接参数
        String jdbcUrl = mysql.getJdbcUrl() +
                "?allowPublicKeyRetrieval=true" +
                "&useSSL=false" +
                "&serverTimezone=UTC";

        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void createCard_shouldReturn201AndCardDetails() {
        String requestBody = "{\"uid\": \"1234567890\", " +
                            "\"visibleNumber\": \"CARD-1234\", " +
                            "\"contractId\": \"QR567STU567890\"}";

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/cards")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("uid", equalTo("1234567890"))
            .body("visibleNumber", equalTo("CARD-1234"))
            .body("contractId", equalTo("QR567STU567890"))
            .body("status", equalTo("CREATED"));
    }

    @Test
    void assignCardToAccount_shouldLinkCardAndAccount() {
        // 创建账户和卡
        String accountId = createTestAccount("cardowner@example.com");
        String cardId = createTestCard();

        given()
            .contentType(ContentType.JSON)
            .body("{\"accountId\": " + accountId + "}")
        .when()
            .patch("/api/cards/" + cardId + "/assign")
        .then()
            .statusCode(204);
    }

    @Test
    void activateCard_shouldChangeStatusToActivated() {
        // 创建卡并分配到账户
        String accountId = createTestAccount("cardactivator@example.com");
        String cardId = createTestCard();
        assignCardToAccount(cardId, accountId);

        given()
            .contentType(ContentType.JSON)
            .body("{\"status\": \"ACTIVATED\"}")
        .when()
            .patch("/api/cards/" + cardId + "/status")
        .then()
            .statusCode(204);
    }

    // ================== 辅助方法 ================== //

    private String createTestCard() {
        String uid = String.valueOf(System.currentTimeMillis() + Math.random());

        String requestBody = String.format("{\"uid\": \"%s\", " +
                "\"visibleNumber\": \"TEST-VIS\", " +
                "\"contractId\": \"QR567STU567890\"}", uid);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/cards")
                .then()
                .extract()
                .response();
        return response.path("id").toString();
    }

    private void assignCardToAccount(String cardId, String accountId) {
        given()
            .contentType(ContentType.JSON)
            .body("{\"accountId\": " + accountId + "}")
        .when()
            .patch("/api/cards/" + cardId + "/assign");
    }

    private String createTestAccount(String email) {
        String requestBody = "{\"email\": \"" + email + "\"}";

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/accounts")
                .then()
                .extract()
                .path("id").toString();
    }
}