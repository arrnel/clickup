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
@Get
@DisplayName("Getting folder tests")
public class GetFolderTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(GetFolderTests.class);

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
    @DisplayName("Can get my folders")
    void canGetMyFolders() {

        //Steps
        folders = folderModels.gets(spaceId);

        //Assertion
        Assertions.assertTrue(folders.getFolders().size() > 0);

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't get not my folders")
    void canNotGetNotMyFolders() {

        //Steps
        error = folderModels.getsHasError(userConfig.getNotMySpaceId());

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
    @DisplayName("Can get my folder")
    void canGetMyFolder() {

        //Steps
        folder = folderModels.get(folderId);

        //Assertion
        Assertions.assertEquals(folderId, folder.getId());

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't get not my folder")
    void canNotGetNotMyFolder() {

        //Steps
        error = folderModels.getHasError(userConfig.getNotMyFolderId());

        //Assertion
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

}
