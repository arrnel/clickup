package com.clickup.api.setup;

import com.clickup.api.dto.*;
import com.clickup.api.helpers.Endpoint;
import com.clickup.api.models.FolderModels;
import com.clickup.api.models.ListModels;
import com.clickup.api.models.SpaceModels;
import com.clickup.api.models.TeamModels;
import com.clickup.common.configs.ErrorConfiguration;
import com.clickup.common.configs.ProductConfiguration;
import com.clickup.common.configs.UserConfiguration;
import com.clickup.common.helpers.SpacesRemover;
import com.clickup.common.object.User;
import com.clickup.common.object.UsersList;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TestBaseAPI {

    private Logger log = LoggerFactory.getLogger(TestBaseAPI.class);

    protected TeamDTO team;
    protected SpaceDTO space;
    protected SpacesDTO spaces;
    protected FolderDTO folder;
    protected FoldersDTO folders;
    protected ListDTO list;
    protected ListsDTO lists;
    protected ErrorDTO error;

    protected TeamModels teamModels;
    protected SpaceModels spaceModels;
    protected FolderModels folderModels;
    protected ListModels listModels;

    protected final UserConfiguration userConfig = ConfigFactory.create(UserConfiguration.class);
    protected final ErrorConfiguration errorConfig = ConfigFactory.create(ErrorConfiguration.class);
    protected final ProductConfiguration productConfig = ConfigFactory.create(ProductConfiguration.class);

    protected Faker fake = new Faker(Locale.ENGLISH);
    protected final Endpoint endpoint = new Endpoint();


    protected String testName;

    private List<String> allUsers = new ArrayList<>();
    protected ArrayList<String> spacesIDsArray = new ArrayList<>();
    protected String teamId,
            spaceId,
            folderId,
            listId,
            spaceName,
            folderName,
            listName;

    private UsersList usersList;
    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    @BeforeAll
    @ResourceLock(value = "resources")
    static void beforeAll() {

        LoggerFactory.getLogger(TestBaseAPI.class).info("############### - PRECONDITIONS BEFORE ALL - ###############");

        RestAssured.baseURI = "https://api.clickup.com/";
        RestAssured.basePath = "api/v2/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        new SpacesRemover().deleteAllSpacesFromAllTeams();

    }

    @BeforeEach
    void beforeEach(TestInfo testInfo) {

        log.info("########### - Preconditions Common Before Each - ###########");

        testName = testInfo.getTestMethod().orElseThrow().getName();

        usersList = new UsersList();
        user = usersList.getRandomUser();
        token = user.getToken();

        log.info(usersList.toString());

        teamModels = new TeamModels(token);
        spaceModels = new SpaceModels(token);
        folderModels = new FolderModels(token);
        listModels = new ListModels(token);

        teamId = teamModels.get().getTeams().get(0).getId();

    }

    @AfterEach
    void afterEach() {
        log.info("########### - End of test " + testName + " - ###########");
        log.info("########### - Postconditions Common After Each - ###########");

        new SpacesRemover().deleteSpaces(token, spacesIDsArray);
    }


    @AfterAll
    static void afterAll() {
        LoggerFactory.getLogger(TestBaseAPI.class).info("############### - POSTCONDITIONS AFTER ALL - ###############");
        new SpacesRemover().deleteAllSpacesFromAllTeams();
    }

}
