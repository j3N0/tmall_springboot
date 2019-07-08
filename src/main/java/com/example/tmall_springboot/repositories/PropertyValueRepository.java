package com.example.tmall_springboot.repositories;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.Property;
import com.example.tmall_springboot.domains.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueRepository extends JpaRepository<PropertyValue, Long> {

    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    PropertyValue getByPropertyAndProduct(Property property, Product product);
}
