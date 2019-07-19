package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.services.CategoryService;
import com.example.tmall_springboot.utils.ImageUtil;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Page4Navigator<Category> listCategories(@RequestParam(value = "start", defaultValue = "0") int start,
                                                   @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        return categoryService.pageFromJpa(start, size);
    }

    @PostMapping("/categories")
    public Object add(Category category, MultipartFile image, HttpServletRequest request) throws Exception {
        categoryService.add(category);
        saveOrUpdateImageFile(category, image, request);
        return category;
    }

    private void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        File imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, bean.getId()+".jpg");
        if(!file.getParentFile().exists()) { file.getParentFile().mkdirs(); }
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request)  throws Exception {
        categoryService.delete(id);
        File  imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category get(@PathVariable Long id) throws Exception {
        return categoryService.get(id);
    }

    @PutMapping("/categories/{id}")
    public Object update(Category category, MultipartFile image,HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        category.setName(name);
        categoryService.update(category);

        if(image != null) {
            saveOrUpdateImageFile(category, image, request);
        }
        return category;
    }
}
