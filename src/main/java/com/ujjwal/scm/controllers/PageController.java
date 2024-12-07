package com.ujjwal.scm.controllers;

import com.ujjwal.scm.entities.User;
import com.ujjwal.scm.forms.UserForm;
import com.ujjwal.scm.helpers.Message;
import com.ujjwal.scm.helpers.MessageType;
import com.ujjwal.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

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
    public String registerPage(Model model) {
        System.out.println("signup page loading");
        UserForm userForm = new UserForm();
        userForm.setName("Ujjwal");
        model.addAttribute("userForm",userForm);
        return "register";
    }

    @RequestMapping(value = "/do-register",method = RequestMethod.POST)
    //@PostMapping("/do-register")
    public String processRegister(@ModelAttribute UserForm userForm, HttpSession session){
        System.out.println("Processing Registration");
        System.out.println(userForm);

//        User user = User.builder()
//                .name(userForm.getName())
//                .email(userForm.getEmail())
//                .password(userForm.getPassword())
//                .phoneNumber(userForm.getPhoneNumber())
//                .about(userForm.getAbout())
//                .profilePic("file:///C:/Users/ujjwa/Downloads/man.png")
//                .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setProfilePic("file:///C:/Users/ujjwa/Downloads/man.png");
        user.setPhoneNumber(userForm.getPhoneNumber());

        User savedUser = userService.saveUser(user);
        System.out.println("user saved"+savedUser);

        //message handling

        Message message = Message.builder().content("Registration Successful").type(MessageType.blue).build();

        session.setAttribute("message",message);





        return "redirect:/register";
    }
}
