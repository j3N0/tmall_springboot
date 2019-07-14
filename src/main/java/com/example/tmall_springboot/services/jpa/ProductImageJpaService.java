package com.example.tmall_springboot.services.jpa;


import com.example.tmall_springboot.domains.OrderItem;
import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.ProductImage;
import com.example.tmall_springboot.repositories.ProductImageRepository;
import com.example.tmall_springboot.services.ProductImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageJpaService implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImageJpaService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    public ProductImage add(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Override
    public void delete(Long id) {
        productImageRepository.deleteById(id);
    }

    @Override
    public ProductImage get(Long id) {
        return productImageRepository.getOne(id);
    }


    @Override
    public List<ProductImage> listProductImages(Product product, String type) {
        return productImageRepository.findByProductAndTypeOrderByIdDesc(product, type);
    }

    @Override
    public void setFirstProductImage(Product product) {

        ProductImage productImage = listProductImages(product, TYPE_SINGLE)
                .stream()
                .findFirst()
                .orElse(new ProductImage());

        product.setFirstProductImage(productImage);
    }

    @Override
    public void setFirstProductImages(List<Product> products) {
        products.forEach(this::setFirstProductImage);
    }

    @Override
    public void setFirstProductImagesOnOrderItems(List<OrderItem> orderItems) {
        orderItems.stream().map(OrderItem::getProduct).forEach(this::setFirstProductImage);
    }
}
