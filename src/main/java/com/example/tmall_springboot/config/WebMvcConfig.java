package com.example.tmall_springboot.config;

import com.example.tmall_springboot.interceptors.LoginInterceptor;
import com.example.tmall_springboot.interceptors.OtherInterceptor;
import com.example.tmall_springboot.services.CategoryService;
import com.example.tmall_springboot.services.OrderItemService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CategoryService categoryService;
    private final OrderItemService orderItemService;

    public WebMvcConfig(CategoryService categoryService, OrderItemService orderItemService) {
        this.categoryService = categoryService;
        this.orderItemService = orderItemService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new OtherInterceptor(categoryService, orderItemService))
                .addPathPatterns("/**");
    }
}
