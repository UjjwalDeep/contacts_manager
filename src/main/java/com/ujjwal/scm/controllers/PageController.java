package com.ujjwal.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home page handler");
        model.addAttribute("name","Substring Technologies");
        model.addAttribute("youtubeChannel","Learn code with ujjwal");
        model.addAttribute("github","https://github.com/UjjwalDeep");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin",false);
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    @RequestMapping("/contact")
    public String ContactPage() {
        System.out.println("contact page loading");
        return "contact";
    }

    @RequestMapping("/login")
    public String loginPage() {
        System.out.println("login page loading");
        return "login";
    }

    @RequestMapping("/register")
    public String registerPage() {
        System.out.println("signup page loading");
        return "register";
    }
}
