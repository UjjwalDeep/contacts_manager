package com.ujjwal.scm.controllers;

import com.ujjwal.scm.entities.Contact;
import com.ujjwal.scm.entities.User;
import com.ujjwal.scm.forms.ContactForm;
import com.ujjwal.scm.helpers.Helper;
import com.ujjwal.scm.services.ContactService;
import com.ujjwal.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public String addContactView(Model model){


        ContactForm contactForm = new ContactForm();
        contactForm.setName("ujjwaldeep");
        contactForm.setFavourite(true);

        model.addAttribute("contactForm",contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String saveContact(@ModelAttribute ContactForm contactForm, Authentication authentication){

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavourite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setEmail(contactForm.getEmail());
        contact.setDescription(contactForm.getDescription());
        contact.setAddress(contactForm.getAddress());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setUser(user);

        contactService.save(contact);

        //System.out.println(contactForm);

        return "redirect:/user/contacts/add";
    }

}
