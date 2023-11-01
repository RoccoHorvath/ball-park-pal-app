package com.horvath.ballparkpalapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PitcherData {
    private Map<String, List<PitcherProp>> pitchers;
}
