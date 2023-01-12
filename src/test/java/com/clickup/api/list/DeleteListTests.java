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
@Story("Delete")
@DisplayName("Deleting list tests")
public class DeleteListTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(DeleteListTests.class);

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
    @DisplayName("Delete my list")
    void canDeleteMyListTest() {

        //Step
        listModels.delete(listId);

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Delete not my list")
    void canNotDeleteNotMyListTest() {

        //Step
        error = folderModels.deleteHasError(userConfig.getNotMySpaceId());

        //Assertions
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Delete not exists list")
    void canNotDeleteNotExistsListTest() {

        //Step
        error = listModels.deleteHasError(fake.number().digits(12));

        //Assertions
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Delete deleted list")
    void canDeleteDeletedSpaceTest() {

        //Steps
        folderModels.delete(folderId);
        folderModels.delete(folderId);

    }

}
