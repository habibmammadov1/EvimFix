package com.example.evimfix.wpAdmin.Controllers;

import java.util.Optional;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "/error/404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }

    @GetMapping(value = "/previousPage")
    public @ResponseBody String rateHandler(HttpServletRequest request) {
        //your controller code
        return getPreviousPageByRequest(request).orElse("/");
    }

    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
    {
    return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:/" + requestUrl);
    }
}
