package com.example.tmall_springboot.services;

import com.example.tmall_springboot.utils.Page4Navigator;

public interface PageService<T> {

    Page4Navigator<T> pageFromJpa(int start, int size);
}
