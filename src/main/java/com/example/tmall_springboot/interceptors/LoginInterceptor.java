package com.example.tmall_springboot.interceptors;

import antlr.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String[] AuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };

        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String page = StringUtils.stripFront(request.getRequestURI(), contextPath + "/");

        if (beginWith(page, AuthPages) && session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return false;
        }
        return true;
    }

    private boolean beginWith(String page, String[] AuthPages) {
        return Arrays.stream(AuthPages).anyMatch(page::startsWith);
    }

}
