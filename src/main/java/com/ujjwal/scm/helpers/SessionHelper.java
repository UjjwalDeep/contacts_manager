package com.ujjwal.scm.helpers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {

    public static void removeMessage(){

        try {

            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();

            session.removeAttribute("message");

            System.out.println("message removed from session");

        } catch (Exception e) {
            System.out.println("Error in SessionHelper:"+e);
            e.printStackTrace();
        }
    }
}
