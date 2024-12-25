package com.ujjwal.scm.helpers;

import com.ujjwal.scm.entities.Providers;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.security.Principal;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){

        //AuthenticatedPrincipal principal = (AuthenticatedPrincipal) authentication.getPrincipal();

        if(authentication instanceof OAuth2AuthenticationToken){

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oAuth2User = (DefaultOAuth2User)authentication.getPrincipal();
            String username ="";

            if(clientId.equalsIgnoreCase(Providers.GOOGLE.name())){

                System.out.println("getting email from google");

                username = oAuth2User.getAttribute("email").toString();

            }else if(clientId.equalsIgnoreCase(Providers.GITHUB.name())){
                System.out.println("getting email from github");

                username = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString() :
                        oAuth2User.getAttribute("login").toString()+"@github.com";
            }

            return username;
        }else{
            System.out.println("getting name from local database");
            return authentication.getName();
        }


    }

    public static String getLinkForEmailVerification(String emailToken,String userId){

        String link = "http://localhost:8080/auth/verify-email?userId="+userId+"&token="+emailToken;

        return link;
    }
}
