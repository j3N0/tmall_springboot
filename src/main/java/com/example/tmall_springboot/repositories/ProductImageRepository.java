package com.example.tmall_springboot.repositories;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);
}
