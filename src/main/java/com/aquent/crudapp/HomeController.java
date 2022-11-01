package com.aquent.crudapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple controller to render a home screen view.  In the future, we could
 * add other landing page behavior here if we were writing a real application.
 */
@Controller
public class HomeController {
    /**
     * Render a simple home screen view for site navigation.
     * In a real application this could be our landing or login page.
     *
     * @return A simple home screen view
     */
    @GetMapping("/")
    public ModelAndView index() {
//        return "redirect:/home/";
        return new ModelAndView("home/home.html");
    }
}
