package com.example.tmall_springboot.services;

import org.springframework.data.domain.Page;

public interface PageService<T> {

    Page<T> pageFromJpa(int start, int size);
}
