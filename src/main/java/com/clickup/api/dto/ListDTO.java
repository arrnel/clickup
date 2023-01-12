package com.clickup.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = false, chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("orderindex")
    private Integer orderIndex;

    @JsonProperty("content")
    private String content;

    @JsonProperty("color")
    private String color;

    @JsonProperty("folder")
    private FolderDTO folder;

    @JsonProperty("space")
    private SpaceDTO space;

    @JsonProperty("archived")
    private boolean isArchived;

    @JsonIgnore
    private String folderId;


}
