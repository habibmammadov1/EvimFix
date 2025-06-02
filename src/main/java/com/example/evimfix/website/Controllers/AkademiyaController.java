package com.example.evimfix.website.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AkademiyaController {
    @GetMapping("/")
    public String index(Model model) {
        return "/website/indexWeb";
    }
}
