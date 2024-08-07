package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupQueryResponse {

    private String description;
    private String displayName;


    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    // Setter Methods
    public void setDescription( String description ) {
        this.description = description;
    }

    public void setDisplayName( String displayName ) {
        this.displayName = displayName;
    }


}