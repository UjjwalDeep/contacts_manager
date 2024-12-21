package com.ujjwal.scm.controllers;

import com.ujjwal.scm.entities.Contact;
import com.ujjwal.scm.entities.User;
import com.ujjwal.scm.forms.ContactForm;
import com.ujjwal.scm.helpers.Helper;
import com.ujjwal.scm.helpers.Message;
import com.ujjwal.scm.helpers.MessageType;
import com.ujjwal.scm.services.ContactService;
import com.ujjwal.scm.services.ImageService;
import com.ujjwal.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/add")
    public String addContactView(Model model){


        ContactForm contactForm = new ContactForm();
        //contactForm.setName("ujjwaldeep");
        //contactForm.setFavourite(true);

        model.addAttribute("contactForm",contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication,
                              HttpSession session){

        if(result.hasErrors()){
            session.setAttribute("message", Message.builder()
                    .content("Error occurred while saving contact")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);


       // logger.info("file information : {}", contactForm.getContactImage().getOriginalFilename());

        String fileName = UUID.randomUUID().toString();

        String fileURL= imageService.uploadImage(contactForm.getContactImage(), fileName);

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
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(fileName);

        contactService.save(contact);

        //System.out.println(contactForm);

        session.setAttribute("message", Message.builder().content("Contact saved successfully").type(MessageType.green).build());

        return "redirect:/user/contacts/add";
    }

    //view contacts

    @RequestMapping
    public String viewContacts(Model model,Authentication authentication){
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user =  userService.getUserByEmail(username);
        List<Contact> contacts = contactService.getByUser(user);

        model.addAttribute("contacts",contacts);

        return "user/contacts";
    }

}
