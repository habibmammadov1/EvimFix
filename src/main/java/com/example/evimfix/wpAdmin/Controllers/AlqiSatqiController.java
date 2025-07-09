package com.example.evimfix.wpAdmin.Controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.evimfix.wpAdmin.Models.AlqiSatqi;
import com.example.evimfix.wpAdmin.Services.AlqiSatqiService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/wp-admin")
public class AlqiSatqiController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AlqiSatqiService alqiSatqiService;

    @GetMapping("/alqi-satqiAll")
    public ModelAndView alqiSatqi(@ModelAttribute String result) {
        ModelAndView model = new ModelAndView();
        model.setViewName("modullar/alqisatqiAll");

        model.addObject("result", result);

        model.addObject("pageName", messageSource.getMessage("commerce", null, LocaleContextHolder.getLocale()));

        model.addObject("alqiSatqiAll", alqiSatqiService.getAllAlqiSatqi());

        return model;
    }

    @GetMapping("/alqi-satqi/delete")
    public ModelAndView deleteAlqiSatqi(RedirectAttributes redirectAttributes, @RequestParam int id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/wp-admin/alqi-satqiAll");

        String result = alqiSatqiService.deleteAlqiSatqi(id);

        redirectAttributes.addFlashAttribute("result", result);

        return model;
    }

    @GetMapping("/getAlqiSatqi")
    public ModelAndView getAlqiSatqiById(@RequestParam int id, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView();
        model.setViewName("modullar/alqiSatqi");

        // DB-den ehtiyacim olan melumatlar
        model.addObject("emlakNovleri", alqiSatqiService.getEmlakNovleri());
        model.addObject("alishKirayeNovler", alqiSatqiService.getAlishKirayeNovler());
        model.addObject("sheherler", alqiSatqiService.getSheherler());
        model.addObject("rayonlar", alqiSatqiService.getRayonlar());
        model.addObject("metrolar", alqiSatqiService.getMetrolar());
        

        AlqiSatqi alqiSatqi = alqiSatqiService.getAlqiSatqiById(id).get();

        if (alqiSatqi != null) {
            model.addObject("alqiSatqi", alqiSatqi);
            model.addObject("pageName", messageSource.getMessage("home", null, LocaleContextHolder.getLocale()));
        } else {
            model.setViewName("redirect:/wp-admin/alqi-satqiAll");
            redirectAttributes.addFlashAttribute("notFound", messageSource.getMessage("notFound", null, LocaleContextHolder.getLocale()));
        }

        return model;
    }

    @PostMapping("/alqi-satqi/update")
    public ModelAndView updateAlqiSatqi(@Valid @ModelAttribute("alqiSatqi") AlqiSatqi alqiSatqi, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws ParseException {
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/wp-admin/getAlqiSatqi?id=" + alqiSatqi.getId());

        // Validate the AlqiSatqi object
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.alqiSatqi", bindingResult);
            redirectAttributes.addFlashAttribute("alqiSatqi", alqiSatqi); // Important!
            return model;
        }
        
        String result = alqiSatqiService.updateAlqiSatqi(alqiSatqi);
        redirectAttributes.addFlashAttribute("result", result);
        
        return model;
    }

    @GetMapping("/alqi-satqi/add")
    public ModelAndView addAlqiSatqi() {
        ModelAndView model = new ModelAndView();
        model.setViewName("modullar/alqiSatqiAdd");
        model.addObject("pageName", messageSource.getMessage("home", null, LocaleContextHolder.getLocale()));

        // DB-den ehtiyacim olan melumatlar
        model.addObject("emlakNovleri", alqiSatqiService.getEmlakNovleri());
        model.addObject("alishKirayeNovler", alqiSatqiService.getAlishKirayeNovler());
        model.addObject("sheherler", alqiSatqiService.getSheherler());
        model.addObject("rayonlar", alqiSatqiService.getRayonlar());
        model.addObject("metrolar", alqiSatqiService.getMetrolar());
        
        AlqiSatqi alqiSatqi = new AlqiSatqi();
        model.addObject("alqiSatqi", alqiSatqi);
        
        return model;
    }

    @PostMapping("/alqi-satqi/add")
    public ModelAndView addAlqiSatqi(AlqiSatqi alqiSatqi, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/wp-admin/alqi-satqiAll");
        
        String result = alqiSatqiService.addAlqiSatqi(alqiSatqi);
        redirectAttributes.addFlashAttribute("add", result);
        
        return model;
    }

    @GetMapping("/photos/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("/opt/evimfix-photos").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
