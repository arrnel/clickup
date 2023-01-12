package com.clickup.api.space;

import com.clickup.api.helpers.Color;
import com.clickup.api.helpers.StatusCode;
import com.clickup.api.dto.ErrorDTO;
import com.clickup.api.dto.SpaceDTO;
import com.clickup.api.setup.TestBaseAPI;
import com.clickup.common.helpers.CaseTag.*;
import io.qameta.allure.Flaky;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Owner("@arrnel")
@API
@SpaceTag
@Update
@DisplayName("Updating space tests")
public class UpdateSpaceTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(UpdateSpaceTests.class);

    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {

        testName = testInfo.getTestMethod().orElseThrow().getName();
        log.info("################# - Preconditions of test " + testName + " - #################");

        space = spaceModels.create(teamId, fake.book().title());
        spaceId = space.getId();

        spacesIDsArray.add(spaceId);

        log.info("################# - Starting test " + testName + " - #################");

    }

    @Test
    @My
    @Positive
    @DisplayName("Can update my space")
    void canUpdateMySpace() {

        //Data
        spaceName = fake.book().title();

        //Steps
        space = spaceModels.update(space.getId(), spaceName);

        //Assertions
        Assertions.assertAll(String.format("Assert space was created with id = \"%s\"", spaceName),
                () -> Assertions.assertEquals(spaceId, space.getId()),
                () -> Assertions.assertEquals(spaceName, space.getName())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Update not my space")
    void canNotUpdateNotMySpace() {

        //Data
        String notMySpaceId = userConfig.getNotMySpaceId(),
                spaceName = fake.funnyName().name();
        //Step
        ErrorDTO error = spaceModels.updateHasError(notMySpaceId, spaceName);

        //Assertions
        Assertions.assertAll("",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't update space name if space name are used")
    void canNotUpdateSpaceNameToExists() {

        //Data
        spaceName = fake.funnyName().name();

        //Steps
        SpaceDTO space2 = spaceModels.create(teamId, spaceName);
        ErrorDTO error = spaceModels.updateHasError(space2.getId(), space.getName());

        //Assertions
        Assertions.assertAll("",
                () -> Assertions.assertEquals(errorConfig.PROJ_024_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.PROJ_024_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode()));

        spacesIDsArray.add(space2.getId());

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't update space name if space name are long")
    void canNotUpdateWithLongSpaceName() {

        //Data
        String spaceName = fake.lorem().fixedString(productConfig.spaceNameMaxLength() + 1);

        //Steps
        ErrorDTO error = spaceModels.updateHasError(space.getId(), spaceName);

        //Assertions
        Assertions.assertAll("",
                () -> Assertions.assertEquals(errorConfig.PROJ_061_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.PROJ_061_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode()));

    }

    @Flaky
    @Test
    @My
    @Positive
    @DisplayName("Can update from public to private space")
    void canUpdateToFromPublicToPrivateSpaceTest() {

        //Data
        space = new SpaceDTO()
                .setId(spaceId)
                .isPrivate(true);

        //Steps
        space = spaceModels.update(space);

        //Assertions
        Assertions.assertTrue(space.isPrivate());

    }

    @Test
    @My
    @Positive
    @DisplayName("Can update from private to public space")
    void canUpdateToFromPrivateToPublicSpaceTest() {

        //Data
        space = space.setId(spaceId).isPrivate(true);
        SpaceDTO spaceUpdated = new SpaceDTO().setId(spaceId).isPrivate(false);

        //Steps
        spaceModels.update(space);

        spaceModels.update(spaceUpdated);
        spaceUpdated = spaceModels.get(spaceId);

        //Assertions
        Assertions.assertFalse(spaceUpdated.isPrivate());

    }

    @Test
    @My
    @Positive
    @DisplayName("Can update color from default to random")
    void canUpdateSpaceColorTest() {

        //Data
        String color = Color.getRandomColor();
        space = new SpaceDTO().setId(spaceId).setColor(color);

        //Steps
        spaceModels.update(space);
        space = spaceModels.get(spaceId);

        //Assertions
        Assertions.assertEquals(color, space.getColor());

    }

    @Test
    @My
    @Positive
    @DisplayName("Can update color twice")
    void canUpdateSpaceColorTwiceTest() {

        //Data
        String color = Color.getRandomColor();

        ArrayList<String> excludedColors = new ArrayList<>();
        excludedColors.add(Color.DEFAULT);
        excludedColors.add(color);

        String updatedColor = Color.getRandomColorWithoutExcluded(excludedColors);

        space = space.setColor(color);
        SpaceDTO updatedSpace = new SpaceDTO().setId(spaceId).setColor(updatedColor);

        //Steps
        spaceModels.update(space);
        spaceModels.update(updatedSpace);
        updatedSpace = spaceModels.get(spaceId);

        //Assertions
        Assertions.assertEquals(updatedColor, updatedSpace.getColor());

    }

    @Disabled("Not working in prod")
    @Test
    @My
    @Positive
    @DisplayName("Can update from active to archive space")
    void canMoveSpaceFromActiveToArchive() {

        //Data
        space = new SpaceDTO().setId(spaceId).isArchived(true);

        //Steps
        spaceModels.update(space);
        space = spaceModels.get(spaceId);

        //Assertions
        Assertions.assertTrue(space.isArchived());

    }

    @Test
    @My
    @Positive
    @DisplayName("Can update from archive to active space")
    void canMoveSpaceFromArchiveToActive() {

        //Data
        space = new SpaceDTO().setId(spaceId).isArchived(true);
        SpaceDTO updatedSpace = new SpaceDTO().setId(spaceId).isArchived(false);

        //Steps
        spaceModels.update(space);
        updatedSpace = spaceModels.update(updatedSpace);
        space = spaceModels.get(spaceId);

        //Assertions
        Assertions.assertFalse(updatedSpace.isArchived());

    }

}
