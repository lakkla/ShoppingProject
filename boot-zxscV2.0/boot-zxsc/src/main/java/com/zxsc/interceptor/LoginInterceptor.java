package com.zxsc.interceptor;

import com.zxsc.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String uri = request.getRequestURI();
        
        if (user == null) {
            if (uri.startsWith("/admin")) {
                response.sendRedirect("/login");
                return false;
            }
            if (!uri.equals("/login") && !uri.equals("/register") && 
                !uri.equals("/user/login") && !uri.equals("/user/register")) {
                response.sendRedirect("/login");
                return false;
            }
        } else {
            if (uri.startsWith("/admin") && user.getRole() != 1) {
                response.sendRedirect("/product/list");
                return false;
            }
        }
        
        return true;
    }
}
