package com.clickup.api.space;

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
@SpaceTag
@Delete
@DisplayName("Deleting space tests")
public class DeleteSpaceTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(DeleteSpaceTests.class);

    @BeforeEach
    void beforeEach() {

        spaceName = fake.book().title() + ". " + fake.book().author();
        space = spaceModels.create(teamId, spaceName);
        spaceId = space.getId();
        spacesIDsArray.add(spaceId);

        log.info("################# - Start test " + testName + " - #################");

    }

    @Test
    @My
    @Positive
    @DisplayName("Delete my space")
    void canDeleteMySpaceTest() {
        //Step
        spaceModels.delete(spaceId);
    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Delete not my space")
    void canNotDeleteNotMySpaceTest() {

        //Step
        error = spaceModels.deleteHasError(userConfig.getNotMySpaceId());

        //Assertions
        Assertions.assertAll("",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Delete not exists space")
    void canNotDeleteNotExistsSpaceTest() {

        //Step
        error = spaceModels.deleteHasError(fake.number().digits(10));

        //Assertions
        Assertions.assertAll("",
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_027_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Delete deleted space")
    void canDeleteDeletedSpaceTest() {

        //Steps
        spaceModels.delete(space.getId());
        spaceModels.delete(space.getId());

    }

}
