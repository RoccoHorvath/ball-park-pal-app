package com.horvath.ballparkpalapi.controller;

import com.horvath.ballparkpalapi.model.BatterPropCategory;
import com.horvath.ballparkpalapi.model.PitcherData;
import com.horvath.ballparkpalapi.service.PropsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PropsController {

    @GetMapping("/api/batterProps")
    public BatterPropCategory getBatterProps() throws IOException {
        return PropsService.createBatterPropsFromJSON();
    }

    @GetMapping("/api/pitcherProps")
    public PitcherData getPitcherProps() throws IOException {
        return PropsService.createPitcherPropsFromJSON();
    }
}
