package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.Review;
import com.example.tmall_springboot.repositories.ReviewRepository;
import com.example.tmall_springboot.services.ReviewService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewJpaService implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewJpaService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @CacheEvict(allEntries = true)
    public Review add(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Cacheable(key = "'reviews-pid-' + #p0.id")
    public List<Review> list(Product product) {
        return reviewRepository.findByProductOrderByIdDesc(product);
    }

    @Override
    @Cacheable(key = "'reviews-count-pid-' + #p0.id")
    public int getCount(Product product) {
        return reviewRepository.countByProduct(product);
    }
}
