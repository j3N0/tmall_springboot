package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.domains.Property;

import java.util.List;

public interface PropertyService extends PageKeyService<Property> {

    Property add(Property property);

    void delete(Long id);

    Property get(Long id);

    Property update(Property property);

    List<Property> listByCategory(Category category);
}
