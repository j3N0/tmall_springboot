package com.example.tmall_springboot.repositories;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductOrderByIdDesc(Product product);
    int countByProduct(Product product);
}
