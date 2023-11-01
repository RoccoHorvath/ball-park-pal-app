package com.horvath.ballparkpalapi.controller;

import com.horvath.ballparkpalapi.model.BatterPropCategory;
import com.horvath.ballparkpalapi.service.PropsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class TableController {

    @GetMapping("/batterProps")
    public String batterPropsTable(Model model){
        try {
            BatterPropCategory batterPropCategory = PropsService.createBatterPropsFromJSON();
            model.addAttribute("hits", batterPropCategory.getHits());
            return "BatterProps";
        } catch (IOException e) {
            return "404";
        }
    }
}
