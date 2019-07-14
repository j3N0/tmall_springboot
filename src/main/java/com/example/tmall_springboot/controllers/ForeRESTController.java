package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.*;
import com.example.tmall_springboot.services.*;
import com.example.tmall_springboot.utils.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class ForeRESTController {

    //Todo Refactoring God Class
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final PropertyValueService propertyValueService;
    private final ReviewService reviewService;
    private final OrderItemService orderItemService;
    private final UserService userService;

    public ForeRESTController(CategoryService categoryService, ProductService productService, ProductImageService productImageService, PropertyValueService propertyValueService, ReviewService reviewService, OrderItemService orderItemService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.productImageService = productImageService;
        this.propertyValueService = propertyValueService;
        this.reviewService = reviewService;
        this.orderItemService = orderItemService;
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

    @GetMapping("forecategory/{cid}")
    public Object category(@PathVariable Long cid, String sort) {
        Category c = categoryService.get(cid);
        productService.fill(c);
        productService.setSaleAndReviewNumber(c.getProducts());
        categoryService.removeCategoryFromProduct(c);

        if (null  != sort) {
            switch (sort) {
                case "review":
                    Collections.sort(c.getProducts(), (p1, p2) -> p2.getReviewCount() - p1.getReviewCount());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(), Comparator.comparing(Product::getCreateDate));
                    break;

                case "saleCount" :
                    Collections.sort(c.getProducts(), (p1, p2) -> p2.getSaleCount() - p1.getSaleCount());
                    break;

                case "price":
                    Collections.sort(c.getProducts(), (p1, p2) -> (int) (p1.getPromotePrice() - p2.getPromotePrice()));
                    break;

                case "all":
                    Collections.sort(c.getProducts(), (p1, p2) -> p2.getReviewCount() * p2.getSaleCount() - p1.getReviewCount() * p1.getSaleCount());
                    break;
            }
        }

        return c;
    }

    @PostMapping("foresearch")
    public Object search(String keyword){
        if(null == keyword) { keyword = ""; }           //Todo Optionoal
        List<Product> products = productService.search(keyword, 0, 20);
        productImageService.setFirstProductImages(products);
        productService.setSaleAndReviewNumber(products);
        return products;
    }

    @GetMapping("forebuyone")
    public Object buyOne(Long pid, int num, HttpSession session) {
        return buyOneAndAddCart(pid, num, session);
    }

    private Long buyOneAndAddCart(Long pid, int num, HttpSession session) {
        Product product = productService.get(pid);
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user);

        Optional<OrderItem> optionalOrderItem = orderItemService.listByUser(user)
                .stream()
                .filter(orderItem -> orderItem.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (optionalOrderItem.isPresent()) {

            OrderItem orderItem = optionalOrderItem.get();
            orderItem.setNumber(orderItem.getNumber() + num);
            orderItemService.update(orderItem);
            return orderItem.getId();
        } else {

            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setProduct(product);
            orderItem.setNumber(num);
            OrderItem savedOrderItem = orderItemService.add(orderItem);
            return savedOrderItem.getId();
        }
    }

    @GetMapping("forebuy")
    public Object buy(String[] oiid, HttpSession session) {
        List<OrderItem> orderItems = new ArrayList<>();

        Float total = Arrays.stream(oiid)
                .map(id -> orderItemService.get(Long.parseLong(id)))
                .peek(orderItems::add)
                .map(orderItem -> orderItem.getProduct().getPromotePrice() * orderItem.getNumber())
                .reduce(0f, Float::sum);

        productImageService.setFirstProductImagesOnOrderItems(orderItems);

        session.setAttribute("ois", orderItems);

        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", orderItems);
        map.put("total", total);
        return Result.success(map);
    }


}
