package com.emsp.interfaces.rest;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AccountControllerApiTest {

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
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    // ================== 账户API测试 ================== //
    
    @Test
    void createAccount_shouldReturn201AndAccountDetails() {
        String requestBody = "{\"email\": \"test@example.com\"}";

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/accounts")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("email", equalTo("test@example.com"))
            .body("status", equalTo("CREATED"));
    }

    @Test
    void activateAccount_shouldChangeStatusToActivated() {
        // 先创建账户
        String accountId = createTestAccount("inactive@example.com");
        
        given()
            .contentType(ContentType.JSON)
            .body("{\"status\": \"ACTIVATED\"}")
        .when()
            .patch("/api/accounts/" + accountId + "/status")
        .then()
            .statusCode(204);
    }

    @Test
    void searchAccounts_shouldReturnPaginatedResults() {
        // 创建测试数据
        createTestAccount("user1@example.com");
        createTestAccount("user2@example.com");
        
        given()
            .param("lastUpdated", "2020-01-01T00:00:00Z")
            .param("page", 0)
            .param("size", 10)
        .when()
            .get("/api/accounts/search")
        .then()
            .statusCode(200)
            .body("content.size()", greaterThanOrEqualTo(2))
            .body("totalPages", greaterThanOrEqualTo(1))
            .body("totalElements", greaterThanOrEqualTo(2));
    }

    // ================== 辅助方法 ================== //
    
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