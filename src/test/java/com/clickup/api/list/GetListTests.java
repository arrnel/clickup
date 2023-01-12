package com.clickup.api.list;

import com.clickup.api.helpers.StatusCode;
import com.clickup.common.helpers.CaseTag.*;
import com.clickup.api.setup.TestBaseAPI;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Owner("@arrnel")
@Epic("API")
@Feature("List")
@Story("Get")
@DisplayName("Getting list tests")
public class GetListTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(GetListTests.class);

    @BeforeEach
    void beforeEachTests() {

        spaceName = fake.book().title() + ". " + fake.book().author();
        space = spaceModels.create(teamId, spaceName);
        spaceId = space.getId();
        spacesIDsArray.add(space.getId());

        folderName = fake.book().title() + ". " + fake.book().author();
        folder = folderModels.create(space.getId(), folderName);
        folderId = folder.getId();

        listName = fake.book().title() + ". " + fake.book().author();
        list = listModels.create(folderId, listName);
        listId = list.getId();

        log.info("################# - Start test " + testName + " - #################");

    }

    @Test
    @My
    @Positive
    @DisplayName("Can get my lists")
    void canGetMyLists() {

        //Steps
        lists = listModels.gets(folderId);

        //Assertion
        Assertions.assertNotNull(lists.getLists().get(0).getId());

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't get not my lists")
    void canNotGetNotMyLists() {

        //Steps
        error = listModels.getsHasError(userConfig.getNotMyFolderId());

        //Assertion
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Positive
    @DisplayName("Can get my list")
    void getSpace() {

        //Steps
        list = listModels.get(listId);

        //Assertion
        Assertions.assertEquals(listId, list.getId());

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't get not my list")
    void canNotGetNotMyList() {

        //Steps
        error = listModels.getHasError(userConfig.getNotMyListId());

        //Assertion
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

}
