package com.clickup.api.space;

import com.clickup.api.dto.SpaceDTO;
import com.clickup.api.helpers.Color;
import com.clickup.api.helpers.StatusCode;
import com.clickup.api.setup.TestBaseAPI;
import com.clickup.common.configs.ProductConfiguration;
import com.clickup.common.helpers.CaseTag.*;
import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.stream.Stream;

@Owner("@arrnel")
@API
@SpaceTag
@Create
@DisplayName("Space creation tests")
public class CreateSpaceTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(CreateSpaceTests.class);

    @BeforeEach
    public void beforeEach() {
        log.info("################# - Start test " + testName + " - #################");
    }

    @Test
    @My
    @Positive
    @DisplayName("Create space in my team")
    void canCreateSpaceTest() {

        //Data
        spaceName = fake.book().title() + ". " + fake.book().author();

        //Steps
        space = spaceModels.create(teamId, spaceName);

        //Assertion
        Assertions.assertAll(String.format("Check space was created with spaceName = '%s'", spaceName),
                () -> Assertions.assertNotNull(space.getId()),
                () -> Assertions.assertEquals(spaceName, space.getName())
        );

        //Data for postconditions
        spacesIDsArray.add(space.getId());

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't create space with long name")
    void canNotCreateSpaceWithLongNameTest() {

        //Data
        spaceName = fake.lorem().fixedString(productConfig.spaceNameMaxLength() + 1);

        //Steps

        error = spaceModels.createHasError(teamId, spaceName);

        //Assertion
        Assertions.assertAll(String.format("Check space wasn't created with spaceName = \"%s\"", spaceName),
                () -> Assertions.assertEquals(errorConfig.PROJ_060_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.PROJ_060_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't create space with exists space name")
    void canNotCreateSpaceWithExistsSpaceNameTest() {

        //Data
        spaceName = fake.book().title() + ". " + fake.book().author();

        //Steps
        space = spaceModels.create(teamId, spaceName);
        error = spaceModels.createHasError(teamId, spaceName);

        //Assertion
        Assertions.assertAll("Assert space not created with existed space name",
                () -> Assertions.assertEquals(errorConfig.PROJ_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.PROJ_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

        spacesIDsArray.add(space.getId());

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't create space in other team")
    void canNotCreateSpaceInOtherTeamTest() {

        //Data
        spaceName = fake.book().title() + ". " + fake.book().author();

        //Steps
        error = spaceModels.createHasError(userConfig.getNotMyTeamId(), spaceName);

        //Assertion
        Assertions.assertAll("Assert space not created with existed space name",
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't create space in not exists team")
    void canNotCreateSpaceInNotExistsTeamTest() {

        //Data
        spaceName = fake.book().title() + ". " + fake.book().author();
        String teamId = 9 + fake.number().digits(7); //can't be field of class or @AfterEach will crash

        //Steps
        error = spaceModels.createHasError(teamId, spaceName);

        //Assertion
        Assertions.assertAll("Assert space not created with existed space name",
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @My
    @Positive
    @DisplayName("Color of space icon not changing when creating space")
    void canNotCreateSpaceWithNotDefaultColorTest() {

        //Data
        spaceName = fake.book().title() + ". " + fake.book().author();
        String spaceColor = Color.BLUE;

        space = new SpaceDTO()
                .setTeamId(teamId)
                .setName(spaceName)
                .setColor(spaceColor);

        //Steps
        space = spaceModels.create(space);
        space = spaceModels.get(space.getId());

        //Assertions
        Assertions.assertNull(space.getColor());

        spacesIDsArray.add(space.getId());

    }

    @Test
    @My
    @Positive
    @DisplayName("Can not change space private status when creating space")
    void canNotCreatePrivateSpaceTest() {

        //Data
        spaceName = fake.book().title() + ". " + fake.book().author();
        boolean isPrivate = true;

        space = new SpaceDTO()
                .setTeamId(teamId)
                .setName(spaceName)
                .isPrivate(isPrivate);

        //Steps
        space = spaceModels.create(space);
        space = spaceModels.get(space.getId());

        //Assertions
        Assertions.assertFalse(space.isPrivate());

        spacesIDsArray.add(space.getId());

    }

    @My
    @Positive
    @ParameterizedTest(name = "Can create space with length of space name = {0}")
    @DisplayName("Can create space with correct length of spaceName")
    @MethodSource("correctNameProvider")
    void canCreateSpaceWithCorrectLengthOfSpaceName(int val, String spaceName) {

        space = spaceModels.create(teamId, spaceName);

        //Assertion
        Assertions.assertAll(String.format("Check space was created with spaceName = '%s'", spaceName),
                () -> Assertions.assertNotNull(space.getId()),
                () -> Assertions.assertEquals(spaceName, space.getName())
        );

        //Data for postconditions
        spacesIDsArray.add(space.getId());

    }

    @My
    @Negative
    @ParameterizedTest(name = "Can not create space with length of space name = {0}")
    @MethodSource("notCorrectNameProvider")
    @DisplayName("Can not create space with not correct length of space name")
    void canNotCreateSpaceWithNotCorrectLengthOfSpaceName(int val, String spaceName) {

        error = spaceModels.createHasError(teamId, spaceName);

        //Assertion
        Assertions.assertAll("Assert space not created with existed space name",
                () -> Assertions.assertEquals(errorConfig.PROJ_060_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.PROJ_060_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    static Stream<Arguments> correctNameProvider() {

        ProductConfiguration productConfig = ConfigFactory.create(ProductConfiguration.class);
        Faker fake = new Faker(new Locale("en"));

        int min = productConfig.spaceNameMinLength(),
                max = productConfig.spaceNameMaxLength();

        return Stream.of(
                Arguments.of(min, fake.lorem().characters(min)),
                Arguments.of(min + 1, fake.lorem().characters(min + 1)),
                Arguments.of(max - 1, fake.lorem().characters(max - 1)),
                Arguments.of(max, fake.lorem().characters(max))
        );

    }

    static Stream<Arguments> notCorrectNameProvider() {

        ProductConfiguration productConfig = ConfigFactory.create(ProductConfiguration.class);
        Faker fake = new Faker(new Locale("en"));

        int max = productConfig.spaceNameMaxLength();

        return Stream.of(
                Arguments.of(0, ""),
                Arguments.of(max + 1, fake.lorem().characters(max + 1)),
                Arguments.of(max + 10, fake.lorem().characters(max + 10))
        );

    }

//    @AfterEach
//    public void afterEach() {
//        log.info("################# - Nested postconditions of test " + testName + " - #################");
//        new SpacesRemover().deleteSpaces(getToken(), spacesIDsArray);
//    }

}
