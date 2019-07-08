package com.example.tmall_springboot.services;

import org.springframework.data.domain.Page;

public interface PageKeyService<T> {

    Page<T> pageFromJpa(Long key, int start, int size);
}
