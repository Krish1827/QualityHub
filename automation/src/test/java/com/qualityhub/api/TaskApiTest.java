
package com.qualityhub.api;

import io.restassured.RestAssured;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@Epic("QualityHub")
@Feature("Task API")
class TaskApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @Story("Retrieve tasks")
    @Description("Verify that the API returns all tasks with an HTTP 200 response and JSON content.")
    void shouldReturnAllTasks() {

        given()
            .when()
            .get("/api/tasks")
            .then()
            .statusCode(200)
            .contentType(containsString("application/json"));
    }

    @Test
    @Story("Handle missing tasks")
    @Description("Verify that requesting a task with an invalid ID returns HTTP 404.")
    void shouldReturnNotFoundForInvalidTask() {

        given()
            .when()
            .get("/api/tasks/999999")
            .then()
            .statusCode(404);
    }
}
