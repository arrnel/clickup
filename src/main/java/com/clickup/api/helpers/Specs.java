package com.clickup.api.helpers;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {

    public static RequestSpecification requestSpec(String token) {

        return RestAssured.with()
                .filter(new AllureRestAssured())
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();

    }

    public static ResponseSpecification responseSpec() {

        return new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .build();

    }

    public static ResponseSpecification responseValidSpec() {

        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(StatusCode.OK)
                .build();

    }

}
