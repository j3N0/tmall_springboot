package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.Review;

import java.util.List;

public interface ReviewService {

    Review add(Review review);

    List<Review> list(Product product);

    int getCount(Product product);
}
