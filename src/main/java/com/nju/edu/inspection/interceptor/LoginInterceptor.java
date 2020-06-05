package com.nju.edu.inspection.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object userinfo = session.getAttribute("username");
        if(userinfo == null){
//            todo 把所有sout替换为log
            System.out.println("Inceptor prehandling!");
            response.sendRedirect("/inspection/login");
            return false;
        }
        return true;
    }
}
