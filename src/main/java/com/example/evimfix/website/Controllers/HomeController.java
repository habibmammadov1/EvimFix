package com.example.evimfix.website.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.evimfix.wpAdmin.Models.AlqiSatqi;
import com.example.evimfix.wpAdmin.Services.AlqiSatqiService;
import com.example.evimfix.wpAdmin.Services.AuthService;

@Controller
public class HomeController {
    @Autowired
    private AlqiSatqiService alqiSatqiService;

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public ModelAndView index(
                            @RequestParam(required = false) Integer sheher,
                            @RequestParam(required = false) Integer rayon,
                            @RequestParam(required = false) Integer metro,
                            @RequestParam(required = false) Double minQiymet,
                            @RequestParam(required = false) Double maxQiymet,
                            @RequestParam(required = false) Integer otaqSayi,
                            @RequestParam(required = false) Integer emlakNovu,
                            @RequestParam(required = false) Integer alishKiraye
    ){
        ModelAndView model = new ModelAndView("/website/indexWeb");
        model.addObject("sheherler", alqiSatqiService.getSheherler());
        model.addObject("alishKiraye", alqiSatqiService.getAlishKirayeNovler());
        model.addObject("emlakNovler", alqiSatqiService.getEmlakNovleri());
        model.addObject("rayonlar", alqiSatqiService.getRayonlar());
        model.addObject("metrolar", alqiSatqiService.getMetrolar());

        model.addObject("sheherA", sheher);
        model.addObject("rayonA", rayon);
        model.addObject("metro", metro);

        model.addObject("minQiymet", minQiymet);
        model.addObject("maxQiymet", maxQiymet);
        model.addObject("otaqSayi", otaqSayi);
        model.addObject("emlakNovuA", emlakNovu);
        model.addObject("alishKirayeA", alishKiraye);


        model.addObject("alqiSatqiList", alqiSatqiService.getAllAlqiSatqiByCategories(
            sheher, rayon, metro, minQiymet, maxQiymet, otaqSayi, emlakNovu, alishKiraye
        ));

        return model;
    }

    @GetMapping("/property")
    public ModelAndView property(@RequestParam int id) {
        ModelAndView model = new ModelAndView("/website/evDetails");

        AlqiSatqi alqiSatqi = alqiSatqiService.getAlqiSatqiById(id).get();

        if (alqiSatqi != null) {
            model.addObject("alqiSatqi", alqiSatqiService.getAlqiSatqiById(id).get());
        }

        else{
            model.setViewName("redirect:/");
        }        

        return model;
    }
}