package com.example.tmall_springboot.services;

import com.example.tmall_springboot.utils.Page4Navigator;

public interface PageKeyService<T> {

    Page4Navigator<T> pageFromJpa(Long key, int start, int size);
}
