package com.horvath.ballparkpalapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatterPropCategory {

    @JsonProperty("O Hits 0.5")
    private List<BatterProp> hits;

    @JsonProperty("O HR 0.5")
    private List<BatterProp> homeruns;

    @JsonProperty("O Bases 1.5")
    private List<BatterProp> bases;

}
