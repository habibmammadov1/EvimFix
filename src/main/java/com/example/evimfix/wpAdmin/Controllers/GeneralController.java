package com.example.evimfix.wpAdmin.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.evimfix.wpAdmin.Services.GeneralService;

@Controller
@RequestMapping("/wp-admin")
public class GeneralController {
    @Autowired
    private GeneralService generalService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/elaqe")
    @PreAuthorize("isAuthenticated()")
    public String elaqe(Model model) {
        model.addAttribute("pageName", messageSource.getMessage("contact", null, LocaleContextHolder.getLocale()));
        return "/modullar/elaqe";
    }

    @GetMapping("/index")
    @PreAuthorize("isAuthenticated()")
    public String index(Model model) {
        model.addAttribute("modullar", generalService.getModullar());
        //model.addAttribute("categories", generalService.getAllCourseCategories());

        model.addAttribute("pageName", messageSource.getMessage("homePage", null, LocaleContextHolder.getLocale()));
        return "index";
    }

    @GetMapping("/modullar_umumi")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView modullaraKechid(@RequestParam String modulId){
        ModelAndView model = new ModelAndView();

        switch (modulId) {
            case "1":
                model.setViewName("redirect:/wp-admin/alqi-satqiAll");
                break;

            case "2":
                model.setViewName("redirect:/wp-admin/tedrisSaheleri/");
                break;
                
            case "3":
                model.setViewName("redirect:/wp-admin/teqaudProqramlari");
                break;

            case "4":
                model.setViewName("redirect:/wp-admin/elaqe");
                break;

            default:
                break;
        }

        return model;
    }
}
