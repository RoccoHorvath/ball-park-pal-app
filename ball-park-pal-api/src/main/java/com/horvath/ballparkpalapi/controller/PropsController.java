package com.horvath.ballparkpalapi.controller;

import com.horvath.ballparkpalapi.model.Prop;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.horvath.ballparkpalapi.service.PropsService.*;

@RestController
public class PropsController {

    @GetMapping("/api/batterProps")
    public Map<String, List<Prop>> getBatterProp(@RequestParam(required = false) String prop, @RequestParam(required = false) String team, @RequestParam(required = false) String book) throws IOException {
        if(prop==null) {
            return createBatterProps(team,book);
        }
        return createBatterProp(prop,team,book);
    }

    @GetMapping("/api/pitcherProps")
    public Map<String, Map<String, List<Prop>>> getPitcherProps(@RequestParam(required = false) String prop, @RequestParam(required = false) String team, @RequestParam(required = false) String book) throws IOException {
        if(prop==null) {
            return createPitcherProps(team, book);
        }
        return createPitcherProp(prop,team,book);
    }


}
