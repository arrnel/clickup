package com.clickup.api.models;

import com.clickup.api.helpers.Endpoint;
import com.clickup.api.helpers.Specs;
import com.clickup.api.dto.TeamsDTO;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class TeamModels {

    Endpoint endpoint = new Endpoint();
    String token;

    public TeamModels(String token){
        this.token = token;
    }

    @Step("Reading teams")
    public TeamsDTO get(){
        String url = endpoint.getTeam();

        // @formatter:off
        TeamsDTO teams = given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().as(TeamsDTO.class);
        // @formatter:on

        return teams;
    }

}
