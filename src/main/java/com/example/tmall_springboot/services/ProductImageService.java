package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.ProductImage;

import java.util.List;

public interface ProductImageService {

    String TYPE_SINGLE = "single";
    String TYPE_DETAIL = "detail";

    ProductImage add(ProductImage productImage);

    void delete(Long id);

    ProductImage get(Long id);

    List<ProductImage> listProductImages(Product product, String type);

    void setFirstProductImage(Product product);

    void setFirstProductImages(List<Product> products);
}
