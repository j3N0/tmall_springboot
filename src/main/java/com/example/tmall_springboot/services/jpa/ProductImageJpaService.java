package com.example.tmall_springboot.services.jpa;


import com.example.tmall_springboot.domains.OrderItem;
import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.ProductImage;
import com.example.tmall_springboot.repositories.ProductImageRepository;
import com.example.tmall_springboot.services.ProductImageService;
import com.example.tmall_springboot.utils.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageJpaService implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImageJpaService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    @CacheEvict(allEntries = true)
    public ProductImage add(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        productImageRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'productImages-one-' + #p0")
    public ProductImage get(Long id) {
        return productImageRepository.findById(id).orElseThrow(RuntimeException::new);
    }


    @Override
    @Cacheable(key="'productImages-' + #p1 + '-pid-'+ #p0.id")
    public List<ProductImage> listProductImages(Product product, String type) {
        return productImageRepository.findByProductAndTypeOrderByIdDesc(product, type);
    }

    @Override
    public void setFirstProductImage(Product product) {
        ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);

        ProductImage productImage = productImageService.listProductImages(product, TYPE_SINGLE)
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
