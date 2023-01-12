package com.clickup.api.models;

import com.clickup.api.helpers.Endpoint;
import com.clickup.api.helpers.Specs;
import com.clickup.api.dto.ErrorDTO;
import com.clickup.api.dto.FolderDTO;
import com.clickup.api.dto.FoldersDTO;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class FolderModels {

    String token;

    public FolderModels(String token){
        this.token = token;
    }

    FolderDTO folder;
    Endpoint endpoint = new Endpoint();

    Logger log = LoggerFactory.getLogger(FolderModels.class);

    @Step("Creating new folder with name = \"{folderName}\" in space with id = \"{spaceId}\"")
    public FolderDTO create(String spaceId, String folderName) {

        JSONObject requestBody = new JSONObject()
                .put("name", folderName);

        String url = endpoint.createFolder(spaceId);

        log.info(String.format("Creating new folder in space with id = '%s', and name = '%s'", spaceId, folderName));

        // @formatter:off
        FolderDTO folder = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .post(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(FolderDTO.class);
        // @formatter:on

        log.info(String.format("New folder with id = \"%s\" and name = \"%s\" created", folder.getId(), folderName));
        log.info(folder.toString());

        return folder;

    }

    @Step("Creating new folder")
    public FolderDTO create(FolderDTO folder) {

        String spaceId = folder.getSpaceId();
        String url = endpoint.createFolder(spaceId);

        log.info(String.format("Creating new folder in space with id = '%s', and name = '%s'",
                folder.getSpaceId(), folder.getName()));

        // @formatter:off
        folder = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(folder)
                .when()
                    .log().ifValidationFails()
                    .post(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(FolderDTO.class);
        // @formatter:on

        log.info("New folder with id = " + folder.getId() + " was created");
        log.info(folder.toString());

        return folder;

    }

    @Step("Creating new folder with name = \"{folderName}\" in space with id = \"{spaceId}\" and expect error")
    public ErrorDTO createHasError(String spaceId, String folderName) {

        String url = endpoint.createFolder(spaceId);

        JSONObject requestBody = new JSONObject()
                .put("name", folderName);

        log.info(String.format("Trying to create new folder in space with id = \"%s\" and folder name = \"%s\"", spaceId, folderName));

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

        log.info(String.format("Folder not created. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));
        log.info(error.toString());

        return error;


    }

    @Step("Creating new folder and expect error")
    public ErrorDTO createHasError(FolderDTO folder) {

        String spaceId = folder.getId(),
                folderName = folder.getName(),
                url = endpoint.createFolder(spaceId);

        log.info(String.format("Trying to create new folder in space with id = \"%s\" and folder name = \"%s\"", spaceId, folderName));

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(folder)
                .when()
                    .log().ifValidationFails()
                    .post(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Folder not created. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));
        log.info(error.toString());

        return error;

    }

    @Step("Reading folder with id = \"{folderId}\"")
    public FolderDTO get(String folderId) {

        String url = endpoint.getFolder(folderId);

        log.info(String.format("Getting folder with id = '%s'", folderId));

        // @formatter:off
        FolderDTO folder = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(FolderDTO.class);
        // @formatter:on

        log.info("Folder with id = '" + folderId + "' is exists");
        log.info(folder.toString());

        return folder;

    }

    @Step("Reading folder with id = \"{folderId}\" and expect error")
    public ErrorDTO getHasError(String folderId) {

        String url = endpoint.getFolder(folderId);

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

        log.info(String.format("Unable to get folder. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading folders in space with id = \"{spaceId}\"")
    public FoldersDTO gets(String spaceId) {

        String url = endpoint.getFolders(spaceId);

        // @formatter:off
        FoldersDTO folders = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(FoldersDTO.class);
        // @formatter:on

        log.info(String.format("Exists '%s' folder(-s) in space with id = '%s'", folders.getFolders().size(), spaceId));

        return folders;

    }

    @Step("Reading folders in space with id = \"{spaceId}\" with query params")
    public FoldersDTO gets(String spaceId, HashMap<String, String> params) {

        String url = endpoint.getFolders(spaceId);

        // @formatter:off
        FoldersDTO folders = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .queryParams(params)
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(FoldersDTO.class);
        // @formatter:on

        log.info(String.format("Exists '%s' folder(-s) in space with id = '%s'", folders.getFolders().size(), spaceId));

        return folders;

    }

    @Step("Reading folders in space with id = \"{spaceId}\" and expect error")
    public ErrorDTO getsHasError(String spaceId) {

        String url = endpoint.getFolders(spaceId);

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

        log.info(String.format("Unable to get folders. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Reading folders in space with id = \"{spaceId}\" with query params and expect error")
    public ErrorDTO getsHasError(String spaceId, HashMap<String, String> params) {

        String url = endpoint.getFolders(spaceId);

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

        log.info(String.format("Unable to get folders. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Updating folder with id = \"{folderId}\" to name = \"{folderName}\"")
    public FolderDTO update(String folderId, String folderName) {

        String url = endpoint.updateFolder(folderId);

        JSONObject requestBody = new JSONObject()
                .put("name", folderName);

        log.info("Updating folder id = " + folderId + "");

        // @formatter:off
        FolderDTO folder = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(requestBody.toString())
                .when()
                    .log().ifValidationFails()
                    .put(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().response().as(FolderDTO.class);
        // @formatter:on

        log.info("Folder with id = " + folderId + " updated");

        return folder;

    }

    @Step("Updating folder")
    public FolderDTO update(FolderDTO folder) {

        String folderId = folder.getId();
        String url = endpoint.updateFolder(folderId);

        if (folderId == null) {
            throw new IllegalArgumentException("Folder id equals null");
        }

        log.info("Updating folder id = " + folderId + "");

        // @formatter:off
        folder = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(folder)
                .when()
                    .log().ifValidationFails()
                    .put(url)
                .then()
                    .log().body()
                    .extract().response().as(FolderDTO.class);
        // @formatter:on

        log.info("Folder with id = " + folderId + " updated");

        return folder;

    }

    @Step("Updating folder with id = \"{folderId}\" to name = \"{folderName}\" and expect error")
    public ErrorDTO updateHasError(String folderId, String folderName) {
        String url = endpoint.updateFolder(folderId);

        JSONObject requestBody = new JSONObject()
                .put("name", folderName);

        log.info("Updating folder id = " + folderId + "");

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

        log.info(String.format("Unable to update folder. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Updating folder and expect error")
    public ErrorDTO updateHasError(FolderDTO folder) {
        String folderId = folder.getId();
        String url = endpoint.updateFolder(folderId);

        log.info("Updating folder id = " + folderId + "");

        // @formatter:off
        Response response = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                    .body(folder)
                .when()
                    .log().ifValidationFails()
                    .put(url);

        ErrorDTO error = response.then()
                    .spec(Specs.responseSpec())
                    .extract().response().as(ErrorDTO.class);
        // @formatter:on

        error.setStatusCode(response.then().extract().statusCode());

        log.info(String.format("Unable to update folder. Status code: '%s', message: '%s'",
                error.getStatusCode(), error.getMessage()));

        return error;

    }

    @Step("Removing folder with id ='{folderId}'")
    public void delete(String folderId) {

        String url = endpoint.deleteFolder(folderId);

        log.info("Deleting folder id = " + folderId + "");

        // @formatter:off
        RestAssured.given()
                .spec(Specs.requestSpec(token))
                .when()
                .log().ifValidationFails()
                .delete(url)
                .then()
                .spec(Specs.responseValidSpec());
        // @formatter:on

        log.info("Folder with id = " + folderId + " was deleted");

    }

    @Step("Removing folder with id ='{folderId}' and expect error")
    public ErrorDTO deleteHasError(String folderId) {

        String url = endpoint.deleteFolder(folderId);

        log.info("Deleting folder id = " + folderId + "");

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

        log.info(String.format("Folder not deleted. Status code: '%s', message: '%s'.",
                error.getStatusCode(), error.getMessage()));

        return error;

    }


}
