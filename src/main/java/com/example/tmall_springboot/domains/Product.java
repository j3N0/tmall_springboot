package com.example.tmall_springboot.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String subTitle;

    private float originalPrice;

    private float promotePrice;

    private int stock;

    private Date createDate;

    @Transient
    private ProductImage firstProductImage;

    @Transient
    private List<ProductImage> productSingleImages;

    @Transient
    private List<ProductImage> productDetailImages;

    @Transient
    private int reviewCount;

    @Transient
    private int saleCount;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;
}
