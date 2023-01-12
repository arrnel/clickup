package com.clickup.common.helpers;

import com.clickup.api.dto.SpaceDTO;
import com.clickup.api.dto.SpacesDTO;
import com.clickup.api.helpers.Endpoint;
import com.clickup.api.helpers.Specs;
import com.clickup.common.object.User;
import com.clickup.common.object.UsersList;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class SpacesRemover {

    Logger log = LoggerFactory.getLogger(SpacesRemover.class);

    public void deleteAllSpacesFromAllTeams() {

        RestAssured.baseURI = "https://api.clickup.com/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.basePath = "api/v2/";

        List<User> allUsers = new UsersList().getAllUsers();
        log.info("All users:" + allUsers.toString());

        for (User user: allUsers) {

            String token = user.getToken();
            log.info("Current token: " + token);

            String teamId = getTeamId(token);
            log.info("Current team_id: " + teamId);

            List<SpaceDTO> spacesList = getSpaces(token, teamId);
            log.info("Spaces in team: " + spacesList.size());

            removeSpaceFromList(token, spacesList);


        }
    }

    void removeSpaceFromList(String token, List<SpaceDTO> spacesList) {
        if (spacesList.size() > 0) {
            String spaceId;
            for (SpaceDTO space : spacesList) {
                spaceId = space.getId();
                log.info("Removing space with id: " + spaceId);
                deleteSpace(token, spaceId);
            }
        }
    }

    String getTeamId(String token) {

        String url = new Endpoint().getTeam();

        // @formatter:off
        return RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
                    .spec(Specs.responseValidSpec())
                    .extract().jsonPath().getList("teams.id").get(0).toString();
        // @formatter:on

    }

    List<String> getSpacesIds(String token, String teamId) {

        String url = new Endpoint().getSpaces(teamId);

        // @formatter:off
        return RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
//                    .log().all()
                    .spec(Specs.responseValidSpec())
                    .extract().jsonPath().getList("spaces.id");
        // @formatter:on

    }

    List<SpaceDTO> getSpaces(String token, String teamId) {

        String url = new Endpoint().getSpaces(teamId);

        // @formatter:off
        SpacesDTO spaces = RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .get(url)
                .then()
//                    .log().all()
                    .spec(Specs.responseValidSpec())
                    .extract().as(SpacesDTO.class);
        // @formatter:on

        return spaces.getSpaces();
    }

    void deleteSpace(String token, String spaceId) {

        log.info("Removing space with id: " + spaceId);
        String url = new Endpoint().deleteSpace(spaceId);

        // @formatter:off
        RestAssured.given()
                    .spec(Specs.requestSpec(token))
                .when()
                    .log().ifValidationFails()
                    .delete(url)
                .then()
                    .spec(Specs.responseValidSpec());
        // @formatter:on

        log.info("Space with id " + spaceId + " removed");

    }

    public void deleteSpaces(@Nonnull String token, @Nonnull List<String> spaceIds) {
        for (String spaceId : spaceIds)
            deleteSpace(token, spaceId);
    }

}
