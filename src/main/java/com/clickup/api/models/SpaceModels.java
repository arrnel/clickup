package com.clickup.api.models;

import com.clickup.api.helpers.Endpoint;
import com.clickup.api.helpers.Specs;
import com.clickup.api.dto.ErrorDTO;
import com.clickup.api.dto.SpaceDTO;
import com.clickup.api.dto.SpacesDTO;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class SpaceModels {

    SpaceDTO space;
    String spaceId;
    String teamId;
    String token;

    Endpoint endpoint = new Endpoint();
    Logger log = LoggerFactory.getLogger(SpaceModels.class);

    public SpaceModels(String token) {
        this.token = token;
    }

    @Step("Creating space")
    public SpaceDTO create(SpaceDTO space) {

        teamId = space.getTeamId();
        String url = endpoint.createSpace(teamId);

        log.info("Creating new space in team with id = " + teamId);

        // @formatter:off
        space = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(space)
                .when()
                    .log().ifValidationFails()
                    .post(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(SpaceDTO.class);
        // @formatter:on

        spaceId = space.getId();

        log.info(String.format("New space with id = \"%s\" and \"%s\" was created", spaceId, space.getName()));

        return space;
    }

    @Step("Create new space in team with id =\"{teamId}\" and spaceName = \"{spaceName}\"")
    public SpaceDTO create(String teamId, String spaceName) {

        String url = endpoint.createSpace(teamId);

        JSONObject requestBody = new JSONObject()
                .put("name", spaceName);

        log.info("Creating new space in team with id = " + teamId);

        // @formatter:off
        SpaceDTO space = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .post(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(SpaceDTO.class);
        // @formatter:on

        spaceId = space.getId();

        log.info(String.format("New space with id = \"%s\" and name = \"%s\" was created", spaceId, space.getName()));

        return space;

    }

    @Step("Create new space and expect error")
    public ErrorDTO createHasError(SpaceDTO space) {

        String teamId = space.getTeamId();
        String url = endpoint.createSpace(teamId);

        log.info("Creating new space in team with id = " + teamId);

        //@formatter:off
        Response response = RestAssured.given()
                .spec(Specs.requestSpec(token))
                    .body(space)
                .when()
                    .log().ifValidationFails()
                .post(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Space not created. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Create new space and expect error")
    public ErrorDTO createHasError(String teamId, String spaceName) {

        String url = endpoint.createSpace(teamId);

        JSONObject requestBody = new JSONObject()
                .put("name", spaceName);

        log.info("Creating new space in team with id = " + teamId);

        //@formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .post(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Space not created. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading space with spaceId = \"{spaceId}\"")
    public SpaceDTO get(String spaceId) {
        String url = endpoint.getSpace(spaceId);

        log.info("Get space with id = '" + spaceId + "'");

        //@formatter:off
        space = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(SpaceDTO.class);
        //@formatter:on

        log.info("Space with id = '" + spaceId + "' exists");

        return space;
    }

    @Step("Reading space with spaceId = \"{spaceId}\" and expect error")
    public ErrorDTO getHasError(String spaceId) {

        String url = endpoint.getSpaces(spaceId);

        log.info("Get space with id = '" + spaceId + "'");

        //@formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to get space. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading spaces in team with id = \"{teamId}\"")
    public SpacesDTO gets(String teamId) {

        String url = endpoint.getSpaces(teamId);

        log.info("Get spaces in team with id = '" + teamId + "'");

        //@formatter:off
        SpacesDTO spaces = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(SpacesDTO.class);
        //@formatter:on

        int spacesCount = spaces.getSpaces().size();
        String[] spacesIds = new String[spacesCount];

        for (int i = 0; i < spacesCount; i++)
            spacesIds[i] = spaces.getSpaces().get(i).getId();

        log.info(String.format("Exists spaces count = \"%s\". Spaces = %s ", spacesCount, Arrays.toString(spacesIds)));

        return spaces;
    }

    @Step("Reading spaces in team with id = \"{teamId}\" with query params")
    public SpacesDTO gets(String teamId, HashMap<String, String> params) {

        String url = endpoint.getSpaces(teamId);

        log.info("Get spaces in team with id = '" + teamId + "'");

        //@formatter:off
        SpacesDTO spaces = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .queryParams(params)
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(SpacesDTO.class);
        //@formatter:on

        int spacesCount = spaces.getSpaces().size();
        String[] spacesIds = new String[spacesCount];

        for (int i = 0; i < spacesCount; i++) {
            spacesIds[i] = spaces.getSpaces().get(i).getId();
        }

        log.info(String.format("Exists spaces count = \"%s\". Spaces = %s ", spacesCount, Arrays.toString(spacesIds)));

        return spaces;
    }

    @Step("Reading spaces in team with id = \"{teamId}\" and expects error")
    public ErrorDTO getsHasError(String teamId) {

        String url = endpoint.getSpaces(teamId);

        log.info("Get spaces in team with id = '" + teamId + "'");

        //@formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to get space. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading spaces in team with id = \"{teamId}\" with query params and expects error")
    public ErrorDTO getsHasError(String teamId, HashMap<String, String> params) {

        String url = endpoint.getSpaces(teamId);

        log.info("Get spaces in team with id = '" + teamId + "'");

        //@formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .queryParams(params)
                .when()
                    .log().ifValidationFails()
                    .get(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to get space. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Updating space")
    public SpaceDTO update(SpaceDTO space) {

        String spaceId = space.getId();
        String url = endpoint.updateSpace(spaceId);

        log.info("Updating space with id = '" + spaceId + "'");

        //@formatter:off
        space = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(space)
                .when()
                    .log().ifValidationFails()
                    .put(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(SpaceDTO.class);
        //@formatter:on

        log.info("Space with id = '" + spaceId + "' was updated");

        return space;

    }

    @Step("Updating spaceName with id = \"{spaceId}\" to \"{spaceName}\"")
    public SpaceDTO update(String spaceId, String spaceName) {

        String url = endpoint.updateSpace(spaceId);

        JSONObject requestBody = new JSONObject()
                .put("name", spaceName);

        log.info("Updating space with id = '" + spaceId + "'");

        //@formatter:off
        space = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .put(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(SpaceDTO.class);
        //@formatter:on

        log.info("Space with id = '" + spaceId + "' was updated");

        return space;

    }

    @Step("Updating space and expect error")
    public ErrorDTO updateHasError(SpaceDTO space) {

        String spaceId = space.getId();
        String url = endpoint.updateSpace(spaceId);

        log.info("Updating space with id = '" + spaceId + "'");

        //@formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(space)
                .when()
                    .log().ifValidationFails()
                    .put(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Space not updated. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Updating space with id = \"{spaceId}\" to name = \"{spaceName}\" and expect error")
    public ErrorDTO updateHasError(String spaceId, String spaceName) {

        String url = endpoint.updateSpace(spaceId);

        JSONObject requestBody = new JSONObject()
                .put("name", spaceName);

        log.info("Updating space with id = '" + spaceId + "'");

        //@formatter:off
        Response response = RestAssured.given()
                .spec(Specs.requestSpec(token))
                .body(requestBody.toString())
                .when()
                .log().ifValidationFails()
                .put(url);

        ErrorDTO error = response.then()
                .spec(Specs.responseSpec())
                .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Space not updated. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Remove space")
    public void delete(String spaceId) {

        String url = endpoint.deleteSpace(spaceId);

        log.info("Removing space with id = '" + spaceId + "'");

        //@formatter:off
        RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .delete(url)
                .then()
                    .spec(Specs.responseValidSpec());
        //@formatter:on

        log.info("Space with id = '" + spaceId + "' was removed");

    }

    @Step("Returns SpaceDTO removing error")
    public ErrorDTO deleteHasError(String spaceId) {

        String url = endpoint.deleteSpace(spaceId);

        log.info("Removing space with id = '" + spaceId + "'");

        //@formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .delete(url);


        ErrorDTO error = response.then()
                .spec(Specs.responseSpec())
                .extract().response().as(ErrorDTO.class);
        //@formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Space not deleted. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }


}
