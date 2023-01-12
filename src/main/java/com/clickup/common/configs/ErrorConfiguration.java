package com.clickup.common.configs;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.Sources;

@Sources("classpath:configs/error_messages.properties")
public interface ErrorConfiguration extends Config {

    @Key("CAT_014_MSG")
    String CAT_014_MSG();

    @Key("CAT_014_CODE")
    String CAT_014_CODE();

    @Key("CAT_018_MSG")
    String CAT_018_MSG();

    @Key("CAT_018_CODE")
    String CAT_018_CODE();

    @Key("CAT_021_MSG")
    String CAT_021_MSG();

    @Key("CAT_021_CODE")
    String CAT_021_CODE();

    @Key("CAT_022_MSG")
    String CAT_022_MSG();

    @Key("CAT_022_CODE")
    String CAT_022_CODE();

    @Key("PROJ_023_MSG")
    String PROJ_023_MSG();

    @Key("OAUTH_023_MODIFIED_MSG")
    String OAUTH_023_MODIFIED_MSG();

    @Key("OAUTH_023_MODIFIED_CODE")
    String OAUTH_023_MODIFIED_CODE();

    @Key("OAUTH_023_MSG")
    String OAUTH_023_MSG();

    @Key("OAUTH_023_CODE")
    String OAUTH_023_CODE();

    @Key("OAUTH_027_MSG")
    String OAUTH_027_MSG();

    @Key("OAUTH_027_CODE")
    String OAUTH_027_CODE();

    @Key("OAUTH_057_MSG")
    String OAUTH_057_MSG();

    @Key("OAUTH_057_CODE")
    String OAUTH_057_CODE();

    @Key("PROJ_023_CODE")
    String PROJ_023_CODE();

    @Key("PROJ_024_MSG")
    String PROJ_024_MSG();

    @Key("PROJ_024_CODE")
    String PROJ_024_CODE();

    @Key("PROJ_060_MSG")
    String PROJ_060_MSG();

    @Key("PROJ_060_CODE")
    String PROJ_060_CODE();

    @Key("PROJ_061_MSG")
    String PROJ_061_MSG();

    @Key("PROJ_061_CODE")
    String PROJ_061_CODE();

    @Key("PROJ_142_MSG")
    String PROJ_142_MSG();

    @Key("PROJ_142_CODE")
    String PROJ_142_CODE();

    @Key("SUBCAT_016_MSG")
    String SUBCAT_016_MSG();

    @Key("SUBCAT_016_CODE")
    String SUBCAT_016_CODE();

    @Key("SUBCAT_019_MSG")
    String SUBCAT_019_MSG();

    @Key("SUBCAT_019_CODE")
    String SUBCAT_019_CODE();

    @Key("SUBCAT_020_MSG")
    String SUBCAT_020_MSG();

    @Key("SUBCAT_020_CODE")
    String SUBCAT_020_CODE();

    @Key("SUBCAT_021_MSG")
    String SUBCAT_021_MSG();

    @Key("SUBCAT_021_CODE")
    String SUBCAT_021_CODE();

}
