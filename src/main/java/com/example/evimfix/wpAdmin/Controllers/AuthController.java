package com.example.evimfix.wpAdmin.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.thymeleaf.util.StringUtils;

import com.example.evimfix.wpAdmin.Models.Auth;
import com.example.evimfix.wpAdmin.Models.Role;
import com.example.evimfix.wpAdmin.Services.AuthService;
import com.example.evimfix.wpAdmin.Utils.Exceptions.InvalidTokenException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/wp-admin")
public class AuthController {
    private static final String REDIRECT_LOGIN = "redirect:/wp-admin/login";

    @Autowired
    private AuthService authService;

    @Autowired
    private MessageSource messageSource;
    
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView model = new ModelAndView("/login/login");

        Auth auth = new Auth();

        model.addObject("pageName", "Giriş");
        model.addObject("user", auth);
        
        return model;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new-user")
    public ModelAndView newUserPage() {
        ModelAndView model = new ModelAndView("/users/new_user");
        model.addObject("pageName", messageSource.getMessage("newUser", null, LocaleContextHolder.getLocale()));

        Auth auth = new Auth();
        model.addObject("user", auth);

        List<Role> rollar = authService.getRollar();
        model.addObject("rollar", rollar);
        return model;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new-user")
    public ModelAndView addNewUser(@Valid Auth auth, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView();

        model.setViewName("redirect:/wp-admin/new-user");

        if (bindingResult.hasErrors()) {
            //redirectAttributes.addFlashAttribute("user", auth);
            model.setViewName("users/new_user");
            model.addObject("user", auth);
            bindingResult.getAllErrors().forEach(error -> System.out.println("Error: " + error.getDefaultMessage() + " " + error.getCode()));
			return model;
		}

        String result = authService.addUser(auth);

        //model.addObject("result", result);
        redirectAttributes.addFlashAttribute("result", result);
        
        return model;
    }

    @GetMapping("/users")
    public ModelAndView users() {
        ModelAndView model = new ModelAndView("/users/users");
        List<Auth> users = authService.getUsers();

        // for (Auth auth : users) {
        //     System.out.println(auth.getUsername());
        // }

        model.addObject("pageName",  messageSource.getMessage("users", null, LocaleContextHolder.getLocale()));
        model.addObject("users", users);

        return model;
    }

    @GetMapping("/update-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateUsers(RedirectAttributes redirectAttributes, @RequestParam int id) { 
        ModelAndView model = new ModelAndView("/users/update_user");

        Optional<Auth> auth = authService.getUserById(id);

        if (auth.get() != null) {
            model.addObject("user", auth);

            if (auth.get().getRole().equals("ROLE_ADMIN")) {
                model.addObject("isAdmin", "readonly");
            }
        }
        else{
            redirectAttributes.addFlashAttribute("result", "NO");
            model.setViewName("redirect:/wp-admin/users");
        }

        List<Role> rollar = authService.getRollar();
        model.addObject("rollar", rollar);

        model.addObject("pageName",  messageSource.getMessage("updateUser", null, LocaleContextHolder.getLocale()));

        return model;
    }
    
    @PostMapping("/update-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateUser(RedirectAttributes redirectAttributes, Auth auth) {
        ModelAndView model = new ModelAndView();

        String result = authService.updateUser(auth);

        redirectAttributes.addFlashAttribute("result", result);
        
        redirectAttributes.addAttribute("id", auth.getId());
        model.setViewName("redirect:/wp-admin/update-user");
        return model;
    }

    // Profile page
    @PostMapping("/update-profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView updateProfile(Auth auth) {
        ModelAndView model = new ModelAndView();

        String result = authService.updateProfile(auth);

        model.setViewName("redirect:/wp-admin/profile?result=" + result);
        return model;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(@RequestParam(required = false, defaultValue = "") String result) {
        ModelAndView model = new ModelAndView("/users/profile");
        model.addObject("pageName", messageSource.getMessage("myProfile", null, LocaleContextHolder.getLocale()));

        final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        //System.out.println(currentUserName);

        Optional<Auth> authOptional = authService.getUserByUsername(currentUserName);

        if (authOptional.isPresent()) {
            Auth auth = authOptional.get();
            model.addObject("user", auth);
        }

        else{
            model.setViewName("redirect:/wp-admin/");
        }        

        model.addObject("result", result);

        return model;
    }

    @GetMapping("/delete-user")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView deleteUser(RedirectAttributes redirectAttributes, @RequestParam String username) {
        ModelAndView model = new ModelAndView("redirect:/wp-admin/users");

        String deleteResult = authService.deleteUser(username);

        redirectAttributes.addFlashAttribute("result", deleteResult);

        return model;
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String token, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(token)) {
            redirectAttributes.addFlashAttribute("tokenError", "Token is invlid");
            return REDIRECT_LOGIN;
        }
        
        try {
            authService.verifyUser(token);
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("tokenError", "Token is invalid");
            return REDIRECT_LOGIN;
        }

        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("validated", null, LocaleContextHolder.getLocale()));
        return REDIRECT_LOGIN;
    }
}