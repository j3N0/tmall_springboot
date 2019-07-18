package com.example.tmall_springboot.interceptors;

import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.services.CategoryService;
import com.example.tmall_springboot.services.OrderItemService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OtherInterceptor implements HandlerInterceptor {

    public static final String NO_INTERCEPTOR_PATH =".*/((css)|(webjars)|(img)).*";

    private final CategoryService categoryService;
    private final OrderItemService orderItemService;

    public OtherInterceptor(CategoryService categoryService, OrderItemService orderItemService) {
        this.categoryService = categoryService;
        this.orderItemService = orderItemService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,  ModelAndView modelAndView) throws Exception {

        if (request.getServletPath().matches(NO_INTERCEPTOR_PATH)) {
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int cartTotalItems = (user == null) ? 0 : orderItemService.listByUser(user).size();

        request.getServletContext().setAttribute("categories_below_search", categoryService.getAll());
        request.getServletContext().setAttribute("contextPath", request.getServletContext().getContextPath());
        session.setAttribute("cartTotalItemNumber", cartTotalItems);
    }
}
