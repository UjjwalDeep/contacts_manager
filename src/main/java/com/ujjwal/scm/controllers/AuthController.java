package com.ujjwal.scm.controllers;

import com.ujjwal.scm.helpers.Helper;
import com.ujjwal.scm.helpers.Message;
import com.ujjwal.scm.helpers.MessageType;
import com.ujjwal.scm.repo.UserRepo;
import com.ujjwal.scm.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {

    //verify email

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam(value = "token") String token,
                              @RequestParam(value = "userId") String userId,
                              HttpSession session,
                              Model model){

        var user = userRepo.findByEmailToken(token).orElse(null);

        if(user != null){

            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);

                session.setAttribute("message",
                        Message.builder()
                                .content("Email verified successfully. You can login now")
                                .type(MessageType.green)
                                .build());

                return "success_page";
            }
        }

            session.setAttribute("message",
                    Message.builder()
                            .content("Invalid Token, Email verification failed")
                            .type(MessageType.red)
                            .build());


        model.addAttribute("userId",userId);

            return "error_page";

    }

    @RequestMapping(value = "/resend-link",method = RequestMethod.POST)
    public String resendLink(@RequestParam(value = "userId") String userId,
                             HttpSession session){

        var user = userRepo.findById(userId).orElse(null);

        if(user == null){
            session.setAttribute("message",
                    Message.builder()
                            .content("User not found, Email verification failed")
                            .type(MessageType.red)
                            .build());
            return "error_page";
        }

        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        var savedUser = userRepo.save(user);

        String emailLink = Helper.getLinkForEmailVerification(emailToken, userId);
        emailService.sendEmail(savedUser.getEmail(), "Resend Email Verification: SCM", emailLink);

        return "resend_link";
    }
}
