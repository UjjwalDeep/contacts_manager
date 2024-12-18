package com.ujjwal.scm.config;

import com.ujjwal.scm.entities.Providers;
import com.ujjwal.scm.entities.User;
import com.ujjwal.scm.helpers.AppConstants;
import com.ujjwal.scm.repo.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        //identify the provider

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        oAuth2User.getAttributes().forEach((key,value)->{
            logger.info("{} => {}",key,value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);

        if(authorizedClientRegistrationId.equalsIgnoreCase(Providers.GOOGLE.name())){

            //google

            user.setEmail(oAuth2User.getAttribute("email").toString());
            user.setName(oAuth2User.getAttribute("name").toString());
            user.setProfilePic(oAuth2User.getAttribute("picture").toString());
            user.setProvider(Providers.GOOGLE);
            user.setProviderUserId(oAuth2User.getName());
            user.setAbout("This account is created using google");


        }else if(authorizedClientRegistrationId.equalsIgnoreCase(Providers.GITHUB.name())){

            //github

            String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString() :
                    oAuth2User.getAttribute("login").toString()+"@github.com";
                String picture = oAuth2User.getAttribute("avatar_url").toString();
                String name = oAuth2User.getAttribute("login").toString();
                String providerUserId = oAuth2User.getName();

                user.setEmail(email);
                user.setName(name);
                user.setProfilePic(picture);
                user.setProvider(Providers.GITHUB);
                user.setProviderUserId(providerUserId);
                user.setAbout("This account is created using github");

        }else{
            logger.info("OAuthAuthenticationSuccessHandler: Unknown provider");
        }



        //DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

//        logger.info(user.getName());
//
//        user.getAttributes().forEach((key, value) ->{
//            logger.info("{} => {}", key, value);
//        });
//
//        logger.info(user.getAuthorities().toString());

//        User user1 = new User();
//        user1.setName(user.getAttribute("name").toString());
//        user1.setEmail(user.getAttribute("email").toString());
//        user1.setProfilePic(user.getAttribute("picture").toString());
//        user1.setPassword("password");
//        user1.setProvider(Providers.GOOGLE);
//        user1.setEnabled(true);
//        user1.setEmailVerified(true);
//        user1.setUserId(UUID.randomUUID().toString());
//        user1.setProviderUserId(user.getName());
//        user1.setRoleList(List.of(AppConstants.ROLE_USER));
//        user1.setAbout("This account is created using google");
//
//        User user2 = userRepo.findByEmail(user.getAttribute("email").toString()).orElse(null);
//        if(user2 == null){
//        userRepo.save(user1);
//            logger.info("User saved"+user.getAttribute("email").toString());
//        }


        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
         if(user2 == null){
        userRepo.save(user);
            logger.info("User saved"+user.getEmail());
        }


        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

    }
}
