package com.clickup.api.folder;

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
@FolderTag
@Create
@DisplayName("Creating folder tests")
public class CreateFolderTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(CreateFolderTests.class);

    @BeforeEach
    void beforeEach() {

        spaceName = fake.book().title() + ". " + fake.book().author();
        space = spaceModels.create(teamId, spaceName);
        spaceId = space.getId();
        spacesIDsArray.add(spaceId);

        folderName = fake.book().title() + ". " + fake.book().author();

        log.info("################# - Start test " + testName + " - #################");

    }

    @Test
    @My
    @Positive
    @DisplayName("Can create folder")
    void canCreateFolderTest() {

        //Steps
        folder = folderModels.create(spaceId, folderName);

        //Assertion
        Assertions.assertAll(String.format("Check folder was created with folder name = '%s'", folderName),
                () -> Assertions.assertNotNull(folder.getId()),
                () -> Assertions.assertEquals(folderName, folder.getName())
        );

    }


    @Test
    @My
    @Negative
    @DisplayName("Can't create folder with long name")
    void canNotCreateFolderWithLongNameTest() {

        String folderName = fake.lorem().fixedString(productConfig.folderNameMaxLength() + 1);

        //Steps
        error = folderModels.createHasError(spaceId, folderName);

        //Assertion
        Assertions.assertAll("Assert folder not created",
                () -> Assertions.assertEquals(errorConfig.CAT_021_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.CAT_021_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't create folder with exists space name")
    void canNotCreateFolderWithExistsFolderNameTest() {

        //Steps
        folder = folderModels.create(spaceId, folderName);
        error = folderModels.createHasError(spaceId, folderName);

        //Assertion
        Assertions.assertAll("Assert folder not created",
                () -> Assertions.assertEquals(errorConfig.CAT_014_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.CAT_014_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't create folder with empty name")
    void canNotCreateFolderWithEmptyNameTest() {

        //Steps
        error = folderModels.createHasError(space.getId(), "");

        //Assertion
        Assertions.assertAll("Assert folder not created",
                () -> Assertions.assertEquals(errorConfig.CAT_021_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.CAT_021_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't create folder in other team")
    void canNotCreateFolderInOtherTeamTest() {

        //Steps
        error = spaceModels.createHasError(userConfig.getNotMySpaceId(), folderName);

        //Assertion
        Assertions.assertAll("Assert folder not created",
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't create folder in not exists space")
    void canNotCreateFolderInNotExistsSpaceTest() {

        //Steps
        error = spaceModels.createHasError(fake.number().digits(12), folderName);

        //Assertion
        Assertions.assertAll("Assert folder not created",
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @My
    @Positive
    @ParameterizedTest(name = "Can create folder with length of folder name = {0}")
    @DisplayName("Can create folder with correct length of folderName")
    @MethodSource("correctNameProvider")
    void canCreateFolderWithCorrectLengthOfSpaceName(int val, String folderName) {

        folder = folderModels.create(spaceId, folderName);

        //Assertion
        Assertions.assertAll(String.format("Check folder was created with folderName = '%s'", spaceName),
                () -> Assertions.assertNotNull(folder.getId()),
                () -> Assertions.assertEquals(folderName, folder.getName())
        );

        //Data for postconditions
        spacesIDsArray.add(space.getId());

    }

    @My
    @Negative
    @ParameterizedTest(name = "Can not create folder with length of folder name = {0}")
    @MethodSource("notCorrectNameProvider")
    @DisplayName("Can not create folder with not correct length of folder name")
    void canNotCreateFolderWithNotCorrectLengthOfSpaceName(int val, String folderName) {

        error = folderModels.createHasError(spaceId, folderName);

        //Assertion
        Assertions.assertAll("Assert space not created with existed space name",
                () -> Assertions.assertEquals(errorConfig.CAT_021_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.CAT_021_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

        //Data for postconditions
        spacesIDsArray.add(space.getId());

    }

    static Stream<Arguments> correctNameProvider() {

        ProductConfiguration productConfig = ConfigFactory.create(ProductConfiguration.class);
        Faker fake = new Faker(new Locale("en"));

        int min = productConfig.folderNameMinLength(),
                max = productConfig.folderNameMaxLength();

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

        int max = productConfig.folderNameMaxLength();

        return Stream.of(
                Arguments.of(0, ""),
                Arguments.of(max + 1, fake.lorem().characters(max + 1)),
                Arguments.of(max + 10, fake.lorem().characters(max + 10))
        );

    }

}
