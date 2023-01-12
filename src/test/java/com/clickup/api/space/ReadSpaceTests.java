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
@Get
@DisplayName("Getting space tests")
public class ReadSpaceTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(ReadSpaceTests.class);

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
    @DisplayName("Can get my spaces")
    void canGetMySpaces() {

        //Steps
        spaces = spaceModels.gets(teamId);

        //Assertion
        Assertions.assertNotNull(spaces.getSpaces().get(0).getId());

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't get not my spaces")
    void canNotGetNotMySpaces() {

        //Steps
        error = spaceModels.getsHasError(userConfig.getNotMyTeamId());

        //Assertion
        Assertions.assertAll("Assert returns error",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MODIFIED_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MODIFIED_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Positive
    @DisplayName("Can get my space")
    void getSpace() {

        //Steps
        space = spaceModels.get(spaceId);

        //Assertion
        Assertions.assertEquals(spaceId, space.getId());

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't get not my space")
    void getNotMySpace() {

        //Steps
        error = spaceModels.getsHasError(userConfig.getNotMySpaceId());

        //Assertion
        Assertions.assertAll("",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MODIFIED_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MODIFIED_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

}
