package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.repositories.CategoryRepository;
import com.example.tmall_springboot.repositories.ProductRepository;
import com.example.tmall_springboot.services.OrderItemService;
import com.example.tmall_springboot.services.ProductImageService;
import com.example.tmall_springboot.services.ProductService;
import com.example.tmall_springboot.services.ReviewService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.tmall_springboot.config.Const.NAVIGATE_PAGES;

@Service
public class ProductJpaService implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;
    private final OrderItemService orderItemService;
    private final ReviewService reviewService;

    public ProductJpaService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductImageService productImageService, OrderItemService orderItemService, ReviewService reviewService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageService = productImageService;
        this.orderItemService = orderItemService;
        this.reviewService = reviewService;
    }

    @Override
    public Product add(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product get(Long id) {
        return productRepository.getOne(id);
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> listByCategory(Category category) {
        return productRepository.findByCategoryOrderById(category);
    }

    @Override
    public void fill(List<Category> categories) {
        categories.forEach(this::fill);
    }

    @Override
    public void fill(Category category) {
        List<Product> products = listByCategory(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }

    @Override
    public void fillByRow(List<Category> categories) {
        categories.forEach(category -> {
            List<Product> products =  category.getProducts();
            List<List<Product>> productsByRow = splitProducts(products);
            category.setProductsByRow(productsByRow);
        });
    }

    /*
    将产品按行切分
    */
    private List<List<Product>> splitProducts(List<Product> products) {
        List<List<Product>> productsByRow =  new ArrayList<>();
        int limit = countStep(products.size());
        for (int i = 0; i < limit; i++) {
            productsByRow.add(
                    products.stream()
                            .skip(i * PRODUCT_NUMBER_EACH_ROW)
                            .limit(PRODUCT_NUMBER_EACH_ROW)
                            .collect(Collectors.toList()));
        }
        return productsByRow;
    }

    /*
    计算切分步数
    */
    private int countStep(int size) {
        return (size + PRODUCT_NUMBER_EACH_ROW - 1) / PRODUCT_NUMBER_EACH_ROW;
    }

    @Override
    public Page4Navigator<Product> pageFromJpa(Long key, int start, int size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Category category = categoryRepository.getOne(key);

        return new Page4Navigator<>(productRepository.findByCategory(category, pageable), NAVIGATE_PAGES);
    }

    @Override
    public void setSaleAndReviewNumber(List<Product> products) {
        products.forEach(this::setSaleAndReviewNumber);
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);
        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);
    }

    @Override
    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        return productRepository.findByNameLike("%" + keyword + "%", pageable);
    }
}
