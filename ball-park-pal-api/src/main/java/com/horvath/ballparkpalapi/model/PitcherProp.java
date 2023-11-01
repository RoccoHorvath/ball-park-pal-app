package com.horvath.ballparkpalapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PitcherProp {
    @JsonProperty("betName")
    private String betName;
    @JsonProperty("bp")
    private Integer bp;
    @JsonProperty("fd")
    private Integer fd;
    @JsonProperty("dk")
    private Integer dk;

    private double expectedValue;
    private double betSize;
}
