package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.ProductImage;
import com.example.tmall_springboot.services.ProductImageService;
import com.example.tmall_springboot.services.ProductService;
import com.example.tmall_springboot.utils.ImageUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductImageController {

    private final ProductImageService productImageService;
    private final ProductService productService;

    public ProductImageController(ProductImageService productImageService, ProductService productService) {
        this.productImageService = productImageService;
        this.productService = productService;
    }

    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@PathVariable Long pid, @RequestParam("type") String type) {

        if (!type.equals(ProductImageService.TYPE_DETAIL) && !type.equals(ProductImageService.TYPE_SINGLE)) { //Todo Maybe can use enum here
            return new ArrayList<>();
        }

        Product product = productService.get(pid);
        return productImageService.listProductImages(product, type);
    }

    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") Long pid, @RequestParam("type") String type, MultipartFile image, HttpServletRequest request) {
        Product product = productService.get(pid);
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setType(type);

        productImageService.add(productImage);

        String folder = "img/" + (productImageService.TYPE_SINGLE.equals(type) ? "productSingle" : "productDetail");
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder, productImage.getId() + ".jpg");
        String fileName = file.getName();

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ProductImageService.TYPE_SINGLE.equals(type)) {
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
        }

        return productImage;
    }

    @DeleteMapping("/productImages/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request)  throws Exception {
        ProductImage productImage = productImageService.get(id);
        productImageService.delete(id);

        String folder = "img/" + (productImageService.TYPE_SINGLE.equals(productImage.getType()) ? "productSingle" : "productDetail");

        File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,productImage.getId()+".jpg");
        String fileName = file.getName();
        file.delete();
        if (ProductImageService.TYPE_SINGLE.equals(productImage.getType())) {
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.delete();
            f_middle.delete();
        }
    }

}
