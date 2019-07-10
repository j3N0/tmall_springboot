package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.User;

public interface UserService extends PageService<User> {

    boolean isExist(String name);

    User getByName(String name);

    void add(User user);
}
