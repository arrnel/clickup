package com.clickup.api.models;

import com.clickup.api.helpers.Endpoint;
import com.clickup.api.helpers.Specs;
import com.clickup.api.dto.ErrorDTO;
import com.clickup.api.dto.ListDTO;
import com.clickup.api.dto.ListsDTO;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ListModels {

    String token;

    public ListModels(String token) {
        this.token = token;
    }

    Logger log = LoggerFactory.getLogger(ListModels.class);
    Endpoint endpoint = new Endpoint();

    @Step("Creating list with name = \"{listName}\" in folder with id = \"{folderId}\"")
    public ListDTO create(String folderId, String listName){

        String url = endpoint.createList(folderId);

        JSONObject requestBody = new JSONObject()
                .put("name", listName);

        log.info("Creating new list");

        // @formatter:off
        ListDTO list = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .post(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(ListDTO.class);
        // @formatter:on

        log.info("New list with id = " + list.getId() + " was created");
        log.info(list.toString());

        return list;

    }

    @Step("Creating folder")
    public ListDTO create(ListDTO list){

        String url = endpoint.createList(list.getFolderId());

        log.info("Creating new list");

        // @formatter:off
        list = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(list)
                .when()
                    .log().ifValidationFails()
                    .post(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(ListDTO.class);
        // @formatter:on

        log.info("New list with id = " + list.getId() + " was created");
        log.info(list.toString());

        return list;

    }

    @Step("Creating new list with name = \"{listName}\" in folder with id = \"{folderId}\" and expect error")
    public ErrorDTO createHasError(String folderId, String listName){
        
        String url = endpoint.createList(folderId);

        JSONObject requestBody = new JSONObject()
                .put("name", listName);

        log.info("Creating new list");

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .post(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("List not created. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Creating new list and expect error")
    public ErrorDTO createHasError(ListDTO list){

        String url = endpoint.createList(list.getFolderId());

        log.info("Creating new list");

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(list)
                .when()
                    .log().ifValidationFails()
                    .post(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("List not created. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading list with id = \"{listId}\"")
    public ListDTO get(String listId){

        String url = endpoint.getList(listId);

        // @formatter:off
        ListDTO list = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(ListDTO.class);
        // @formatter:on

        log.info("List with id = " + listId + " exists");

        return list;

    }

    @Step("Reading list with id = \"{listId}\" and expect error")
    public ErrorDTO getHasError(String listId){

        String url = endpoint.getList(listId);

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to get list. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading lists in folderId = \"{listId}\"")
    public ListsDTO gets(String folderId){

        String url = endpoint.getLists(folderId);

        // @formatter:off
        ListsDTO lists = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(ListsDTO.class);
        // @formatter:on

        log.info("Exists lists count = " + lists.getLists().size());

        return lists;

    }

    @Step("Reading lists in folderId with id = \"{folderId}\"")
    public ListsDTO gets(String folderId, HashMap<String, String> params){

        String url = endpoint.getLists(folderId);

        // @formatter:off
        ListsDTO lists = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .queryParams(params)
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(ListsDTO.class);
        // @formatter:on

        log.info("Exists lists count = " + lists.getLists().size());

        return lists;

    }

    @Step("Reading lists in folderId with id = \"{folderId}\" and expect error")
    public ErrorDTO getsHasError(String folderId){

        String url = endpoint.getLists(folderId);

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to get list. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading lists in folderId with id = \"{folderId}\" and expect error")
    public ErrorDTO getsHasError(String folderId, HashMap<String, String> params){

        String url = endpoint.getLists(folderId);

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .queryParams(params)
                .when()
                    .log().ifValidationFails()
                    .get(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to get list. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Updating list with id = \"{listId}\" to name = \"{listName}\"")
    public ListDTO update(String listId, String listName){

        String url = endpoint.updateList(listId);

        JSONObject requestBody = new JSONObject()
                .put("name", listName);

        log.info("Updating list with id = '" + listId + "'");

        // @formatter:off
        ListDTO list = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .put(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(ListDTO.class);
        // @formatter:on

        log.info("List with id = '" + listId + "' was updated");

        return list;

    }

    @Step("Updating list with id = \"{listId}\" to name = \"{listName}\"")
    public ListDTO update(ListDTO list){

        String url = endpoint.updateList(list.getId());

        // @formatter:off
        list = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(list)
                .when()
                    .log().ifValidationFails()
                    .put(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(ListDTO.class);
        // @formatter:on

        log.info("List with id = " + list.getId() + " was updated");

        return list;

    }

    @Step("Updating list with id = \"{listId}\" to name = \"{listName}\" and expect error")
    public ErrorDTO updateHasError(String listId, String listName){

        String url = endpoint.updateList(listId);

        JSONObject requestBody = new JSONObject()
                .put("name", listName);

        log.info("Updating list with id = '" + listId + "'");

        // @formatter:off
        Response response = RestAssured.given()
                .spec(Specs.requestSpec(token))
                .body(requestBody.toString())
                .when()
                .log().ifValidationFails()
                .put(url);

        ErrorDTO error = response.then()
                .spec(Specs.responseSpec())
                .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to update list. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Updating list with id = \"{listId}\" to name = \"{listName}\" and expect error")
    public ErrorDTO updateHasError(ListDTO list){

        String listId = list.getId();
        String url = endpoint.updateList(listId);

        log.info("Updating list with id = '" + listId + "'");

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(list)
                .when()
                    .log().ifValidationFails()
                    .put(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to update list. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Remove list with id = \"{listId}\"")
    public void delete(String listId){

        String url = endpoint.deleteList(listId);

        // @formatter:off
        RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .delete(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().body().toString();
        // @formatter:on

        log.info("List with id = " + listId + " was deleted");

    }


    @Step("Remove list with id = \"{listId}\" and expect error")
    public ErrorDTO deleteHasError(String listId){

        String url = endpoint.deleteList(listId);

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .delete(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to update list. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

}
