package com.clickup.api.folder;

import com.clickup.api.helpers.StatusCode;
import com.clickup.api.setup.TestBaseAPI;
import com.clickup.common.helpers.CaseTag.*;
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
@Delete
@DisplayName("Deleting folder tests")
public class DeleteFolderTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(DeleteFolderTests.class);

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
    @DisplayName("Delete my folder")
    void canDeleteMyFolderTest() {

        //Step
        folderModels.delete(folderId);

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Delete not my folder")
    void canNotDeleteNotMyFolderTest() {

        //Step
        error = folderModels.deleteHasError(userConfig.getNotMySpaceId());

        //Assertions
        Assertions.assertAll("Assert folder not deleted",
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_MSG(), error.getMessage() ),
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Delete not exists folder")
    void canNotDeleteNotExistsFolderTest() {

        //Step
        error = folderModels.deleteHasError(fake.number().digits(12));

        //Assertions
        Assertions.assertAll("Assert folder not deleted",
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Delete deleted folder")
    void canDeleteDeletedFolderTest() {

        //Steps
        folderModels.delete(folderId);
        folderModels.delete(folderId);

    }

}
