package com.example.tmall_springboot.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "oid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    private int number;
}
