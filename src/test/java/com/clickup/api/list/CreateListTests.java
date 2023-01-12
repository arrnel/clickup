package com.clickup.api.list;

import com.clickup.api.dto.ListDTO;
import com.clickup.api.helpers.StatusCode;
import com.clickup.common.configs.ProductConfiguration;
import com.clickup.common.helpers.CaseTag.*;
import com.clickup.api.setup.TestBaseAPI;
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
@ListTag
@Create
@DisplayName("Creating list tests")
public class CreateListTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(CreateListTests.class);

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

        log.info("################# - Start test " + testName + " - #################");

    }

    @Test
    @My
    @Positive
    @DisplayName("Can create list")
    void createListTest() {

        //Steps
        list = listModels.create(folderId, listName);

        //Assertion
        Assertions.assertAll(String.format("Check list was created with list name = '%s'", listName),
                () -> Assertions.assertNotNull(list.getId()),
                () -> Assertions.assertEquals(listName, list.getName())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't create list with long name")
    void canNotCreateListWithLongNameTest() {


        //Data
        String listName = fake.lorem().fixedString(productConfig.listNameMaxLength() + 1);

        //Steps
        error = listModels.createHasError(folderId, listName);

        //Assertion
        Assertions.assertAll("Assert list not created",
                () -> Assertions.assertEquals(errorConfig.SUBCAT_020_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.SUBCAT_020_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't create list with exists space name")
    void canNotCreateListWithExistsListNameTest() {

        //Steps
        list = listModels.create(folderId, listName);
        error = listModels.createHasError(folderId, listName);

        //Assertion
        Assertions.assertAll("Assert list not created",
                () -> Assertions.assertEquals(errorConfig.SUBCAT_016_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.SUBCAT_016_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    @Test
    @My
    @Negative
    @DisplayName("Can't create list with empty name")
    void canNotCreateListWithEmptyNameTest() {

        //Steps
        error = listModels.createHasError(folderId, "");

        //Assertion
        Assertions.assertAll("Assert list not created",
                () -> Assertions.assertEquals(errorConfig.SUBCAT_020_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.SUBCAT_020_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't create list in other team")
    void canNotCreateListInOtherTeamTest() {

        //Steps
        error = listModels.createHasError(userConfig.getNotMyFolderId(), listName);

        //Assertion
        Assertions.assertAll("Assert list not created",
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_023_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @Test
    @NotMy
    @Negative
    @DisplayName("Can't create list in not exists space")
    void canNotCreateListInNotExistsSpaceTest() {

        //Steps
        error = spaceModels.createHasError(fake.number().digits(12), folderName);

        //Assertion
        Assertions.assertAll("Assert list not created",
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.OAUTH_057_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.UNAUTHORIZED, error.getStatusCode())
        );

    }

    @My
    @Positive
    @ParameterizedTest(name = "Can create list with length of list name = {0}")
    @DisplayName("Can create list with correct length of listName")
    @MethodSource("correctNameProvider")
    void canCreateFolderWithCorrectLengthOfSpaceName(int val, String listName) {

        list = listModels.create(folderId, listName);

        //Assertion
        Assertions.assertAll(String.format("Check list was created with listName = '%s'", spaceName),
                () -> Assertions.assertNotNull(list.getId()),
                () -> Assertions.assertEquals(listName, list.getName())
        );

        //Data for postconditions
        spacesIDsArray.add(space.getId());

    }

    @My
    @Negative
    @ParameterizedTest(name = "Can not create list with length of list name = {0}")
    @MethodSource("notCorrectNameProvider")
    @DisplayName("Can not create list with not correct length of list name")
    void canNotCreateListWithNotCorrectLengthOfSpaceName(int val, String listName) {

        error = listModels.createHasError(folderId, listName);

        //Assertion
        Assertions.assertAll("Assert space not created with existed space name",
                () -> Assertions.assertEquals(errorConfig.SUBCAT_020_MSG(), error.getMessage()),
                () -> Assertions.assertEquals(errorConfig.SUBCAT_020_CODE(), error.getCode()),
                () -> Assertions.assertEquals(StatusCode.BAD_REQUEST, error.getStatusCode())
        );

        //Data for postconditions
        spacesIDsArray.add(space.getId());

    }

    static Stream<Arguments> correctNameProvider() {

        ProductConfiguration productConfig = ConfigFactory.create(ProductConfiguration.class);
        Faker fake = new Faker(new Locale("en"));

        int min = productConfig.listNameMinLength(),
                max = productConfig.listNameMaxLength();

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

        int max = productConfig.listNameMaxLength();

        return Stream.of(
                Arguments.of(0, ""),
                Arguments.of(max + 1, fake.lorem().characters(max + 1)),
                Arguments.of(max + 10, fake.lorem().characters(max + 10))
        );

    }
}
