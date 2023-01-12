package com.clickup.api.folder;

import com.clickup.api.helpers.StatusCode;
import com.clickup.api.dto.FolderDTO;
import com.clickup.common.helpers.CaseTag.*;
import com.clickup.api.setup.TestBaseAPI;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Owner("@arrnel")
@API
@FolderTag
@Update
@DisplayName("Updating folder tests")
public class UpdateFolderTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(UpdateFolderTests.class);

    @BeforeEach
    void beforeEach() {

        spaceName = fake.book().title() + ". " + fake.book().author();
        space = spaceModels.create(teamId, spaceName);
        spaceId = space.getId();
        spacesIDsArray.add(spaceId);

        folderName = fake.book().title() + ". " + fake.book().author();
        folder = folderModels.create(space.getId(), folderName);
        folderId = folder.getId();

        log.info("################# - Start test " + testName + " - #################");

    }

    @Test
    @My
    @Positive
    @DisplayName("Can update my folder")
    void canUpdateMyFolder() {

        String folderNameUpdated = fake.book().title() + ". " + fake.book().author();

        //Steps
        FolderDTO folderUpdated = folderModels.update(folder.getId(), folderNameUpdated); // do

        //Assertions
        Assertions.assertAll("Assert folder was updated",
                () -> Assertions.assertEquals(folderId, folderUpdated.getId()),
                () -> Assertions.assertEquals(folderNameUpdated, folderUpdated.getName())
        );

    }

    @Test
    @My
    @Positive
    @DisplayName("Can not update not my folder")
    void canNotUpdateNotMyFolder() {

        //Steps
        error = folderModels.updateHasError(userConfig.getNotMyFolderId(), folderName);

        //Assertions
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't update folder if folder name are used")
    void canNotUpdateFolderNameToExists() {

        //Data
        String folder2Name = fake.book().title() + ". " + fake.book().author();

        //Steps
        folderModels.create(spaceId, folder2Name);
        error = folderModels.updateHasError(folderId, folder2Name);
        folder = folderModels.get(folderId);

        //Assertions
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.CAT_018_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.CAT_018_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode()),
                () -> Assertions.assertEquals(folderName, folder.getName())  //after update
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't update folder name if folder name are long")
    void canNotUpdateWithLongFolderName() {

        //Data
        String folderNameLong = fake.lorem().fixedString(productConfig.folderNameMaxLength() + 1);

        //Steps
        error = folderModels.updateHasError(folderId, folderNameLong);
        folder = folderModels.get(folderId);

        //Assertions
        Assertions.assertAll("Assert returns error and list name not updated",
                () -> Assertions.assertEquals(errorConfig.CAT_022_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.CAT_022_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode()),
                () -> Assertions.assertEquals(folderName, folder.getName())
        );

    }

}
