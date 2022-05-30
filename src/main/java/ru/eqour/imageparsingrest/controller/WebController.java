package ru.eqour.imageparsingrest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {

    @GetMapping("/")
    public String test() {
        return "index";
    }

    @GetMapping("/editor")
    public String editor() {
        return "editor";
    }

}
