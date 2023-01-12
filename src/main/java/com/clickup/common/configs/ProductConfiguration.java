package com.clickup.common.configs;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configs/error_messages.properties")
public interface ProductConfiguration extends Config {

    @Key("SPACE_LIMIT")
    @DefaultValue("5")
    int spaceLimit();

    @Key("SPACE_NAME_MIN_LENGTH")
    @DefaultValue("1")
    int spaceNameMinLength();

    @Key("SPACE_NAME_MAX_LENGTH")
    @DefaultValue("2048")
    int spaceNameMaxLength();

    @Key("FOLDER_NAME_MIN_LENGTH")
    @DefaultValue("1")
    int folderNameMinLength();

    @Key("FOLDER_NAME_MAX_LENGTH")
    @DefaultValue("2048")
    int folderNameMaxLength();

    @Key("LIST_NAME_MIN_LENGTH")
    @DefaultValue("1")
    int listNameMinLength();

    @Key("LIST_NAME_MAX_LENGTH")
    @DefaultValue("2048")
    int listNameMaxLength();

}
