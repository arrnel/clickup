package com.clickup.api.list;

import com.clickup.api.helpers.StatusCode;
import com.clickup.api.dto.ListDTO;
import com.clickup.common.helpers.CaseTag.*;
import com.clickup.api.setup.TestBaseAPI;
import io.qameta.allure.Flaky;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Owner("@arrnel")
@API
@ListTag
@Update
@DisplayName("Updating list tests")
public class UpdateListTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(UpdateListTests.class);

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
    @DisplayName("Can update my list")
    void canUpdateMyList() {

        //Steps
        String listNameUpdated = fake.book().title() + ". " + fake.book().author();

        ListDTO listUpdated = listModels.update(listId, listNameUpdated); // do

        //Assertions
        Assertions.assertAll("Assert folder was updated",
                () -> Assertions.assertEquals(listId, listUpdated.getId()),
                () -> Assertions.assertEquals(listNameUpdated, listUpdated.getName())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can not update not my list")
    void canNotUpdateNotMyList() {

        //Steps
        error = listModels.updateHasError(userConfig.getNotMyListId(), listName);

        //Assertions
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Flaky
    @Test
    @My
    @Negative
    @DisplayName("Can't update list if list name are used")
    void canNotUpdateListNameToExists() {

        //Data
        String list2Name = fake.book().title();

        //Steps
        listModels.create(folderId, list2Name);
        error = listModels.updateHasError(listId, list2Name);
        list = listModels.get(listId);

        //Assertions
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.SUBCAT_019_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.SUBCAT_019_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode()),
                () -> Assertions.assertEquals(folderName, folder.getName())  //after update
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't update list name if list name are long")
    void canNotUpdateWithLongFolderName() {

        //Data
        String listNameLong = fake.lorem().fixedString(productConfig.listNameMaxLength() + 1);

        //Steps
        error = listModels.updateHasError(listId, listNameLong);
        list = listModels.get(listId);

        //Assertions
        Assertions.assertAll("Assert returns error and list name not updated",
                () -> Assertions.assertEquals(errorConfig.SUBCAT_021_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.SUBCAT_021_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode()),
                () -> Assertions.assertEquals(listName, list.getName())
        );

    }

}
