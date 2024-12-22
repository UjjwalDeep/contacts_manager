package com.ujjwal.scm.controllers;

import com.ujjwal.scm.entities.Contact;
import com.ujjwal.scm.entities.User;
import com.ujjwal.scm.forms.ContactForm;
import com.ujjwal.scm.forms.ContactSearchForm;
import com.ujjwal.scm.helpers.AppConstants;
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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        //image processing
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {

            String fileName = UUID.randomUUID().toString();

            String fileURL = imageService.uploadImage(contactForm.getContactImage(), fileName);

            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(fileName);

        }


        contactService.save(contact);

        //System.out.println(contactForm);

        session.setAttribute("message", Message.builder().content("Contact saved successfully").type(MessageType.green).build());

        return "redirect:/user/contacts/add";
    }

    //view contacts

    @RequestMapping
    public String viewContacts(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue= AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
            @RequestParam(value = "direction",defaultValue = "asc") String direction
            ,Model model,Authentication authentication){
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user =  userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);



        model.addAttribute("contacts",pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm",new ContactSearchForm());

        return "user/contacts";
    }

    //search handler

    @RequestMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue= AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
            @RequestParam(value = "direction",defaultValue = "asc") String direction,
            Model model, Authentication authentication
    ){

        String field = contactSearchForm.getField();
        String value = contactSearchForm.getValue();

        logger.info("field : {} keyword : {}",field,value);

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));


        Page<Contact> pageContact = null;

        if(field.equalsIgnoreCase("name")){
           pageContact = contactService.searchByName(value,page,size,sortBy,direction,user);
        }else if(field.equalsIgnoreCase("email")){
           pageContact = contactService.searchByEmail(value,page,size,sortBy,direction,user);
        }else if(field.equalsIgnoreCase("phone")){
           pageContact = contactService.searchByPhoneNumber(value,page,size,sortBy,direction,user);
        }

        model.addAttribute("contacts",pageContact);
        model.addAttribute("pageSize", size);
        model.addAttribute("contactSearchForm",contactSearchForm);

        System.out.println("pageContact : "+pageContact +"pageSize : "+size);

        return "user/search";

       // return "user/contacts";
    }

    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id,
                                HttpSession session){

        contactService.delete(id);

        logger.info("contactId : {} deleted",id);

        session.setAttribute("message",
                Message.builder()
                        .content("Contact deleted successfully")
                        .type(MessageType.green)
                        .build());


        return "redirect:/user/contacts";
    }

    //update contact

    @GetMapping("view/{id}")
    public String updateContactFromView(
            @PathVariable String id,
            Model model
    ){

        var contact = contactService.getById(id);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setFavourite(contact.isFavorite());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setEmail(contact.getEmail());
        contactForm.setDescription(contact.getDescription());
        contactForm.setAddress(contact.getAddress());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedinLink(contact.getLinkedinLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm",contactForm);
        model.addAttribute("contactId",id);

        return "user/update_contact_view";
    }

    @RequestMapping(value = "/update/{contactId}",method = RequestMethod.POST)
    public String updateContact(
            @PathVariable String contactId,
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            Model model,
            HttpSession session

    ){

        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }

        var con = new Contact();
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setFavorite(contactForm.isFavourite());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setEmail(contactForm.getEmail());
        con.setDescription(contactForm.getDescription());
        con.setAddress(contactForm.getAddress());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedinLink(contactForm.getLinkedinLink());
        //con.setPicture(contactForm.getPicture());
        //process image upload

        if(contactForm.getContactImage() != null &&
        !contactForm.getContactImage().isEmpty()){


        String fileName = UUID.randomUUID().toString();
        String imageURL = imageService.uploadImage(contactForm.getContactImage(),
                fileName);

        con.setPicture(imageURL);
        con.setCloudinaryImagePublicId(fileName);

        }

        var updatedCon = contactService.update(con);
        logger.info("updated contact : {}",updatedCon);

        session.setAttribute("message",
                Message.builder()
                        .content("Contact updated successfully")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contacts/view/"+contactId;
    }
}
