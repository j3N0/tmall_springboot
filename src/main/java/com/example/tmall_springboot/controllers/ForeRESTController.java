package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.services.CategoryService;
import com.example.tmall_springboot.services.ProductService;
import com.example.tmall_springboot.services.UserService;
import com.example.tmall_springboot.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ForeRESTController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    public ForeRESTController(CategoryService categoryService, ProductService productService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/forehome")
    public List<Category> home() {
        List<Category> categoryList = categoryService.getAll();
        productService.fill(categoryList);
        productService.fillByRow(categoryList);
        categoryService.removeCategoryFromProduct(categoryList);
        return categoryList;
    }

    @PostMapping("/foreregister")
    public Object register(@RequestBody User user) {
        String name =  user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);          //TODO can implement Optional

        if(exist){
            String message ="用户名已经被使用,不能使用";
            return Result.fail(message);
        }

        user.setPassword(password);

        userService.add(user);

        return Result.success();
    }

    @PostMapping("/forelogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name =  userParam.getName();
        name = HtmlUtils.htmlEscape(name);

        User user = userService.get(name, userParam.getPassword());  //Todo use Optional
        if(null == user) {
            return Result.fail("账号密码错误");
        }

        session.setAttribute("user", user);
        return Result.success();
    }

}
