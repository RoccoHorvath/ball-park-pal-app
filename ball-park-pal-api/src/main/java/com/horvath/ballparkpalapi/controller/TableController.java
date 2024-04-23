package com.horvath.ballparkpalapi.controller;

import com.horvath.ballparkpalapi.model.Prop;
import com.horvath.ballparkpalapi.service.PropsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class TableController {

    @GetMapping("/batterProps")
    public String batterPropsTable(Model model){
        try {
            Map<String, List<Prop>> batterProps = PropsService.createBatterProps(null,null);
            model.addAttribute("hits", batterProps.get("Hits"));
            return "BatterProps";
        } catch (IOException e) {
            return "404";
        }
    }
}
