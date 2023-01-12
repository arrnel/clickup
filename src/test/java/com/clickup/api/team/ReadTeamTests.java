package com.clickup.api.team;

import com.clickup.api.setup.TestBaseAPI;
import com.clickup.api.dto.TeamDTO;
import com.clickup.api.dto.TeamsDTO;
import com.clickup.common.helpers.CaseTag.*;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@API
@TeamTag
@Get
@Owner("@arrnel")
@DisplayName("Read team")
public class ReadTeamTests extends TestBaseAPI {

    Logger log = LoggerFactory.getLogger(ReadTeamTests.class);

    @BeforeEach
    void setup(){

    }

    @Test
    @My
    @Positive
    @DisplayName("Can read my team")
    void canReadMyTeam(){
        log.info("################# - Start test " + testName + " - #################");

        //Steps
        TeamsDTO teams = teamModels.get();
        TeamDTO team = teams.getTeams().get(0);

        //Assertions
        Assertions.assertNotNull(team.getId());
    }

    @AfterEach
    void tearDown(){

    }

}
