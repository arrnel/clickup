package com.clickup.common.configs;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.Sources;

@Sources("classpath:configs/test.properties")
public interface TestConfiguration extends Config {

    @Key("REMOTE_URL")
    String REMOTE_URL();

    @Key("BASE_URL_API")
    String BASE_URL_API();

    @Key("BASE_URL_UI")
    String BASE_URL_UI();

    @Key("BASE_URL_MOBILE")
    String BASE_URL_MOBILE();


}
