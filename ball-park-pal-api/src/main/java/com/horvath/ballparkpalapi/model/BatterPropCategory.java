package com.horvath.ballparkpalapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatterPropCategory {

    @JsonProperty("Hits")
    private List<BatterProp> hits;

    @JsonProperty("Home Runs")
    private List<BatterProp> homeruns;

    @JsonProperty("Bases")
    private List<BatterProp> bases;

}
