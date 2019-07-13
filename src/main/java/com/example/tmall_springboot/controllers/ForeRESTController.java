package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.*;
import com.example.tmall_springboot.services.*;
import com.example.tmall_springboot.utils.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ForeRESTController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final PropertyValueService propertyValueService;
    private final ReviewService reviewService;
    private final UserService userService;

    public ForeRESTController(CategoryService categoryService, ProductService productService, ProductImageService productImageService, PropertyValueService propertyValueService, ReviewService reviewService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.productImageService = productImageService;
        this.propertyValueService = propertyValueService;
        this.reviewService = reviewService;
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

    @GetMapping("/foreproduct/{pid}")
    public Result product(@PathVariable Long pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listProductImages(product, productImageService.TYPE_SINGLE);
        List<ProductImage> productDetailImages = productImageService.listProductImages(product, productImageService.TYPE_DETAIL);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> propertyValues = propertyValueService.list(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);

        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pvs", propertyValues);
        map.put("reviews", reviews);

        return Result.success(map);
    }

    @GetMapping("forecheckLogin")
    public Object checkLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");        //ToDo Optional
        if (null != user) { return Result.success(); }
        return Result.fail("未登录");
    }

}
